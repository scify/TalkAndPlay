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
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

/**
 *
 * @author christina
 */
public class GameImage {

    protected String name;
    protected ImageResource imageResource;
    protected SoundResource soundResource;
    protected int order;
    protected boolean enabled;

    public GameImage(ImageResource imageResource, boolean enabled, int order) {
        this.name = "";
        this.imageResource = imageResource;
        this.enabled = enabled;
        this.order = order;
    }

    public GameImage(String name, boolean enabled, int order) {
        this.name = name;
        this.enabled = enabled;
        this.order = order;
    }

    public GameImage(GameImage gameImage) {
        name = gameImage.name;
        enabled = gameImage.enabled;
        order = gameImage.order;

        imageResource = null;
        if (gameImage.imageResource != null)
            imageResource = new ImageResource(gameImage.imageResource);

        soundResource = null;
        if (gameImage.soundResource != null)
            soundResource = new SoundResource(gameImage.soundResource);
    }

    public boolean isAltered(GameImage gameImage) {
        if (!name.equals(gameImage.name) || order != gameImage.order || enabled != gameImage.enabled)
            return true;

        if ((imageResource != null && imageResource.isAltered(gameImage.imageResource)) || (imageResource == null && gameImage.imageResource != null))
            return true;

        if ((soundResource != null && soundResource.isAltered(gameImage.soundResource)) || (soundResource == null && gameImage.soundResource != null))
            return true;

        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageResource getImage() {
        return imageResource;
    }

    public void setImage(String path, ResourceType resourceType) {

        this.imageResource = new ImageResource(path, resourceType);
    }

    public SoundResource getSound() {
        return soundResource;
    }

    public void setSound(String path, ResourceType resourceType) {
        this.soundResource = new SoundResource(path, resourceType);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
