package org.scify.talkandplay.utils;


import io.sentry.Sentry;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;
import javax.swing.*;

public class AudioPlayer {
    protected static AudioPlayer instance;
    protected Logger logger = Logger.getLogger(AudioPlayer.class);
    protected ResourceManager rm = ResourceManager.getInstance();

    public static AudioPlayer getInstance() {
        if (instance == null)
            instance = new AudioPlayer();
        return instance;
    }

    public MediaPlayer getMediaPlayer(Media media) {
        try {
            MediaPlayer audioPlayer = new MediaPlayer(media);
            return audioPlayer;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, rm.getTextOfXMLTag("audioError"), "Error", JOptionPane.ERROR_MESSAGE);
            logger.error(e);
            Sentry.capture(e.getMessage());
            return null;
        }
    }
}
