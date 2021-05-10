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
package org.scify.talkandplay.gui.grid.entertainment;

import javafx.scene.media.MediaPlayer;
import org.scify.talkandplay.gui.grid.BaseMediaPanel;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.selectors.Selector;
import org.scify.talkandplay.gui.helpers.FileExtensions;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ResourceType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VideoPanel extends BaseMediaPanel {

    private JPanel playPanel;
    private VideoFrame videoFrame;

    public VideoPanel(User user, GridFrame parent) {
        super(user, parent,
                user.getEntertainmentModule().getVideoModule().getFolderPath(),
                FileExtensions.getVideoExtensions());
        filesPanel = new FilesPanel(user, files, this);

        initComponents();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 517, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 353, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {

        initLayout();
        boolean isEmpty = isEmpty(user.getEntertainmentModule().getVideoModule().getFolderPath());

        if (isEmpty) {
            drawEmpty();
        } else {

            add(filesPanel, c);
            /*mediaPlayerPanel.getAudioPlayer().mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                @Override
                public void finished(MediaPlayer mediaPlayer) {
                    setPlayButton();
                }

                @Override
                public void playing(MediaPlayer mediaPlayer) {
                    setPauseButton();
                }

                @Override
                public void paused(MediaPlayer mediaPlayer) {
                    setPlayButton();
                }
            });*/
        }

        revalidate();
        repaint();
        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();

        if (isEmpty) {
            selector.setList(controlsList);
            selector.start();
        } else {
            selector.setList(filesPanel.getPanelList());
            selector.start();
        }
    }

    public void playFile(String fileName) {
        selector.cancel();
        videoFrame = new VideoFrame(user, null, this, filesPanel);
        videoFrame.playMedia(getFilePath(fileName));
    }

    public Selector getSelector() {
        return selector;
    }

    public List<JPanel> getControlsList() {
        return controlsList;
    }

    public String getFilePath(String fileName) {
        return user.getEntertainmentModule().getVideoModule().getFolderPath() + fileName;
    }

    private void setPlayButton() {
        ((JLabel) playPanel.getComponent(0)).setText(rm.getTextOfXMLTag("playControl"));
        ((JLabel) playPanel.getComponent(1)).setIcon(new ImageIcon(rm.getImage("play-button.png", ResourceType.JAR).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

    }

    private void setPauseButton() {
        ((JLabel) playPanel.getComponent(0)).setText(rm.getTextOfXMLTag("pauseControl"));
        ((JLabel) playPanel.getComponent(1)).setIcon(new ImageIcon(rm.getImage("pause-button.png", ResourceType.JAR).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));

    }

    /*public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
        return this.mediaPlayerComponent;
    }*/

    public MediaPlayerPanel getMediaPlayerPanel() {
        return this.mediaPlayerPanel;
    }

    public void disposeMediaPlayer() {
        MediaPlayer mediaPlayer = this.mediaPlayerPanel.getMediaPlayer();
        if (mediaPlayer != null) {
            this.mediaPlayerPanel.stop();
        }
    }
}
