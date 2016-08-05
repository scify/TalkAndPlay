package org.scify.talkandplay.services;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.utils.ConfigurationFile;

public class GameService {

    private ConfigurationFile configurationFile;

    public GameService() {
        this.configurationFile = ConfigurationFile.getInstance();
    }

    public void updateGame(String username, Game game, String type) throws Exception {
        Element profile = configurationFile.getUserElement(username);

        if (profile != null) {

            List gamesList = profile.getChild("games").getChild(type).getChild("games").getChildren("game");

            for (int i = 0; i < gamesList.size(); i++) {

                Element gameEl = (Element) gamesList.get(i);

                if (game.getName().equals(gameEl.getChildText("name"))) {

                    gameEl.getChild("enabled").setText(String.valueOf(game.isEnabled()));
                    gameEl.getChild("winSound").setText(game.getWinSound());
                    gameEl.getChild("errorSound").setText(game.getErrorSound());

                    /*  gameEl.getAttribute("image").setValue(game.getImage());
                     gameEl.getAttribute("winSound").setValue(game.getWinSound());
                     gameEl.getAttribute("errorSound").setValue(game.getErrorSound());*/
                    gameEl.getChild("gameImages").detach();
                    Element gameImagesChild = new Element("gameImages");

                    for (int j = 0; j < game.getImages().size(); j++) {
                        /*   Element imageEl = (Element) gameImagesList.get(j);
                         gameEl.getAttribute("enabled").setValue(String.valueOf(game.isEnabled()));*/
                        Element imageEl = new Element("image");

                        imageEl.addContent(new Element("name"));
                        imageEl.addContent(new Element("path").setText(game.getImages().get(j).getImage()));
                        imageEl.addContent(new Element("order").setText(String.valueOf(game.getImages().get(j).getOrder())));
                        imageEl.addContent(new Element("enabled").setText(String.valueOf(game.getImages().get(j).isEnabled())));

                        gameImagesChild.addContent(imageEl);
                    }
                    gameEl.addContent(gameImagesChild);
                }
            }
            configurationFile.update();
        }
    }

    public void updateGameType(String username, GameType gameType) throws Exception {
        Element profile = configurationFile.getUserElement(username);

        if (profile != null) {
            Element gameTypeEl = profile.getChild("games").getChild(gameType.getType() + "s");

            if (gameTypeEl != null) {
                gameTypeEl.getChild("winSound").setText(gameType.getWinSound());
                gameTypeEl.getChild("errorSound").setText(gameType.getErrorSound());

                configurationFile.update();
            }
        }
    }

    public List setDefaultGames() {
        List games = null;
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "defaultGames.xml";
            File file = new File(filePath);
            if (!file.exists() || file.isDirectory()) {
                return games;
            }

            SAXBuilder builder = new SAXBuilder();
            Document configurationFile = (Document) builder.build(file);

            games = configurationFile.getRootElement().getChildren();

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return games;
    }
}
