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
package org.scify.talkandplay.services;

import java.util.HashSet;
import java.util.List;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameCollection;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

public class GameService {

    private TalkAndPlayProfileConfiguration talkAndPlayConfigurationFile;

    public GameService() {
        this.talkAndPlayConfigurationFile = TalkAndPlayProfileConfiguration.getInstance();
    }

    private String getUniqueGameName(List<Game> gamesList) {
        int counter = 1;
        HashSet<String> gameNames = new HashSet<>();
        for (Game game: gamesList)
            gameNames.add(game.getNameUnmodified());

        String name = "Custom Game " + counter;
        while (gameNames.contains(name)) {
            counter ++;
            name = "Custom Game " + counter;
        }
        return name;
    }

    public void createGame(User user, Game game, String gameType) throws Exception {
        GameCollection gameCollection = user.getGameModule().getGameCollection(gameType);
        String gameName = getUniqueGameName(gameCollection.getGames());
        game.setName(gameName);
        gameCollection.addGame(game);
        update(user);
    }

    public void update(User user) throws Exception {
        talkAndPlayConfigurationFile.getConfigurationHandler().updateUser(user);
        talkAndPlayConfigurationFile.getConfigurationHandler().update();
    }
}
