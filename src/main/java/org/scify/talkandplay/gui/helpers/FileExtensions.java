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
package org.scify.talkandplay.gui.helpers;

/**
 * File extensions for the vlc media player
 *
 * @author christina
 */
public class FileExtensions {

    public static final FileExtensions instance = new FileExtensions();

    private static final String[] EXTENSIONS_AUDIO = {
            "mp3",
            "aif",
            "aiff",
            "wav"
    };

    private static final String[] EXTENSIONS_VIDEO = {
            "mp4", "m4a", "m4v"
    };

    private FileExtensions() {
    }

    public static FileExtensions getInstance() {
        return instance;
    }

    public static String[] getAudioExtensions() {
        return EXTENSIONS_AUDIO;
    }

    public static String[] getVideoExtensions() {
        return EXTENSIONS_VIDEO;
    }

}
