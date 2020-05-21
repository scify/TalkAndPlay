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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.services.GameService;
import org.scify.talkandplay.utils.XMLConfigurationHandler;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class GamesTab extends javax.swing.JPanel {

    private User user;
    private GuiHelper guiHelper;
    private GameService gameService;
    private ConfigurationPanel parent;
    private List<GamePanel> gamePanels;
    private String currentGameType, windSoundPath, errorSoundPath;
    private AudioPlayerComponent audioPlayer;
    private GameType gameType = null;

    public GamesTab(User user, ConfigurationPanel parent) {
        this.user = user;
        this.guiHelper = new GuiHelper();
        this.gameService = new GameService();
        this.parent = parent;
        this.audioPlayer = new AudioPlayerComponent();

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        gamePanels = new ArrayList();
        guiHelper.drawButton(saveButton);
        Font font = new Font(UIConstants.mainFont, Font.BOLD, 16);
        step1Label.setFont(font);
        step2Label.setFont(font);
        step3Label.setFont(font);
        step4Label.setFont(font);
        step2Label.setVisible(false);
        step3Label.setVisible(false);
        step3ExplLabel.setVisible(false);        
        winSoundLabel.setVisible(false);
        errorSoundLabel.setVisible(false);
        step4Label.setVisible(false);
        saveButton.setVisible(false);
        removeWinSoundLavel.setVisible(false);
        removeErrorSoundLavel.setVisible(false);
        winSoundLabel.setHorizontalTextPosition(JLabel.CENTER);
        winSoundLabel.setVerticalTextPosition(JLabel.BOTTOM);
        errorSoundLabel.setHorizontalTextPosition(JLabel.CENTER);
        errorSoundLabel.setVerticalTextPosition(JLabel.BOTTOM);

        gamesPanel2.setLayout(new BoxLayout(gamesPanel2, BoxLayout.Y_AXIS));
        gamesPanel4.setLayout(new BoxLayout(gamesPanel4, BoxLayout.Y_AXIS));
        gamesComboBox.setBorder(new LineBorder(Color.decode(UIConstants.green), 1));
        gamesComboBox.setFont(new Font(UIConstants.green, Font.PLAIN, 12));

        gamesComboBox.addItem("[-- Επίλεξε παιχνίδι --]");
        gamesComboBox.addItem("Ερέθισμα - Αντίδραση");
        gamesComboBox.addItem("Χρονική αλληλουχία");
        gamesComboBox.addItem("Βρες το όμοιο");

        setListeners();
    }

    public void showGamesPerType(String type) {
        //refresh user from configuration file
        XMLConfigurationHandler ch = new XMLConfigurationHandler();
        for (User temp : ch.getUsers()) {
            if (temp.getName().equals(user.getName())) {
                user = temp;
                break;
            }
        }
        gamesPanel4.removeAll();
        gamesPanel2.removeAll();        
        gamePanels.clear();

        for (GameType gt : user.getGameModule().getGameTypes()) {
            if (type.equals(gt.getType())) {
                gameType = gt;
            }
        }

        if (gameType != null) {
            if (gameType.getGames().size() > 0) {
                for (Game game : gameType.getGames()) {
                    GamePanel gamePanel = new GamePanel(game, this);
                    gamesPanel2.add(gamePanel);
                    gamePanels.add(gamePanel);
                }
                step2Label.setVisible(true);
                step3Label.setVisible(true);
                step3ExplLabel.setVisible(true);                
                winSoundLabel.setVisible(true);
                errorSoundLabel.setVisible(true);
                step4Label.setVisible(true);
                saveButton.setVisible(true);

                if (gameType.getWinSound() == null || gameType.getWinSound().isEmpty() || !(new File(gameType.getWinSound()).isFile())) {
                    windSoundPath = null;
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    windSoundPath = gameType.getWinSound();
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    removeWinSoundLavel.setVisible(true);
                }

                if (gameType.getErrorSound() == null || gameType.getErrorSound().isEmpty() || !(new File(gameType.getErrorSound()).isFile())) {
                    errorSoundPath = null;
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    errorSoundPath = gameType.getErrorSound();
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    removeErrorSoundLavel.setVisible(true);
                }
                
                //add new game
                NewGamePanel newGamePanel = new NewGamePanel(this, user, gameType.getType());
                gamesPanel4.add(newGamePanel);
            }
        } else {
            step2Label.setVisible(false);
            step3Label.setVisible(false);
            step3ExplLabel.setVisible(false);            
            winSoundLabel.setVisible(false);
            errorSoundLabel.setVisible(false);
            step4Label.setVisible(false);
            gamesPanel4.setVisible(false);
            saveButton.setVisible(false);
            gamesPanel2.add(new JLabel("Δεν υπάρχουν παιχνίδια σε αυτή την κατηγορία"));
        }

        gamesPanel2.revalidate();
        gamesPanel2.repaint();
        gamesPanel4.revalidate();
        gamesPanel4.repaint();
    }

    private void setListeners() {

        winSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (windSoundPath == null || windSoundPath.isEmpty()) {
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    playMedia(windSoundPath);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (windSoundPath == null || windSoundPath.isEmpty()) {
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
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
                    windSoundPath = chooser.getSelectedFile().getAbsolutePath();
                    winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    gameType.setWinSound(windSoundPath);
                    removeWinSoundLavel.setVisible(true);
                }
            }
        });

        errorSoundLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (errorSoundPath == null || errorSoundPath.isEmpty()) {
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    playMedia(errorSoundPath);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (errorSoundPath == null || errorSoundPath.isEmpty()) {
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                } else {
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
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
                    errorSoundPath = chooser.getSelectedFile().getAbsolutePath();
                    errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/sound-icon.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                    gameType.setErrorSound(errorSoundPath);
                    removeErrorSoundLavel.setVisible(true);
                }
            }
        });

        gamesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if ("Ερέθισμα - Αντίδραση".equals(ie.getItem())) {
                    showGamesPerType("stimulusReactionGame");
                    currentGameType = "stimulusReactionGames";
                } else if ("Χρονική αλληλουχία".equals(ie.getItem())) {
                    showGamesPerType("sequenceGame");
                    currentGameType = "sequenceGames";
                } else if ("Βρες το όμοιο".equals(ie.getItem())) {
                    showGamesPerType("similarityGame");
                    currentGameType = "similarityGames";
                }
            }
        });

        removeWinSoundLavel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                gameType.setWinSound("");
                winSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                removeWinSoundLavel.setVisible(false);
                windSoundPath = null;
            }
        });

        removeErrorSoundLavel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                gameType.setErrorSound("");
                errorSoundLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/add-icon-hover.png")).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
                removeErrorSoundLavel.setVisible(false);
                errorSoundPath = null;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        wrapperPanel = new javax.swing.JPanel();
        step1Label = new javax.swing.JLabel();
        gamesComboBox = new javax.swing.JComboBox();
        step2Label = new javax.swing.JLabel();
        gamesPanel2 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        step3Label = new javax.swing.JLabel();
        step3ExplLabel = new javax.swing.JLabel();
        winSoundLabel = new javax.swing.JLabel();
        errorSoundLabel = new javax.swing.JLabel();
        removeWinSoundLavel = new javax.swing.JLabel();
        removeErrorSoundLavel = new javax.swing.JLabel();
        step4Label = new javax.swing.JLabel();
        gamesPanel4 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        wrapperPanel.setBackground(new java.awt.Color(255, 255, 255));
        wrapperPanel.setForeground(new java.awt.Color(0, 0, 0));

        step1Label.setText("1. Επίλεξε κατηγορία παιχνιδιού");

        gamesComboBox.setBackground(new java.awt.Color(255, 255, 255));

        step2Label.setText("2. Διαχειρίσου τις διαθέσιμες ομάδες εικόνων");

        gamesPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gamesPanel2Layout = new javax.swing.GroupLayout(gamesPanel2);
        gamesPanel2.setLayout(gamesPanel2Layout);
        gamesPanel2Layout.setHorizontalGroup(
            gamesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        gamesPanel2Layout.setVerticalGroup(
            gamesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 172, Short.MAX_VALUE)
        );

        saveButton.setBackground(new java.awt.Color(75, 161, 69));
        saveButton.setFont(saveButton.getFont());
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Αποθήκευση");
        saveButton.setBorder(null);
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });

        step3Label.setText("3. Βάλε ήχους επιβράβευσης και λάθους ");

        step3ExplLabel.setText("(θα χρησιμοποιηθούν προεπιλεγμένοι ήχοι αν δεν ανεβάσεις ήχο)");

        winSoundLabel.setText("επιβράβευση");

        errorSoundLabel.setText("σφάλμα");

        removeWinSoundLavel.setText("Αφαίρεση ήχου");

        removeErrorSoundLavel.setText("Αφαίρεση ήχου");

        step4Label.setText("4. Προσθήκη νέου παιχνιδιού");

        gamesPanel4.setBackground(new java.awt.Color(255, 255, 255));
        gamesPanel4.setToolTipText("");
        gamesPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gamesPanel4.setOpaque(false);
        gamesPanel4.setRequestFocusEnabled(false);

        javax.swing.GroupLayout gamesPanel4Layout = new javax.swing.GroupLayout(gamesPanel4);
        gamesPanel4.setLayout(gamesPanel4Layout);
        gamesPanel4Layout.setHorizontalGroup(
            gamesPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        gamesPanel4Layout.setVerticalGroup(
            gamesPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout wrapperPanelLayout = new javax.swing.GroupLayout(wrapperPanel);
        wrapperPanel.setLayout(wrapperPanelLayout);
        wrapperPanelLayout.setHorizontalGroup(
            wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrapperPanelLayout.createSequentialGroup()
                .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, wrapperPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(step4Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, wrapperPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(step1Label)
                            .addComponent(step2Label)
                            .addComponent(gamesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(step3ExplLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, wrapperPanelLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(winSoundLabel)
                            .addComponent(removeWinSoundLavel))
                        .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(wrapperPanelLayout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(errorSoundLabel))
                            .addGroup(wrapperPanelLayout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(removeErrorSoundLavel)))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(wrapperPanelLayout.createSequentialGroup()
                .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wrapperPanelLayout.createSequentialGroup()
                        .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(wrapperPanelLayout.createSequentialGroup()
                                .addGap(270, 270, 270)
                                .addComponent(saveButton))
                            .addGroup(wrapperPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(step3Label)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(wrapperPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(gamesPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gamesPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        wrapperPanelLayout.setVerticalGroup(
            wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrapperPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(step1Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gamesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(step2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gamesPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(step3Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(step3ExplLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(winSoundLabel)
                    .addComponent(errorSoundLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeWinSoundLavel)
                    .addComponent(removeErrorSoundLavel))
                .addGap(32, 32, 32)
                .addComponent(step4Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gamesPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(wrapperPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked
        for (GamePanel panel : gamePanels) {
            try {
                gameService.updateGame(user.getName(), panel.getGame(), currentGameType);
            } catch (Exception ex) {
                Logger.getLogger(GamesTab.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            gameService.updateGameType(user.getName(), gameType);
        } catch (Exception ex) {
            Logger.getLogger(GamesTab.class.getName()).log(Level.SEVERE, null, ex);
        }
        parent.displayMessage("Οι αλλαγές αποθηκεύτηκαν!");
    }//GEN-LAST:event_saveButtonMouseClicked

    public void playMedia(String path) {
        audioPlayer.mediaPlayer().media().play(path);
    }

    public void stopPlayer() {
        audioPlayer.mediaPlayer().release();
    }    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorSoundLabel;
    private javax.swing.JComboBox gamesComboBox;
    private javax.swing.JPanel gamesPanel2;
    private javax.swing.JPanel gamesPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel removeErrorSoundLavel;
    private javax.swing.JLabel removeWinSoundLavel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel step1Label;
    private javax.swing.JLabel step2Label;
    private javax.swing.JLabel step3ExplLabel;
    private javax.swing.JLabel step3Label;
    private javax.swing.JLabel step4Label;
    private javax.swing.JLabel winSoundLabel;
    private javax.swing.JPanel wrapperPanel;
    // End of variables declaration//GEN-END:variables
}
