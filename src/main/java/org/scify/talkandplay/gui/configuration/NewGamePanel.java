/**
* Copyright 2016 SciFY
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
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
    private List<JCheckBox> imgCheckboxes;
    private List<GameImage> gameImages;
    private ImageIcon addIcon, soundIcon, addIconHover, soundIconHover;

    private Font buttonFont;

    public NewGamePanel(GamesTab parent, User user, String gameType) {
        this.game = new Game();
        this.gameService = new GameService();
        this.parent = parent;
        this.user = user;
        this.gameType = gameType;
        this.imgLabels = new ArrayList();
        this.imgCheckboxes = new ArrayList();
        this.gameImages = new ArrayList();

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {

        addIcon = new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        soundIcon = new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        addIconHover=new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        soundIconHover=new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        
        imgLabels.add(img1Label);
        imgLabels.add(img2Label);
        imgLabels.add(img3Label);
        imgLabels.add(img4Label);

        imgCheckboxes.add(checkBox1);
        imgCheckboxes.add(checkBox2);
        imgCheckboxes.add(checkBox3);
        imgCheckboxes.add(checkBox4);

        for (int i = 0; i < MAX_IMAGES; i++) {
            setImageIcon(imgLabels.get(i), addIcon.getDescription());
            imgCheckboxes.get(i).setEnabled(true);
            imgCheckboxes.get(i).setSelected(true);
            setImageListener(imgLabels.get(i), i);
        }

        buttonFont = new Font(UIConstants.mainFont, Font.BOLD, 12);

        addNewButton.setBackground(Color.white);
        addNewButton.setMargin(new Insets(10, 10, 10, 10));

        soundLabel.setHorizontalTextPosition(JLabel.CENTER);
        soundLabel.setVerticalTextPosition(JLabel.BOTTOM);

        soundLabel.setIcon(addIcon);
        soundLabel.setText("Προσθήκη ήχου");
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
                chooser.setDialogTitle("Διάλεξε εικόνα");
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
                if (game.getWinSound() == null || game.getWinSound().isEmpty()) {
                    soundLabel.setIcon(addIconHover);
                } else {
                    soundLabel.setIcon(soundIconHover);
                    parent.playMedia(game.getWinSound());
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (game.getWinSound() == null || game.getWinSound().isEmpty()) {
                    soundLabel.setIcon(addIcon);
                } else {
                    soundLabel.setIcon(soundIcon);
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle("Διάλεξε ήχο");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Sound Files", "mp3", "wav", "wma", "mid"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    game.setWinSound(chooser.getSelectedFile().getAbsolutePath());
                    soundLabel.setIcon(soundIcon);
                    soundLabel.setText("");
                    removeSoundLabel.setVisible(true);
                }
            }
        });

        removeSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                game.setWinSound("");
                soundLabel.setIcon(addIconHover);
                removeSoundLabel.setVisible(false);
                soundLabel.setText("Προσθήκη ήχου");
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
        checkBox1 = new javax.swing.JCheckBox();
        checkBox2 = new javax.swing.JCheckBox();
        img2Label = new javax.swing.JLabel();
        checkBox3 = new javax.swing.JCheckBox();
        img3Label = new javax.swing.JLabel();
        checkBox4 = new javax.swing.JCheckBox();
        img4Label = new javax.swing.JLabel();
        addNewButton = new javax.swing.JButton();
        soundLabel = new javax.swing.JLabel();
        removeSoundLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(checkBox1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(checkBox2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(checkBox3)
                        .addGap(42, 42, 42)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(checkBox4)
                        .addGap(190, 190, 190)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soundLabel)
                    .addComponent(removeSoundLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(img3Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(img2Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkBox2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(img1Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkBox1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(img4Label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(soundLabel)
                            .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBox4)
                            .addComponent(removeSoundLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(155, Short.MAX_VALUE)
                .addComponent(errorLabel)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(29, Short.MAX_VALUE)))
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
//            try {
//                gameService.updateGameType(user.getName(), gameType);
//            } catch (Exception ex) {
//                Logger.getLogger(GamesTab.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            parent.displayMessage("Οι αλλαγές αποθηκεύτηκαν!");
        }
    }//GEN-LAST:event_addNewButtonMouseClicked

    private boolean validateGame() {
        if (game.getImages().size() > 0) {
            errorLabel.setText("");
            return true; 
        } else {
            errorLabel.setText("Παρακαλώ δημιουργείστε ένα παιχνίδι για να το προσθέσετε");
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
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkBox2;
    private javax.swing.JCheckBox checkBox3;
    private javax.swing.JCheckBox checkBox4;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel img1Label;
    private javax.swing.JLabel img2Label;
    private javax.swing.JLabel img3Label;
    private javax.swing.JLabel img4Label;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel removeSoundLabel;
    private javax.swing.JLabel soundLabel;
    // End of variables declaration//GEN-END:variables
}
