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
package org.scify.talkandplay.models.games;

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.SoundResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author christina
 */
public class Game {

    protected String name;
    protected ImageResource imageResource;
    protected SoundResource soundResource;
    protected SoundResource winSoundResource;
    protected SoundResource errorSoundResource;
    protected boolean enabled;
    protected List<GameImage> images;
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
    }

    public Game(Game game) {
        rm = ResourceManager.getInstance();
        name = game.name;
        imageResource = null;
        if (game.imageResource != null)
            imageResource = new ImageResource(game.imageResource);

        soundResource = null;
        if (game.soundResource != null)
            soundResource = new SoundResource(game.soundResource);

        winSoundResource = null;
        if (game.winSoundResource != null)
            winSoundResource = new SoundResource(game.winSoundResource);

        errorSoundResource = null;
        if (game.errorSoundResource != null)
            errorSoundResource = new SoundResource(game.errorSoundResource);

        enabled = game.enabled;
        images = new ArrayList<>();
        for (GameImage image : game.images) {
            images.add(new GameImage(image));
        }
    }

    public boolean isAltered(Game game) {
        if (!name.equals(game.name) || enabled != game.enabled)
            return true;

        if ((imageResource != null && imageResource.isAltered(game.imageResource)) ||
                (imageResource == null && game.imageResource != null))
            return true;

        if ((soundResource != null && soundResource.isAltered(game.soundResource)) ||
                (soundResource == null && game.soundResource != null))
            return true;

        if ((winSoundResource != null && winSoundResource.isAltered(game.winSoundResource)) ||
                (winSoundResource == null && game.winSoundResource != null))
            return true;

        if ((errorSoundResource != null && errorSoundResource.isAltered(game.errorSoundResource)) ||
                (errorSoundResource == null && game.errorSoundResource != null))
            return true;

        List<GameImage> gameImages = game.images;
        if (images.size() != gameImages.size())
            return true;
        else {
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).isAltered(gameImages.get(i)))
                    return true;
            }
        }
        return false;
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
        List<GameImage> enabledImages = new ArrayList<>();
        for (GameImage gameImage : images) {
            if (gameImage.isEnabled())
                enabledImages.add(gameImage);
        }
        return enabledImages;
    }

    public List<GameImage> getImages() {
        return images;
    }

    public void setImages(List<GameImage> images) {
        this.images = images;
    }

}
