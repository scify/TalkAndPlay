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
public class GameCollection {

    protected String name;
    protected String gameType;
    protected boolean enabled;
    protected ResourceManager rm;

    protected ImageResource imageResource;
    protected SoundResource soundResource;
    protected SoundResource winSoundResource;
    protected SoundResource errorSoundResource;

    protected List<Game> games;
    protected HashMap<String, Integer> indexes;

    public GameCollection(String name, boolean enabled, String gameType) {
        this.name = name;
        this.enabled = enabled;
        this.gameType = gameType;
        this.games = new ArrayList<>();
        this.indexes = new HashMap<>();
        this.rm = ResourceManager.getInstance();
    }

    public GameCollection(GameCollection gameCollection) {
        name = gameCollection.name;
        enabled = gameCollection.enabled;
        gameType = gameCollection.gameType;

        imageResource = null;
        if (gameCollection.imageResource != null)
            imageResource = new ImageResource(gameCollection.imageResource);

        soundResource = null;
        if (gameCollection.soundResource != null)
            soundResource = new SoundResource(gameCollection.soundResource);

        winSoundResource = null;
        if (gameCollection.winSoundResource != null)
            winSoundResource = new SoundResource(gameCollection.winSoundResource);

        errorSoundResource = null;
        if (gameCollection.errorSoundResource != null)
            errorSoundResource = new SoundResource(gameCollection.errorSoundResource);

        games = new ArrayList<>();
        indexes = new HashMap<>();
        for (Game game: gameCollection.getGames()) {
            int index = games.size();
            indexes.put(game.getNameUnmodified(), index);
            switch (gameType) {
                case "stimulusReactionGame":
                    gameCollection.addGame(new StimulusReactionGame((StimulusReactionGame) game));
                    break;
                case "sequenceGame":
                    gameCollection.addGame(new SequenceGame((SequenceGame) game));
                    break;
                case "similarityGame":
                    gameCollection.addGame(new SimilarityGame((SimilarityGame) game));
                    break;
                default:
            }

        }
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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public List<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        String gameName = game.getNameUnmodified();
        int index = games.size();
        games.add(game);
        indexes.put(gameName, index);
    }

    public Game getGame(String name) {
        if (indexes.containsKey(name)) {
            return games.get(indexes.get(name));
        } else
            return null;
    }

    public List<Game> getEnabledGames() {
        List<Game> enabledGamesList = new ArrayList<>();
        for (Game game: games){
            if (game.isEnabled())
                enabledGamesList.add(game);
        }
        return enabledGamesList;
    }

    public boolean containsGame (String gameName) {
        return indexes.containsKey(gameName);
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
