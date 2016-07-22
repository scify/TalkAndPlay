package org.scify.talkandplay.services;

import java.util.List;
import org.jdom.Element;
import org.scify.talkandplay.models.games.Game;
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

}
