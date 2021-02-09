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
package org.scify.talkandplay.models.modules;

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.SoundResource;

public class MusicModule extends Module {

    private String folderPath;
    private int playlistSize;

    public MusicModule() {
        super();
    }

    public MusicModule getCopy() {
        MusicModule musicModule = new MusicModule();
        musicModule.setFolderPath(folderPath);
        musicModule.playlistSize = playlistSize;
        musicModule.setName(getName());
        ImageResource imageResource = getImageResource();
        if (imageResource == null)
            musicModule.setImage(null);
        else
            musicModule.setImage(imageResource.getCopy());
        SoundResource soundResource = getSoundResource();
        if (soundResource == null)
            musicModule.setSound(null);
        else
            musicModule.setSound(soundResource.getCopy());
        musicModule.setEnabled(this.isEnabled());
        return musicModule;
    }

    public boolean isAltered(MusicModule module) {
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
