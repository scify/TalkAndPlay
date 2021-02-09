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

import java.util.*;

/**
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
    protected ResourceManager rm;

    protected HashMap<String, Game> games;
    protected HashSet<String> enabledGames;

    public GameType(String name, boolean enabled, String type) {
        this.name = name;
        this.enabled = enabled;
        this.type = type;
        this.games = new HashMap<>();
        this.enabledGames = new HashSet<>();
        this.rm = ResourceManager.getInstance();
    }

    public GameType getCopy() {
        GameType gameType = new GameType(name, enabled, type);
        if (imageResource == null)
            gameType.imageResource = null;
        else
            gameType.imageResource = imageResource.getCopy();
        if (soundResource == null)
            gameType.soundResource = null;
        else
            gameType.soundResource = soundResource.getCopy();
        if (winSoundResource == null)
            gameType.winSoundResource = null;
        else
            gameType.winSoundResource = winSoundResource.getCopy();
        if (errorSoundResource == null)
            gameType.errorSoundResource = null;
        else
            gameType.errorSoundResource = errorSoundResource.getCopy();

        for (Map.Entry<String, Game> game : games.entrySet()) {
            gameType.games.put(game.getKey(), game.getValue().getCopy());
        }

        for (String gameName : enabledGames) {
            gameType.enabledGames.add(gameName);
        }
        return gameType;
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
        List<Game> gamesList = new ArrayList<>();
        for (Game game : games.values()) {
            gamesList.add(game);
        }
        return gamesList;
    }

    public void enableGame(String gameName) {
        enabledGames.add(gameName);
    }

    public void disableGame(String gameName) {
        enabledGames.remove(gameName);
    }

    public void addGame(Game game) {
        String gameName = game.getName();
        games.put(gameName, game);
        if (game.isEnabled())
            enableGame(gameName);
        else
            disableGame(gameName);
    }

    public Game getGame(String name) {
        return games.get(name);
    }

    public List<Game> getEnabledGames() {
        List<Game> enabledGamesList = new ArrayList<>();
        for (Game game : games.values()) {
            if (enabledGames.contains(game.getName()))
                enabledGamesList.add(game);
        }
        return enabledGamesList;
    }

    public boolean containsGame (String gameName) {
        return games.containsKey(gameName);
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

}
