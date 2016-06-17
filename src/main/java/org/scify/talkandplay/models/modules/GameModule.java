package org.scify.talkandplay.models.modules;

import java.util.ArrayList;
import java.util.List;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameType;

public class GameModule extends Module {

    private List<GameType> gameTypes;

    public GameModule() {
        this.gameTypes = new ArrayList();
    }

    public List<GameType> getGameTypes() {
        return gameTypes;
    }

    public void setGameTypes(List<GameType> gameTypes) {
        this.gameTypes = gameTypes;
    }

}
