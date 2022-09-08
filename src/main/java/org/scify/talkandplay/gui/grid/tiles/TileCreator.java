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
package org.scify.talkandplay.gui.grid.tiles;

import java.io.File;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Creates the panel that holds a name and an image. Adds the mouse and keyboard
 * listeners and plays a sound if it is set.
 *
 * @author christina
 */
public class TileCreator {

    private User user;
    private TileAction tileAction;
    private SensorService sensorService;
    private GuiHelper guiHelper;
    private MediaPlayer audioPlayer;
    private static String DEFAULT_SOUND;
    protected final ResourceManager rm;
    protected Media defaultMedia;

    public TileCreator(User user, int rows, int columns) {
        rm = ResourceManager.getInstance();
        DEFAULT_SOUND = rm.getSound("sounds/default.mp3", ResourceType.BUNDLE).getAbsolutePath();
        defaultMedia = new Media(new File(DEFAULT_SOUND).toURI().toString());
        this.user = user;
        this.sensorService = new SensorService(user);
        this.guiHelper = new GuiHelper(user);
        audioPlayer = null;
    }



    /**
     * Draw the panel with its name and image and add the mouse and key
     * listeners
     *
     * @param name
     * @param image
     * @param sound
     * @param tileAction
     * @return JPanel panel
     */
    public JPanel create(String name, ImageResource image, SoundResource sound, TileAction tileAction) {
        JPanel panel = guiHelper.createImagePanel(image, name);
        addListeners(panel, sound, tileAction);
        return panel;
    }

    public JPanel create(String name, ImageResource image, SoundResource sound) {
        JPanel panel = guiHelper.createImagePanel(image, name);
        return panel;
    }

    public JPanel createEmpty() {
        ImageResource im = new ImageResource("empty_pixel.png", ResourceType.JAR);
        JPanel panel = guiHelper.createImagePanel(im, "");
        return panel;
    }

    private void addListeners(JPanel panel, final SoundResource sound, final TileAction tileAction) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    act(sound, tileAction);
                }
            }
        });

        panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    act(sound, tileAction);
                }
            }
        });
    }

    /**
     * The action that will be performed after the right key is pressed
     *
     * @param tileAction
     */
    private void act(SoundResource sound, TileAction tileAction) {
        tileAction.act();
        this.tileAction = tileAction;
        if (!this.tileAction.mute() && user.getConfiguration().hasSound()) {
            playAudio(sound.getSound().getAbsolutePath());
        } else {
            tileAction.audioFinished();
        }
    }

    /**
     * Release the media player resources
     */
    public void freePlayerResources() {
        if (audioPlayer != null)
            audioPlayer.dispose();
    }

    /**
     * Play a sound on demand
     *
     * @param sound
     */
    public void playAudio(String sound) {
        Media media = defaultMedia;
        if (sound != null && !sound.isEmpty()) {
            media = new Media(new File(sound).toURI().toString());
        }
        audioPlayer = AudioPlayer.getInstance().getMediaPlayer(media);
        if (audioPlayer != null) {
            audioPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    audioPlayer.dispose();
                    tileAction.audioFinished();
                }
            });
            audioPlayer.play();
        } else {
            tileAction.audioFinished();
        }
    }

    public void playAudio(String sound, TileAction tileAction) {
        this.tileAction = tileAction;
        playAudio(sound);
    }
}
