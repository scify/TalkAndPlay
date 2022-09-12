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
package org.scify.talkandplay.gui.grid.entertainment;

import java.awt.Font;
import java.io.File;
import javax.swing.JPanel;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.scify.talkandplay.gui.helpers.Time;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.utils.AudioPlayer;

/**
 *
 * @author christina
 */
public class MediaPlayerPanel extends javax.swing.JPanel {

    private MediaPlayer mediaPlayer;
    private JPanel parent;

    public MediaPlayerPanel(JPanel parent) {
        this.parent = parent;
        this.mediaPlayer = null;
        initComponents();
        initAudioPlayer();
        initCustomComponents();
    }

    protected void startTimer() {
        Thread timer = new Thread(() -> {
            while(mediaPlayer != null) {
                try {
                    // running "long" operation not on UI thread
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
                Platform.runLater(()-> {
                    if (mediaPlayer != null) {
                        double length = mediaPlayer.getCurrentTime().toMillis();
                        int secs = (int) (length / 1000) % 60;
                        int mins = (int) ((length / (1000 * 60)) % 60);
                        int hrs = (int) ((length / (1000 * 60 * 60)) % 24);
                        startLabel.setText(Time.getTime(hrs, mins, secs));
                        int sliderValue = (int) ((length / mediaPlayer.getTotalDuration().toMillis()) * 100.0);
                        mediaSlider.setValue(sliderValue);
                    }
                });
            }
        });
        timer.start();
    }

    private void initAudioPlayer() {
        mediaSlider.setEnabled(false);
    }

    private void initCustomComponents() {

        startLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        endLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
    }

    public void playMedia(String path, boolean isVideo) {
        mediaSlider.setValue(0);

        Media media = new Media(new File(path).toURI().toString());

        mediaPlayer = AudioPlayer.getInstance().getMediaPlayer(media);
        if (mediaPlayer != null) {
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    double length = mediaPlayer.getMedia().getDuration().toMillis();
                    int secs = (int) (length / 1000) % 60;
                    int mins = (int) ((length / (1000 * 60)) % 60);
                    int hrs = (int) ((length / (1000 * 60 * 60)) % 24);
                    endLabel.setText(Time.getTime(hrs, mins, secs));
                }
            });
            mediaPlayer.play();
            if (!isVideo)
                startTimer();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mediaPlayerPanel = new javax.swing.JPanel();
        mediaSlider = new javax.swing.JSlider();
        startLabel = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();

        mediaPlayerPanel.setBackground(new java.awt.Color(255, 255, 255));

        mediaSlider.setToolTipText("");
        mediaSlider.setValue(0);

        startLabel.setText("00:00:00");

        endLabel.setText("00:00:00");

        javax.swing.GroupLayout mediaPlayerPanelLayout = new javax.swing.GroupLayout(mediaPlayerPanel);
        mediaPlayerPanel.setLayout(mediaPlayerPanelLayout);
        mediaPlayerPanelLayout.setHorizontalGroup(
            mediaPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mediaPlayerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mediaSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endLabel)
                .addContainerGap())
        );
        mediaPlayerPanelLayout.setVerticalGroup(
            mediaPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mediaPlayerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mediaPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mediaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startLabel)
                    .addComponent(endLabel))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mediaPlayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mediaPlayerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public boolean isPlaying() {
        if (mediaPlayer == null)
            return false;
        else {
            return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel endLabel;
    private javax.swing.JPanel mediaPlayerPanel;
    private javax.swing.JSlider mediaSlider;
    private javax.swing.JLabel startLabel;
    // End of variables declaration//GEN-END:variables
}
