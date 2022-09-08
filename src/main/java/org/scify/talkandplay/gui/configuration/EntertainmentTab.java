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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

import io.sentry.Sentry;
import org.apache.commons.lang3.StringUtils;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.ModuleService;
import org.scify.talkandplay.utils.ResourceManager;

public class EntertainmentTab extends javax.swing.JPanel {

    private User user;
    private GuiHelper guiHelper;
    private ModuleService moduleService;
    private ConfigurationPanel parent;
    protected ResourceManager rm;

    public EntertainmentTab(User user, ConfigurationPanel parent) {
        this.user = user;
        this.guiHelper = new GuiHelper();
        this.moduleService = new ModuleService();
        this.parent = parent;
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {

        guiHelper.setCustomTextField(musicPathTextField);
        guiHelper.setCustomTextField(songSumTextField);
        guiHelper.setCustomTextField(videoPathTextField);

        guiHelper.setStepLabelFont(step1Label);
        guiHelper.setStepLabelFont(step2Label);
        guiHelper.setStepLabelFont(step3Label);

        guiHelper.drawButton(saveButton);
        guiHelper.drawButton(backButton);
        soundLabel.setVisible(false);
        errorLabel.setVisible(false);

        if (user.getEntertainmentModule().getMusicModule().getFolderPath() != null && !user.getEntertainmentModule().getMusicModule().getFolderPath().isEmpty()) {
            musicPathTextField.setText(user.getEntertainmentModule().getMusicModule().getFolderPath());
        }

        if (user.getEntertainmentModule().getVideoModule().getFolderPath() != null && !user.getEntertainmentModule().getVideoModule().getFolderPath().isEmpty()) {
            videoPathTextField.setText(user.getEntertainmentModule().getVideoModule().getFolderPath());
        }

        songSumTextField.setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

        setListeners();
    }

    private void setListeners() {

        musicPathTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle(rm.getTextOfXMLTag("chooseDirectory"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    musicPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        videoPathTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle(rm.getTextOfXMLTag("chooseDirectory"));
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    videoPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        musicPathTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (rm.getTextOfXMLTag("musicDirectory").equals(musicPathTextField.getText())) {
                    musicPathTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (musicPathTextField.getText().isEmpty()) {
                    musicPathTextField.setText(rm.getTextOfXMLTag("musicDirectory"));
                } else if (!musicPathTextField.getText().endsWith("/")) {
                    musicPathTextField.setText(musicPathTextField.getText() + "/");
                }
            }
        });

        videoPathTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (rm.getTextOfXMLTag("videoDirectory").equals(videoPathTextField.getText())) {
                    videoPathTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (videoPathTextField.getText().isEmpty()) {
                    videoPathTextField.setText(rm.getTextOfXMLTag("videoDirectory"));
                } else if (!videoPathTextField.getText().endsWith("/")) {
                    videoPathTextField.setText(videoPathTextField.getText() + "/");
                }
            }
        });

        songSumTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if (rm.getTextOfXMLTag("amount").equals(songSumTextField.getText())) {
                    songSumTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (songSumTextField.getText().isEmpty()) {
                    songSumTextField.setText(rm.getTextOfXMLTag("amount"));
                }
            }
        });

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                parent.goBack();
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

        step1Label = new javax.swing.JLabel();
        step2Label = new javax.swing.JLabel();
        step3Label = new javax.swing.JLabel();
        soundLabel = new javax.swing.JLabel();
        musicPathTextField = new javax.swing.JTextField();
        videoPathTextField = new javax.swing.JTextField();
        songSumTextField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        step1Label.setText("1. " + rm.getTextOfXMLTag("chooseMusicDirectory"));

        step2Label.setText("2. " + rm.getTextOfXMLTag("chooseVideoDirectory"));

        step3Label.setText("3. " + rm.getTextOfXMLTag("defineAmountOfItemsPerPage"));

        //soundLabel.setText("4. " + Όρισε ένταση ήχου (ignore this)");

        musicPathTextField.setText(rm.getTextOfXMLTag("musicDirectory"));

        videoPathTextField.setText(rm.getTextOfXMLTag("videoDirectory"));

        songSumTextField.setText("10");

        saveButton.setBackground(new java.awt.Color(75, 161, 69));
        saveButton.setFont(saveButton.getFont());
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText(rm.getTextOfXMLTag("saveButton"));
        saveButton.setBorder(null);
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });

        errorLabel.setForeground(new java.awt.Color(153, 0, 0));
        errorLabel.setText("error");

        backButton.setBackground(new java.awt.Color(75, 161, 69));
        backButton.setFont(backButton.getFont());
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText(rm.getTextOfXMLTag("backButton"));
        backButton.setBorder(null);
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 454, Short.MAX_VALUE)
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(backButton)
                                .addGap(14, 14, 14))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(step1Label)
                                        .addComponent(musicPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(step2Label)
                                        .addComponent(videoPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(step3Label)
                                        .addComponent(songSumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(soundLabel)
                                        .addComponent(errorLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(step1Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(musicPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(step2Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(videoPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(step3Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(songSumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(soundLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(errorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveButton)
                                        .addComponent(backButton))
                                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveButtonMouseClicked

        if (musicPathTextField.getText().isEmpty() || rm.getTextOfXMLTag("musicDirectory").equals(musicPathTextField.getText()) || !(new File(musicPathTextField.getText()).isDirectory())) {
            errorLabel.setText(rm.getTextOfXMLTag("errorMusicDirectory"));
            errorLabel.setVisible(true);
        } else if (videoPathTextField.getText().isEmpty() || rm.getTextOfXMLTag("videoDirectory").equals(videoPathTextField.getText()) || !(new File(videoPathTextField.getText()).isDirectory())) {
            errorLabel.setText(rm.getTextOfXMLTag("errorVideoDirectory"));
            errorLabel.setVisible(true);
        } else if (songSumTextField.getText().isEmpty() || !StringUtils.isNumeric(songSumTextField.getText())) {
            errorLabel.setText(rm.getTextOfXMLTag("errorAmountOfSongs"));
            errorLabel.setVisible(true);
        } else if (Integer.parseInt(songSumTextField.getText()) < 6 || Integer.parseInt(songSumTextField.getText()) > 10) {
            errorLabel.setText(rm.getTextOfXMLTag("errorAmountOfSongs2"));
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);

            user.getEntertainmentModule().getMusicModule().setFolderPath(musicPathTextField.getText());
            user.getEntertainmentModule().getMusicModule().setPlaylistSize(Integer.parseInt(songSumTextField.getText()));
            user.getEntertainmentModule().getVideoModule().setFolderPath(videoPathTextField.getText());
            user.getEntertainmentModule().getVideoModule().setPlaylistSize(Integer.parseInt(songSumTextField.getText()));

            try {
                moduleService.update(user);
                parent.displayMessage(rm.getTextOfXMLTag("changesSaved"));
            } catch (Exception ex) {
                Logger.getLogger(EntertainmentTab.class.getName()).log(Level.SEVERE, null, ex);
                Sentry.capture(ex.getMessage());
            }
        }
    }//GEN-LAST:event_saveButtonMouseClicked

    private void backButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_backButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JTextField musicPathTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField songSumTextField;
    private javax.swing.JLabel soundLabel;
    private javax.swing.JLabel step1Label;
    private javax.swing.JLabel step2Label;
    private javax.swing.JLabel step3Label;
    private javax.swing.JTextField videoPathTextField;
    // End of variables declaration//GEN-END:variables
}
