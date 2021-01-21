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
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christina
 */
public class GameType {

    protected String name;
    protected ImageResource imageResource;
    protected SoundResource soundResource;
    protected String type;
    protected SoundResource winSoundResource;
    protected SoundResource errorSoundResource;
    protected boolean enabled;

    protected List<Game> games;
    protected List<Game> enabledGames;

    public GameType() {
        this.games = new ArrayList();
        this.enabledGames = new ArrayList();
    }

    public GameType(String name, boolean enabled, String type) {
        this.name = name;
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

    public SoundResource getWinSound() {
        return winSoundResource;
    }

    public void setWinSound(String path, ResourceType resourceType) {
        this.winSoundResource = new SoundResource(path, resourceType);
    }

    public SoundResource getErrorSound() {
        return errorSoundResource;
    }

    public void setErrorSound(String path, ResourceType resourceType) {
        this.errorSoundResource = new SoundResource(path, resourceType);
    }

}
