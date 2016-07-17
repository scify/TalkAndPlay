package org.scify.talkandplay.gui.configuration;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.apache.commons.lang3.StringUtils;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.ModuleService;

public class EntertainmentTab extends javax.swing.JPanel {

    private User user;
    private GuiHelper guiHelper;
    private ModuleService moduleService;
    private ConfigurationPanel parent;

    public EntertainmentTab(User user, ConfigurationPanel parent) {
        this.user = user;
        this.guiHelper = new GuiHelper();
        this.moduleService = new ModuleService();
        this.parent = parent;

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

                chooser.setDialogTitle("Διάλεξε φάκελο");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    System.out.println(chooser.getSelectedFile().getAbsolutePath());
                    musicPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        videoPathTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle("Διάλεξε φάκελο");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    System.out.println(chooser.getSelectedFile().getAbsolutePath());
                    videoPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        musicPathTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if ("Φάκελος μουσικής".equals(musicPathTextField.getText())) {
                    musicPathTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (musicPathTextField.getText().isEmpty()) {
                    musicPathTextField.setText("Φάκελος μουσικής");
                } else if (!musicPathTextField.getText().endsWith("/")) {
                    musicPathTextField.setText(musicPathTextField.getText() + "/");
                }
            }
        });

        videoPathTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if ("Φάκελος video".equals(videoPathTextField.getText())) {
                    videoPathTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (videoPathTextField.getText().isEmpty()) {
                    videoPathTextField.setText("Φάκελος video");
                } else if (!videoPathTextField.getText().endsWith("/")) {
                    videoPathTextField.setText(videoPathTextField.getText() + "/");
                }
            }
        });

        songSumTextField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe) {
                if ("Πλήθος".equals(songSumTextField.getText())) {
                    songSumTextField.setText("");
                }
            }

            public void focusLost(FocusEvent fe) {
                if (songSumTextField.getText().isEmpty()) {
                    songSumTextField.setText("Πλήθος");
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

        step1Label.setText("1. Επίλεξε φάκελο μουσικής");

        step2Label.setText("2. Επίλεξε φάκελο video");

        step3Label.setText("3. Όρισε πλήθος τραγουδιών και video ανά σελίδα");

        soundLabel.setText("4. Όρισε ένταση ήχου (ignore this)");

        musicPathTextField.setText("Φάκελος μουσικής");

        videoPathTextField.setText("Φάκελος video");

        songSumTextField.setText("10");

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

        errorLabel.setForeground(new java.awt.Color(153, 0, 0));
        errorLabel.setText("error");

        backButton.setBackground(new java.awt.Color(75, 161, 69));
        backButton.setFont(backButton.getFont());
        backButton.setForeground(new java.awt.Color(255, 255, 255));
        backButton.setText("Πίσω");
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

        if (musicPathTextField.getText().isEmpty() || "Φάκελος μουσικής".equals(musicPathTextField.getText()) || !(new File(musicPathTextField.getText()).isDirectory())) {
            errorLabel.setText("Ο φάκελος μουσικής πρέπει να οριστεί σωστά.");
            errorLabel.setVisible(true);
        } else if (videoPathTextField.getText().isEmpty() || "Φάκελος video".equals(videoPathTextField.getText()) || !(new File(videoPathTextField.getText()).isDirectory())) {
            errorLabel.setText("Ο φάκελος video πρέπει να οριστεί σωστά.");
            errorLabel.setVisible(true);
        } else if (songSumTextField.getText().isEmpty() || !StringUtils.isNumeric(songSumTextField.getText())) {
            errorLabel.setText("Το πλήθος τραγουδιών πρέπει οριστεί σωστά");
            errorLabel.setVisible(true);
        } else if (Integer.parseInt(songSumTextField.getText()) < 0 || Integer.parseInt(songSumTextField.getText()) > 10) {
            errorLabel.setText("Το πλήθος τραγουδιών πρέπει να είναι μεταξύ 0 και 10");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);

            user.getEntertainmentModule().getMusicModule().setFolderPath(musicPathTextField.getText());
            user.getEntertainmentModule().getMusicModule().setPlaylistSize(Integer.parseInt(songSumTextField.getText()));
            user.getEntertainmentModule().getVideoModule().setFolderPath(videoPathTextField.getText());

            try {
                moduleService.update(user);
                parent.displayMessage("Οι ρυθμίσεις αποθηκεύτηκαν!");
            } catch (Exception ex) {
                Logger.getLogger(EntertainmentTab.class.getName()).log(Level.SEVERE, null, ex);
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
