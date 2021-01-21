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
package org.scify.talkandplay.services;

import java.io.File;
import java.util.List;

import io.sentry.Sentry;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

public class GameService {

    private TalkAndPlayProfileConfiguration talkAndPlayConfigurationFile;

    public GameService() {
        this.talkAndPlayConfigurationFile = TalkAndPlayProfileConfiguration.getInstance();
    }

    private String getUniqueGameName(List gamesList) {
        int counter = 1;
        String name = null;
        boolean foundUserName = false;
        while (!foundUserName) {
            String tempName = "Custom Game " + counter;
            int counterCopy = counter;
            for (int i = 0; i < gamesList.size(); i++) {
                Element gameEl = (Element) gamesList.get(i);
                //if game name is found try again with the incremented counter
                if (gameEl.getChildText("name").equals(tempName)) {
                    counter++;
                    break;
                }
            }
            //if the counter hasn't been changed, the unique game name has been found
            if (counterCopy == counter) {
                foundUserName = true;
                name = tempName;
            }
        }
        return name;
    }

    public void createGame(String username, Game game, String type) throws Exception {
        Element profile = talkAndPlayConfigurationFile.getConfigurationHandler().getUserElement(username);

        if (profile != null) {

            List gamesList = profile.getChild("games").getChild(type + "s").getChild("games").getChildren("game");

            Element gameEl = new Element("game");
            gameEl.addContent(new Element("name").setText(getUniqueGameName(gamesList)));
            gameEl.addContent(new Element("enabled").setText(String.valueOf(true)));
            gameEl.addContent(new Element("image"));
            //TODO: change that?
            gameEl.addContent(new Element("difficulty").setText(String.valueOf(game.getImages().size())));
            gameEl.addContent(new Element("winSound").setText(game.getWinSound().getPath()));
            gameEl.addContent(new Element("errorSound"));

            //add images
            Element gameImagesChild = new Element("gameImages");

            for (int j = 0; j < 4; j++) {
                Element imageEl = new Element("image");
                GameImage image = null;
                for (int k = 0; k < game.getImages().size(); k++) {
                    //order the images correctly
                    if (game.getImages().get(k).getOrder() == j + 1) {
                        image = game.getImages().get(k);
                        break;
                    }
                }
                if (image != null) {
                    imageEl.addContent(new Element("name"));
                    imageEl.addContent(new Element("path").setText(image.getImage().getPath()));
                    imageEl.addContent(new Element("order").setText(String.valueOf(image.getOrder())));
                    imageEl.addContent(new Element("enabled").setText(String.valueOf(image.isEnabled())));

                    gameImagesChild.addContent(imageEl);
                }
            }
            gameEl.addContent(gameImagesChild);
            profile.getChild("games").getChild(type + "s").getChild("games").addContent(gameEl);
            talkAndPlayConfigurationFile.getConfigurationHandler().update();
        }
    }

    public void updateGame(String username, Game game, String type) throws Exception {
        Element profile = talkAndPlayConfigurationFile.getConfigurationHandler().getUserElement(username);

        if (profile != null) {

            List gamesList = profile.getChild("games").getChild(type).getChild("games").getChildren("game");

            for (int i = 0; i < gamesList.size(); i++) {

                Element gameEl = (Element) gamesList.get(i);

                if (game.getName().equals(gameEl.getChildText("name"))) {

                    gameEl.getChild("enabled").setText(String.valueOf(game.isEnabled()));
                    gameEl.getChild("winSound").setText(game.getWinSound().getPath());
                    gameEl.getChild("errorSound").setText(game.getErrorSound().getPath());

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
                        imageEl.addContent(new Element("path").setText(game.getImages().get(j).getImage().getPath()));
                        imageEl.addContent(new Element("order").setText(String.valueOf(game.getImages().get(j).getOrder())));
                        imageEl.addContent(new Element("enabled").setText(String.valueOf(game.getImages().get(j).isEnabled())));

                        gameImagesChild.addContent(imageEl);
                    }
                    gameEl.addContent(gameImagesChild);
                }
            }
            talkAndPlayConfigurationFile.getConfigurationHandler().update();
        }
    }

    public void updateGameType(String username, GameType gameType) throws Exception {
        Element profile = talkAndPlayConfigurationFile.getConfigurationHandler().getUserElement(username);

        if (profile != null) {
            Element gameTypeEl = profile.getChild("games").getChild(gameType.getType() + "s");

            if (gameTypeEl != null) {
                gameTypeEl.getChild("winSound").setText(gameType.getWinSound().getPath());
                gameTypeEl.getChild("errorSound").setText(gameType.getErrorSound().getPath());

                talkAndPlayConfigurationFile.getConfigurationHandler().update();
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
            Sentry.capture(e);
        }
        return games;
    }
}
