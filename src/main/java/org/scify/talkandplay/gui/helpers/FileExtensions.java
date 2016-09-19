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
        "3ga",
        "669",
        "a52",
        "aac",
        "ac3",
        "adt",
        "adts",
        "aif",
        "aifc",
        "aiff",
        "amb",
        "amr",
        "aob",
        "ape",
        "au",
        "awb",
        "caf",
        "dts",
        "flac",
        "it",
        "kar",
        "m4a",
        "m4b",
        "m4p",
        "m5p",
        "mid",
        "mka",
        "mlp",
        "mod",
        "mpa",
        "mp1",
        "mp2",
        "mp3",
        "mpc",
        "mpga",
        "mus",
        "oga",
        "ogg",
        "oma",
        "opus",
        "qcp",
        "ra",
        "rmi",
        "s3m",
        "sid",
        "spx",
        "tak",
        "thd",
        "tta",
        "voc",
        "vqf",
        "w64",
        "wav",
        "wma",
        "wv",
        "xa",
        "xm"
    };

    private static final String[] EXTENSIONS_VIDEO = {
        "3g2",
        "3gp",
        "3gp2",
        "3gpp",
        "amv",
        "asf",
        "avi",
        "bik",
        "bin",
        "divx",
        "drc",
        "dv",
        "evo",
        "f4v",
        "flv",
        "gvi",
        "gxf",
        "iso",
        "m1v",
        "m2v",
        "m2t",
        "m2ts",
        "m4v",
        "mkv",
        "mov",
        "mp2",
        "mp2v",
        "mp4",
        "mp4v",
        "mpe",
        "mpeg",
        "mpeg1",
        "mpeg2",
        "mpeg4",
        "mpg",
        "mpv2",
        "mts",
        "mtv",
        "mxf",
        "mxg",
        "nsv",
        "nuv",
        "ogg",
        "ogm",
        "ogv",
        "ogx",
        "ps",
        "rec",
        "rm",
        "rmvb",
        "rpl",
        "thp",
        "tod",
        "ts",
        "tts",
        "txd",
        "vob",
        "vro",
        "webm",
        "wm",
        "wmv",
        "wtv",
        "xesc"
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
