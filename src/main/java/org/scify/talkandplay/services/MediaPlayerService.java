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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.services;

import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
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
