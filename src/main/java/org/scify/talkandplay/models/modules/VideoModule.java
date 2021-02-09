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

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.SoundResource;

public class VideoModule extends Module {

    private String folderPath;
    private int playlistSize;

    public VideoModule() {
        super();
    }

    public VideoModule getCopy() {
        VideoModule videoModule = new VideoModule();
        videoModule.setFolderPath(folderPath);
        videoModule.playlistSize = playlistSize;
        videoModule.setName(this.getName());
        ImageResource imageResource = getImageResource();
        if (imageResource == null)
            videoModule.setImage(null);
        else
            videoModule.setImage(imageResource.getCopy());
        SoundResource soundResource = getSoundResource();
        if (soundResource == null)
            videoModule.setSound(null);
        else
            videoModule.setSound(soundResource.getCopy());
        videoModule.setEnabled(this.isEnabled());
        return videoModule;
    }

    public boolean isAltered(VideoModule module) {
        if (super.isAltered(module))
            return true;
        if (!folderPath.equals(module.folderPath))
            return true;
        if (playlistSize != module.playlistSize)
            return true;
        return false;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getPlaylistSize() {
        return playlistSize;
    }

    public void setPlaylistSize(int playlistSize) {
        this.playlistSize = playlistSize;
    }
}
