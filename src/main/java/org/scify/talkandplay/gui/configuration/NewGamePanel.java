/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.gui.configuration;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.services.GameService;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

/**
 *
 * @author snik
 */
public class NewGamePanel extends javax.swing.JPanel {

    private static final int MAX_IMAGES = 4;
    private User user;
    private String gameType;
    private Game game;
    private GameService gameService;
    private GamesTab parent;
    private List<JLabel> imgLabels;
    private List<GameImage> gameImages;
    private ImageIcon addIcon, soundIcon, addIconHover, soundIconHover;
    private ResourceManager rm;
    private Font buttonFont;

    public NewGamePanel(GamesTab parent, User user, String gameType) {
        this.game = new Game();
        this.gameService = new GameService();
        this.parent = parent;
        this.user = user;
        this.gameType = gameType;
        this.imgLabels = new ArrayList();
        this.gameImages = new ArrayList();
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {

        addIcon = new ImageIcon(rm.getImage("add-icon.png", ResourceType.FROM_JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        soundIcon = new ImageIcon(rm.getImage("sound-icon.png", ResourceType.FROM_JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        addIconHover = new ImageIcon(rm.getImage("add-icon-hover.png", ResourceType.FROM_JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        soundIconHover = new ImageIcon(rm.getImage("sound-icon-hover.png", ResourceType.FROM_JAR).getScaledInstance(70, 70, Image.SCALE_SMOOTH));

        imgLabels.add(img1Label);
        imgLabels.add(img2Label);
        imgLabels.add(img3Label);
        imgLabels.add(img4Label);

        for (int i = 0; i < MAX_IMAGES; i++) {
            setImageIcon(imgLabels.get(i), addIcon.getDescription());
            setImageListener(imgLabels.get(i), i);
        }

        buttonFont = new Font(UIConstants.mainFont, Font.BOLD, 12);

        addNewButton.setBackground(Color.white);
        addNewButton.setMargin(new Insets(10, 10, 10, 10));

        soundLabel.setHorizontalTextPosition(JLabel.CENTER);
        soundLabel.setVerticalTextPosition(JLabel.BOTTOM);

        soundLabel.setIcon(addIcon);
        soundLabel.setText(rm.getTextOfXMLTag("addNewSound"));
        removeSoundLabel.setVisible(false);

        errorLabel.setForeground(new java.awt.Color(153, 0, 0));

        setSoundListener();
    }

    private void setImageIcon(JLabel label, String path) {

        if (path == null || path.isEmpty() || !new File(path).exists()) {
            label.setIcon(addIcon);
        } else {
            label.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
        }
    }

    private void setImageListener(final JLabel image, final int i) {
        final JFileChooser chooser = new JFileChooser();

        image.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                chooser.setDialogTitle(rm.getTextOfXMLTag("selectImage"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "JPG", "JPEG", "gif"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    image.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
                    for (int j = 0; j < gameImages.size(); j++) {
                        if (gameImages.get(j).getOrder() == i + 1) {
                            gameImages.remove(j);
                        }
                    }
                    gameImages.add(new GameImage(path, true, i + 1));
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (image.getIcon() == addIcon) {
                    image.setIcon(addIconHover);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (image.getIcon() == addIconHover) {
                    image.setIcon(addIcon);
                }
            }
        });
    }

    private void setSoundListener() {

        soundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (game.getWinSound() == null || game.getWinSound().getResourceType() == ResourceType.MISSING) {
                    soundLabel.setIcon(addIconHover);
                } else {
                    soundLabel.setIcon(soundIconHover);
                    parent.playMedia(rm.getSound(game.getWinSound()).getAbsolutePath());
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (game.getWinSound() == null || game.getWinSound().getResourceType() == ResourceType.MISSING) {
                    soundLabel.setIcon(addIcon);
                } else {
                    soundLabel.setIcon(soundIcon);
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle(rm.getTextOfXMLTag("selectSound"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Sound Files", "mp3", "wav", "wma", "mid"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    game.setWinSound(chooser.getSelectedFile().getAbsolutePath(), ResourceType.FULL_PATH);
                    soundLabel.setIcon(soundIcon);
                    soundLabel.setText("");
                    removeSoundLabel.setVisible(true);
                }
            }
        });

        removeSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                game.setWinSound("", ResourceType.MISSING);
                soundLabel.setIcon(addIconHover);
                removeSoundLabel.setVisible(false);
                soundLabel.setText(rm.getTextOfXMLTag("addNewSound"));
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        img1Label = new javax.swing.JLabel();
        img2Label = new javax.swing.JLabel();
        img3Label = new javax.swing.JLabel();
        img4Label = new javax.swing.JLabel();
        addNewButton = new javax.swing.JButton();
        soundLabel = new javax.swing.JLabel();
        removeSoundLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        instructionLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setAutoscrolls(true);

        img1Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img1Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img1Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img1Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img1Label.setVerifyInputWhenFocusTarget(false);

        img2Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img2Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img2Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img2Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img2Label.setVerifyInputWhenFocusTarget(false);

        img3Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img3Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img3Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img3Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img3Label.setVerifyInputWhenFocusTarget(false);

        img4Label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/no-photo.png"))); // NOI18N
        img4Label.setMaximumSize(new java.awt.Dimension(90, 90));
        img4Label.setMinimumSize(new java.awt.Dimension(90, 90));
        img4Label.setPreferredSize(new java.awt.Dimension(90, 90));
        img4Label.setVerifyInputWhenFocusTarget(false);

        addNewButton.setText("Πρόσθεσέ το");
        addNewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addNewButtonMouseClicked(evt);
            }
        });

        soundLabel.setText("Προσθήκη ήχου");
        soundLabel.setAlignmentY(0.0F);

        removeSoundLabel.setText("Αφαίρεση ήχου");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(soundLabel)
                                        .addComponent(removeSoundLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(soundLabel)
                                                        .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeSoundLabel))
                                        .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        instructionLabel.setText("Για να φαίνεται όμορφο και σωστό,  προτιμήστε να ανεβάσετε τετράγωνες εικόνες");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(instructionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(instructionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(errorLabel)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addNewButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewButtonMouseClicked
        game.setImages(gameImages);
        if (validateGame()) {
            try {
                gameService.createGame(user.getName(), game, gameType);
                parent.showGamesPerType(gameType);
            } catch (Exception ex) {
                Logger.getLogger(GamesTab.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_addNewButtonMouseClicked

    private boolean validateGame() {
        if (game.getImages().size() > 0) {
            errorLabel.setText("");
            return true;
        } else {
            errorLabel.setText(rm.getTextOfXMLTag("pleaseCreateNewGame"));
            return false;
        }
    }

    private void setCheckboxListener(final JCheckBox checkbox, final int i) {

        checkbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                game.getImages().get(i).setEnabled(checkbox.isSelected());
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel img1Label;
    private javax.swing.JLabel img2Label;
    private javax.swing.JLabel img3Label;
    private javax.swing.JLabel img4Label;
    private javax.swing.JLabel instructionLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel removeSoundLabel;
    private javax.swing.JLabel soundLabel;
    // End of variables declaration//GEN-END:variables
}
