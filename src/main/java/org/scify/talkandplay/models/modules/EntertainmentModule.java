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
package org.scify.talkandplay.models.modules;

public class EntertainmentModule extends Module {

    private MusicModule musicModule;
    private VideoModule videoModule;

    public EntertainmentModule() {
        super();
    }

    public MusicModule getMusicModule() {
        return musicModule;
    }

    public void setMusicModule(MusicModule musicModule) {
        this.musicModule = musicModule;
    }

    public VideoModule getVideoModule() {
        return videoModule;
    }

    public void setVideoModule(VideoModule videoModule) {
        this.videoModule = videoModule;
    }

}
