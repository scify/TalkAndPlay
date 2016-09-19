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
public class GameType {

    private String name;
    private String image;
    private String sound;
    private String type;
    private String winSound;
    private String errorSound;
    private boolean enabled;

    //used only to display default ImageIcons from the app jar
    private URL imageURL;

    private List<Game> games;
    private List<Game> enabledGames;

    public GameType() {
        this.games = new ArrayList();
        this.enabledGames = new ArrayList();
    }

    public GameType(String name, String image, boolean enabled, String type) {
        this.name = name;
        this.image = image;
        this.enabled = enabled;
        this.type = type;
        this.games = new ArrayList();
        this.enabledGames = new ArrayList();
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Game> getEnabledGames() {
        return enabledGames;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
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

}
