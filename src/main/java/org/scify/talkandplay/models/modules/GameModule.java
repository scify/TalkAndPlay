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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameCollection;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;
import org.scify.talkandplay.utils.XMLConfigurationHandler;

public class GameModule extends Module {

    protected List<GameCollection> gameCollections;
    protected Random randomGenerator;

    public GameModule() {
        super();
        randomGenerator = new Random();
        this.gameCollections = new ArrayList();
    }

    public List<GameCollection> getGameTypes() {
        return gameCollections;
    }

    public GameCollection getGameCollection(String name) {
        for (GameCollection gameCollection : gameCollections) {
            if (gameCollection.getGameType().equals(name))
                return gameCollection;
        }
        return null;
    }

    public List<Game> getGames(String gameType) {
        GameCollection gameCollection = getGameCollection(gameType);
        List<Game> enabledGames = gameCollection.getEnabledGames();
        XMLConfigurationHandler ch = TalkAndPlayProfileConfiguration.getInstance().getConfigurationHandler();
        if (gameType.equals("stimulusReactionGame")) {
            for (Game game : ch.getDownloadedStimulusReactionGames()) {
                enabledGames.add(game);
            }
        } else if (gameType.equals("sequenceGame")) {
            for (Game game : ch.getDownloadedSequenceGames()) {
                enabledGames.add(game);
            }
        } else if (gameType.equals("similarityGame")) {
            for (Game game : ch.getDownloadedSimilarityGames()) {
                enabledGames.add(game);
            }
        }
        return enabledGames;
    }

    public Game getRandomGame(String gameType, String previousGame) {

        List<Game> enabledGames = getGames(gameType);
        int randomInput = enabledGames.size();
        if (randomInput == 0)
            return null;
        else if (randomInput == 1)
            return enabledGames.get(0);
        else {
            Game ret = null;
            while (ret == null) {
                int i = randomGenerator.nextInt(randomInput);
                if (previousGame == null || previousGame.isEmpty())
                    ret = enabledGames.get(i);
                else if (!enabledGames.get(i).getName().equals(previousGame))
                    ret = enabledGames.get(i);
            }
            return ret;
        }
    }
}
