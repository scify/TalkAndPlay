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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christina
 */
public class Game {

    private String name;
    private String image;
    private String sound;
    private String winSound;
    private String errorSound;
    private boolean enabled;
    private List<GameImage> images;
    private List<GameImage> enabledImages;

    //used only to display default ImageIcons from the app jar
    private URL imageURL;

    public Game() {
        this.images = new ArrayList();
    }

    public Game(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
        this.images = new ArrayList();
        this.enabledImages = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getWinSound() {
        return winSound;
    }

    public void setWinSound(String winSound) {
        this.winSound = winSound;
    }

    public String getErrorSound() {
        return errorSound;
    }

    public void setErrorSound(String errorSound) {
        this.errorSound = errorSound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
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
