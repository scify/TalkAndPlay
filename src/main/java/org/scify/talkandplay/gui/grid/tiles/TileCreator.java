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
import java.net.URL;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

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
    private AudioPlayerComponent audioPlayer;
    private static String DEFAULT_SOUND;
    protected final ResourceManager rm;

    public TileCreator(User user, int rows, int columns) {
        rm = ResourceManager.getInstance();
        DEFAULT_SOUND = new File("resources/sounds/default.mp3").getAbsolutePath();
        //   DEFAULT_SOUND = getClass().getResource("/org/scify/talkandplay/resources/sounds/cat.mp3").getPath();
        this.user = user;
        this.sensorService = new SensorService(user);
        this.guiHelper = new GuiHelper(user);
        this.audioPlayer = new AudioPlayerComponent();
        initAudioPlayer();
    }

    /**
     * Initialize the audio player and set the action that will be performed
     * after the player finishes
     *
     */
    private void initAudioPlayer() {

        audioPlayer.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                audioPlayer.mediaPlayer().audio().setMute(false);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                tileAction.audioFinished();
            }
        });
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
        ImageResource im = new ImageResource("empty_pixel.png", ResourceType.FROM_JAR);
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
        audioPlayer.mediaPlayer().release();
    }

    /**
     * Play a sound on demand
     *
     * @param sound
     */
    public void playAudio(String sound) {
        if (sound != null && !sound.isEmpty()) {
            audioPlayer.mediaPlayer().media().play(sound);
        } else {
            audioPlayer.mediaPlayer().media().play(DEFAULT_SOUND);
        }
    }

    public void playAudio(String sound, TileAction tileAction) {
        this.tileAction = tileAction;
        if (sound != null && !sound.isEmpty()) {
             audioPlayer.mediaPlayer().media().play(sound);
        } else {
            audioPlayer.mediaPlayer().media().play(DEFAULT_SOUND);
        }
    }
}
