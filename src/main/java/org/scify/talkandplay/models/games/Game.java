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
package org.scify.talkandplay.models.games;

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christina
 */
public class Game {

    private String name;
    private ImageResource imageResource;
    private SoundResource soundResource;
    private SoundResource winSoundResource;
    private SoundResource errorSoundResource;
    private boolean enabled;
    private List<GameImage> images;
    private List<GameImage> enabledImages;
    protected ResourceManager rm;

    public Game() {
        this.rm = ResourceManager.getInstance();
        this.images = new ArrayList();
    }

    public Game(String name, boolean enabled) {
        this.rm = ResourceManager.getInstance();
        this.name = name;
        this.enabled = enabled;
        this.images = new ArrayList();
        this.enabledImages = new ArrayList();
    }

    public Game getCopy() {
        Game game = new Game(name, enabled);
        if (imageResource == null)
            game.imageResource = null;
        else
            game.imageResource = imageResource.getCopy();
        if (soundResource == null)
            game.soundResource = null;
        else
            game.soundResource = soundResource.getCopy();
        if (winSoundResource == null)
            game.winSoundResource = null;
        else
            game.winSoundResource = winSoundResource.getCopy();
        if (errorSoundResource == null)
            game.errorSoundResource = null;
        else
            game.errorSoundResource = errorSoundResource.getCopy();

        for (GameImage image: images) {
            game.images.add(image.getCopy());
        }

        for (GameImage image: enabledImages) {
            game.enabledImages.add(image.getCopy());
        }
        return game;
    }

    public String getName() {
        return rm.decodeTextIfRequired(name);
    }

    public String getNameUnmodified() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageResource getImage() {
        return imageResource;
    }

    public void setImage(ImageResource imageResource) {
        this.imageResource = imageResource;
    }

    public SoundResource getSound() {
        return soundResource;
    }

    public void setSound(SoundResource soundResource) {
        this.soundResource = soundResource;
    }

    public SoundResource getWinSound() {
        return winSoundResource;
    }

    public void setWinSound(SoundResource soundResource) {
        this.winSoundResource = soundResource;
    }

    public SoundResource getErrorSound() {
        return errorSoundResource;
    }

    public void setErrorSound(SoundResource soundResource) {
        this.errorSoundResource = soundResource;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<GameImage> getEnabledImages() {
        return enabledImages;
    }

    public void setEnabledImages() {
        for (GameImage image : images) {
            if (image.isEnabled()) {
                enabledImages.add(image);
            }
        }
    }

    public List<GameImage> getImages() {
        return images;
    }

    public void setImages(List<GameImage> images) {
        this.images = images;
    }

}
