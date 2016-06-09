/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.jthinkfreedom.talkandplay.services;

import com.sun.jna.NativeLibrary;
import javax.swing.JFrame;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 *
 * @author xrist
 */
public class MediaPlayerService {

    private static final String NATIVE_LIBRARY_SEARCH_PATH = "/home/vlc";

    private final AudioMediaPlayerComponent audioPlayer;

    public MediaPlayerService() {
        initPlayer();

        audioPlayer = new AudioMediaPlayerComponent();
       /* audioPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                System.out.println("stopped");
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("finished");
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("error");
            }
        });*/
    }

    public void playSound(String path) {
        audioPlayer.getMediaPlayer().playMedia(path);
    }

    private void initPlayer() {
        boolean found = new NativeDiscovery().discover();
        if (!found) {
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }
    }
}
