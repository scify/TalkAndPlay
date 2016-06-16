/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.scify.talkandplay.gui.helpers.Time;
import org.scify.talkandplay.models.User;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;

/**
 *
 * @author christina
 */
public class VideoFrame extends javax.swing.JFrame {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private String currentFile;
    private List<JLabel> files;
    private User user;

    public VideoFrame(User user, String currentFile) {
        this.mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        this.currentFile = currentFile;
        this.user = user;
        setTitle("Video Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        initMediaPlayer();
        initCustomComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        videoPanel = new javax.swing.JPanel();
        controlsPanel = new javax.swing.JPanel();
        durationSlider = new javax.swing.JSlider();
        startLabel = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        previousButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        videoPanel.setLayout(new java.awt.BorderLayout());

        durationSlider.setValue(0);

        startLabel.setText("00:00:00");

        endLabel.setText("00:00:00");

        previousButton.setText("<<");
        previousButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                previousButtonMouseClicked(evt);
            }
        });

        playButton.setText("Play");
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playButtonMouseClicked(evt);
            }
        });

        nextButton.setText(">>");
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextButtonMouseClicked(evt);
            }
        });

        backButton.setText("Back");
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(startLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(durationSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endLabel))
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(previousButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backButton)))
                .addContainerGap())
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(endLabel)
                    .addComponent(startLabel)
                    .addComponent(durationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousButton)
                    .addComponent(playButton)
                    .addComponent(nextButton)
                    .addComponent(backButton))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(videoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(videoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseClicked
        if ("Play".equals(playButton.getText())) {
            playButton.setText("Pause");
        } else {
            playButton.setText("Play");
        }
        mediaPlayerComponent.getMediaPlayer().pause();
    }//GEN-LAST:event_playButtonMouseClicked

    private void backButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMouseClicked
        dispose();
    }//GEN-LAST:event_backButtonMouseClicked

    private void nextButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMouseClicked
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getText().equals(currentFile)) {
                if (i == files.size() - 1) {
                    currentFile = files.get(0).getText();
                } else {
                    currentFile = files.get(i + 1).getText();
                }
                break;
            }
        }
        playMedia(currentFile);
    }//GEN-LAST:event_nextButtonMouseClicked

    private void previousButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMouseClicked
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getText().equals(currentFile)) {
                if (i == 0) {
                    currentFile = files.get(files.size() - 1).getText();
                } else {
                    currentFile = files.get(i - 1).getText();
                }
                break;
            }
        }

        playMedia(currentFile);
    }//GEN-LAST:event_previousButtonMouseClicked

    private void initMediaPlayer() {
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                mediaPlayerComponent.getMediaPlayer().mute(false);
                mediaPlayerComponent.getMediaPlayer().setVolume(100);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {

            }

            @Override
            public void positionChanged(MediaPlayer mp, float f) {
                int iPos = (int) (f * 100.0);
                durationSlider.setValue(iPos);
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, final long newTime) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        startLabel.setText(String.format("%s", Time.formatTime(newTime)));
                    }
                });
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.getMediaPlayer().stop();
                mediaPlayerComponent.getMediaPlayer().stop();
                e.getWindow().dispose();
            }
        });
    }

    private void initCustomComponents() {
        videoPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mediaPlayerComponent.getMediaPlayer().setFullScreenStrategy(
                new DefaultAdaptiveRuntimeFullScreenStrategy(this)
        );
        mediaPlayerComponent.getMediaPlayer().setFullScreen(true);
        // playMedia(currentFile);

    }

    public void playMedia(String file) {
        currentFile = file;
        mediaPlayerComponent.getMediaPlayer().prepareMedia(getFilePath(file));
        mediaPlayerComponent.getMediaPlayer().parseMedia();

        int secs = (int) (mediaPlayerComponent.getMediaPlayer().getMediaMeta().getLength() / 1000) % 60;
        int mins = (int) ((mediaPlayerComponent.getMediaPlayer().getMediaMeta().getLength() / (1000 * 60)) % 60);
        int hrs = (int) ((mediaPlayerComponent.getMediaPlayer().getMediaMeta().getLength() / (1000 * 60 * 60)) % 24);

        endLabel.setText(Time.getTime(hrs, mins, secs));

        playButton.setText("Pause");

        mediaPlayerComponent.getMediaPlayer().playMedia(getFilePath(file));
        setVisible(true);
    }

    public String getFilePath(String fileName) {
        return user.getEntertainmentModule().getVideoModule().getFolderPath() + File.separator + fileName;
    }

    public void setFiles(List<JLabel> files) {
        this.files = files;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JSlider durationSlider;
    private javax.swing.JLabel endLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel startLabel;
    private javax.swing.JPanel videoPanel;
    // End of variables declaration//GEN-END:variables
}
