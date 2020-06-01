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
package org.scify.talkandplay.utils;

import io.sentry.Sentry;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.Configuration;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.*;
import org.scify.talkandplay.models.modules.*;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConfigurationHandler is responsible for parsing the xml and other xml-related
 * functions.
 *
 * @author christina
 */
public class XMLConfigurationHandler {

    protected String configurationFilePath;
    protected String defaultUserConfigurationFilePath;
    protected Document configurationXmlDocument;

    protected List<User> users;
    //for each user we add his files in a hashmap
    protected Map<String, List<String>> userFiles;
    //contains all the image and sound paths that were found in the xml configuration.
    //we use this to check if all the paths exist on hard drive.
    protected List<String> imageAndSoundFilePaths;
    //the user that uses the application
    protected User currentUser;
    // a hidden user that could be used to create new users
    protected User defaultUser;
    Properties properties;
    static Logger logger = Logger.getLogger(XMLConfigurationHandler.class);

    public XMLConfigurationHandler() {

        properties = Properties.getInstance();
        configurationFilePath = System.getenv("APPDATA") + File.separator + "Talk and Play" + File.separator + "conf.xml";
        defaultUserConfigurationFilePath = properties.getApplicationFolder() + File.separator + "defaultUser.xml";

        initConfigurationFile();
    }

    private void initConfigurationFile() {
        try {
            File configurationFile = new File(configurationFilePath);
            if (!configurationFile.exists()) {
                logger.info("Configuration file not found.");
                createLocalConfigurationFile();
            }
            parseConfigurationFileXML();
            parseDefaultUserXml();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Sentry.capture(e);
        }
    }

    private void createLocalConfigurationFile() throws IOException {
        String defaultConfigurationFilePath = properties.getApplicationFolder() + File.separator + "conf.xml";
        File configurationFile = new File(configurationFilePath);
        File defaultConfigurationFile = new File(defaultConfigurationFilePath);
        if (defaultConfigurationFile.exists()) {
            logger.info("Copying default configuration file from: " + defaultConfigurationFile + " to: " + configurationFilePath);
            FileUtils.copyFile(defaultConfigurationFile, configurationFile);
        } else {
            logger.info("Default configuration file not found. Creating a new empty configuration file at:" + configurationFilePath);
            PrintWriter writer = new PrintWriter(configurationFilePath, "UTF-8");
            writer.println("<?xml version=\"1.0\"?>\n"
                    + "<profiles></profiles>");
            writer.close();
        }
    }

    public User getDefaultUser() {
        return defaultUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public Element getRootElement() {
        return configurationXmlDocument.getRootElement();
    }

    public Element getUserElement(String name) throws Exception {
        Element userEl = null;
        List profiles = configurationXmlDocument.getRootElement().getChildren();

        for (int i = 0; i < profiles.size(); i++) {

            userEl = (Element) profiles.get(i);

            if (name.equals(userEl.getChildText("name"))) {
                break;
            }
        }
        return userEl;
    }

    private void parseDefaultUserXml() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = (Document) builder.build(new File(defaultUserConfigurationFilePath));
        Element xmlUserElement = (Element) doc.getRootElement().getChildren().get(0);
        this.defaultUser = parseUserFromXml(xmlUserElement);
    }

    /**
     * Parse the XML file that holds all users' configuration
     *
     * @return
     * @throws Exception
     */
    private void parseConfigurationFileXML() throws Exception {
        File configurationFile = new File(configurationFilePath);
        SAXBuilder builder = new SAXBuilder();
        configurationXmlDocument = builder.build(configurationFile);

        userFiles = new HashMap();
        users = new ArrayList();
        List usersEl = configurationXmlDocument.getRootElement().getChildren();

        for (int i = 0; i < usersEl.size(); i++) {
            imageAndSoundFilePaths = new ArrayList();
            User user = parseUserFromXml((Element) usersEl.get(i));
            currentUser = user;
            users.add(user);
            userFiles.put(user.getName(), imageAndSoundFilePaths);
        }
    }

    private User parseUserFromXml(Element userEl) {
        imageAndSoundFilePaths = new ArrayList();

        User user = new User(userEl.getChildText("name"), userEl.getChildText("image"));
        currentUser = user;
        if (userEl.getAttributeValue("preselected") != null) {
            user.setPreselected(Boolean.parseBoolean(userEl.getAttributeValue("preselected")));
        } else {
            user.setPreselected(false);
        }

        Element configuration = (Element) userEl.getChild("configuration");
        user.setConfiguration(getConfiguration(configuration));

        Element communication = (Element) userEl.getChild("communication");
        user.setCommunicationModule(getCommunicationModule(imageAndSoundFilePaths, communication));

        Element entertainment = (Element) userEl.getChild("entertainment");
        user.setEntertainmentModule(getEntertainmentModule(entertainment));

        Element games = (Element) userEl.getChild("games");
        user.setGameModule(getGameModule(games));

        return user;
    }

    /**
     * Get the configuration list for a certain user
     *
     * @param configurationNode
     * @param profile
     * @return
     * @throws Exception
     */
    private Configuration getConfiguration(Element configurationNode) {
        Configuration configuration = new Configuration();
        Sensor selectionSensor = null;
        Sensor navigationSensor = null;

        configuration.setRotationSpeed(Integer.parseInt(configurationNode.getChildText("rotationSpeed")));
        configuration.setDefaultGridRow(Integer.parseInt(configurationNode.getChildText("defaultGridRow")));
        configuration.setDefaultGridColumn(Integer.parseInt(configurationNode.getChildText("defaultGridColumn")));
        configuration.setSound("true".equals(configurationNode.getChildText("sound")));
        configuration.setImage("true".equals(configurationNode.getChildText("image")));
        configuration.setText("true".equals(configurationNode.getChildText("text")));

        Element selectionSensorEl = configurationNode.getChild("selectionSensor");
        if ("mouse".equals(selectionSensorEl.getChildText("type"))) {
            selectionSensor = new MouseSensor(Integer.parseInt(selectionSensorEl.getChildText("button")),
                    Integer.parseInt(selectionSensorEl.getChildText("clickCount")), selectionSensorEl.getChildText("type"));
        } else if ("keyboard".equals(selectionSensorEl.getChildText("type"))) {
            selectionSensor = new KeyboardSensor(Integer.parseInt(selectionSensorEl.getChildText("keyCode")),
                    selectionSensorEl.getChildText("keyChar"), selectionSensorEl.getChildText("type"));
        }

        Element navigationSensorEl = configurationNode.getChild("navigationSensor");
        if (navigationSensorEl != null) {
            if ("mouse".equals(navigationSensorEl.getChildText("type"))) {
                navigationSensor = new MouseSensor(Integer.parseInt(navigationSensorEl.getChildText("button")),
                        Integer.parseInt(navigationSensorEl.getChildText("clickCount")), navigationSensorEl.getChildText("type"));
            } else if ("keyboard".equals(navigationSensorEl.getChildText("type"))) {
                navigationSensor = new KeyboardSensor(Integer.parseInt(navigationSensorEl.getChildText("keyCode")),
                        navigationSensorEl.getChildText("keyChar"), navigationSensorEl.getChildText("type"));
            }
            configuration.setNavigationSensor(navigationSensor);
        }

        configuration.setSelectionSensor(selectionSensor);

        return configuration;
    }

    private CommunicationModule getCommunicationModule(List<String> imageAndSoundPathsToFill, Element communicationNode) {
        //set the communication module settings
        List<Category> categoriesArray = new ArrayList();

        Element categories = (Element) communicationNode.getChild("categories");

        categoriesArray = getCategories(imageAndSoundPathsToFill, categories, categoriesArray, null);

        CommunicationModule communicationModule = new CommunicationModule();
        communicationModule.setName(communicationNode.getChildText("name"));
        /*communicationModule.setRows(Integer.parseInt(communicationNode.getChildText("rows")));
         communicationModule.setColumns(Integer.parseInt(communicationNode.getChildText("columns")));*/
        communicationModule.setRows(currentUser.getConfiguration().getDefaultGridRow());
        communicationModule.setColumns(currentUser.getConfiguration().getDefaultGridColumn());

        if (communicationNode.getChildText("image").isEmpty()) {
            communicationModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/communication_module.png"));
        } else {
            communicationModule.setImage(communicationNode.getChildText("image"));
        }

        if (communicationNode.getChildText("sound").isEmpty()) {
            communicationModule.setSound("resources/sounds/Επικοινωνία.mp3");
        } else {
            communicationModule.setSound(communicationNode.getChildText("sound"));
        }

        communicationModule.setEnabled("true".equals(communicationNode.getChildText("enabled")));
        communicationModule.setCategories(categoriesArray);

        return communicationModule;
    }

    private EntertainmentModule getEntertainmentModule(Element entertainmentNode) {
        //set the entertainment module settings
        EntertainmentModule entertainmentModule = new EntertainmentModule();
        entertainmentModule.setName(entertainmentNode.getChildText("name"));
        entertainmentModule.setSound(entertainmentNode.getChildText("sound"));
        entertainmentModule.setEnabled("true".equals(entertainmentNode.getChildText("enabled")));

        if (entertainmentNode.getChildText("image").isEmpty()) {
            entertainmentModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/entertainment_module.png"));
        } else {
            entertainmentModule.setImage(entertainmentNode.getChildText("image"));
        }

        if (entertainmentNode.getChildText("sound").isEmpty()) {
            entertainmentModule.setSound("resources/sounds/Ψυχαγωγία.mp3");
        } else {
            entertainmentModule.setSound(entertainmentNode.getChildText("sound"));
        }

        //set the music module
        Element musicNode = (Element) entertainmentNode.getChild("music");
        MusicModule musicModule = new MusicModule();
        musicModule.setName(musicNode.getChildText("name"));
        musicModule.setSound(musicNode.getChildText("sound"));
        musicModule.setFolderPath(musicNode.getChildText("path"));
        musicModule.setPlaylistSize(Integer.parseInt(musicNode.getChildText("playlistSize")));
        musicModule.setEnabled("true".equals(musicNode.getChildText("name")));

        if (musicNode.getChildText("image").isEmpty()) {
            musicModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/music_module.png"));
        } else {
            musicModule.setImage(musicNode.getChildText("image"));
        }

        if (musicNode.getChildText("sound").isEmpty()) {
            musicModule.setSound("resources/sounds/Μουσική.mp3");
        } else {
            musicModule.setSound(musicNode.getChildText("sound"));
        }

        //set the video module
        Element videoNode = (Element) entertainmentNode.getChild("video");
        VideoModule videoModule = new VideoModule();
        videoModule.setName(videoNode.getChildText("name"));
        videoModule.setSound(videoNode.getChildText("sound"));
        videoModule.setFolderPath(videoNode.getChildText("path"));
        videoModule.setPlaylistSize(Integer.parseInt(videoNode.getChildText("playlistSize")));
        videoModule.setEnabled("true".equals(videoNode.getChildText("name")));

        if (videoNode.getChildText("image").isEmpty()) {
            videoModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/video_module.png"));
        } else {
            videoModule.setImage(videoNode.getChildText("image"));
        }

        if (videoNode.getChildText("sound").isEmpty()) {
            videoModule.setSound("resources/sounds/Βίντεο.mp3");
        } else {
            videoModule.setSound(videoNode.getChildText("sound"));
        }

        entertainmentModule.setMusicModule(musicModule);
        entertainmentModule.setVideoModule(videoModule);

        return entertainmentModule;
    }

    private GameModule getGameModule(Element gameNode) {
        //set the game module settings
        GameModule gameModule = new GameModule();
        gameModule.setName(gameNode.getChildText("name"));
        gameModule.setSound(gameNode.getChildText("sound"));
        gameModule.setEnabled("true".equals(gameNode.getChildText("enabled")));

        if (gameNode.getChildText("image").isEmpty()) {
            gameModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/games_module.png"));
        } else {
            gameModule.setImage(gameNode.getChildText("image"));
        }

        if (gameNode.getChildText("sound").isEmpty()) {
            gameModule.setSound("resources/sounds/Παιχνίδια.mp3");
        } else {
            gameModule.setSound(gameNode.getChildText("sound"));
        }

        //set the stimulus reaction games
        Element stimulusReactionGamesNode = gameNode.getChild("stimulusReactionGames");
        if (stimulusReactionGamesNode != null) {
            GameType stimulusReactionType = new GameType(stimulusReactionGamesNode.getChildText("name"),
                    stimulusReactionGamesNode.getChildText("image"),
                    "true".equals(stimulusReactionGamesNode.getChildText("enabled")),
                    "stimulusReactionGame");

            if (stimulusReactionGamesNode.getChildText("sound").isEmpty()) {
                stimulusReactionType.setSound("resources/sounds/Ερέθισμα - Αντίδραση.mp3");
            } else {
                stimulusReactionType.setSound(stimulusReactionGamesNode.getChildText("sound"));
            }

            if (stimulusReactionGamesNode.getChildText("image").isEmpty()) {
                stimulusReactionType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/stimulus_game.png"));
            } else {
                stimulusReactionType.setImage(stimulusReactionGamesNode.getChildText("image"));
            }

            if (stimulusReactionGamesNode.getChildText("winSound").isEmpty()) {
                stimulusReactionType.setWinSound(null);
            } else {
                stimulusReactionType.setWinSound(stimulusReactionGamesNode.getChildText("winSound"));
            }

            if (stimulusReactionGamesNode.getChildText("errorSound").isEmpty()) {
                stimulusReactionType.setErrorSound(null);
            } else {
                stimulusReactionType.setErrorSound(stimulusReactionGamesNode.getChildText("errorSound"));
            }

            List gamesList = stimulusReactionGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                StimulusReactionGame game = new StimulusReactionGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));

                if (((Element) gamesList.get(i)).getChildText("winSound") == null || ((Element) gamesList.get(i)).getChildText("winSound").isEmpty()) {
                    game.setWinSound(null);
                } else {
                    game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("errorSound") == null || ((Element) gamesList.get(i)).getChildText("errorSound").isEmpty()) {
                    game.setErrorSound(null);
                } else {
                    game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("image").isEmpty()) {
                    game.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/stimulus_game.png"));
                } else {
                    game.setImage(((Element) gamesList.get(i)).getChildText("image"));
                }

                List imagesList = ((Element) gamesList.get(i)).getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    GameImage image = new GameImage(((Element) imagesList.get(j)).getChildText("path"),
                            "true".equals(((Element) imagesList.get(j)).getChildText("enabled")),
                            Integer.parseInt(((Element) imagesList.get(j)).getChildText("order")));
                    game.getImages().add(image);
                }

                game.setEnabledImages();
                stimulusReactionType.getGames().add(game);
            }

            for (Game game : stimulusReactionType.getGames()) {
                if (game.isEnabled()) {
                    stimulusReactionType.getEnabledGames().add((StimulusReactionGame) game);
                }
            }

            gameModule.getGameTypes().add(stimulusReactionType);
        }

        //set the sequence games
        Element sequenceGamesNode = gameNode.getChild("sequenceGames");
        if (sequenceGamesNode != null) {
            GameType sequenceGameType = new GameType(sequenceGamesNode.getChildText("name"),
                    sequenceGamesNode.getChildText("image"),
                    "true".equals(sequenceGamesNode.getChildText("enabled")),
                    "sequenceGame");

            if (sequenceGamesNode.getChildText("sound").isEmpty()) {
                sequenceGameType.setSound("resources/sounds/Χρονικής Αλληλουχίας.mp3");
            } else {
                sequenceGameType.setSound(sequenceGamesNode.getChildText("sound"));
            }

            if (sequenceGamesNode.getChildText("image").isEmpty()) {
                sequenceGameType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/sequence_game.png"));
            } else {
                sequenceGameType.setImage(sequenceGamesNode.getChildText("image"));
            }

            if (sequenceGamesNode.getChildText("winSound").isEmpty()) {
                sequenceGameType.setWinSound(null);
            } else {
                sequenceGameType.setWinSound(sequenceGamesNode.getChildText("winSound"));
            }

            if (sequenceGamesNode.getChildText("errorSound").isEmpty()) {
                sequenceGameType.setErrorSound(null);
            } else {
                sequenceGameType.setErrorSound(sequenceGamesNode.getChildText("errorSound"));
            }

            List gamesList = sequenceGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                SequenceGame game = new SequenceGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));

                if (((Element) gamesList.get(i)).getChildText("winSound") == null || ((Element) gamesList.get(i)).getChildText("winSound").isEmpty()) {
                    game.setWinSound(null);
                } else {
                    game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("errorSound") == null || ((Element) gamesList.get(i)).getChildText("errorSound").isEmpty()) {
                    game.setErrorSound(null);
                } else {
                    game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("image").isEmpty()) {
                    game.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/sequence_game.png"));
                } else {
                    game.setImage(((Element) gamesList.get(i)).getChildText("image"));
                }

                List imagesList = ((Element) gamesList.get(i)).getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    GameImage image = new GameImage(((Element) imagesList.get(j)).getChildText("path"),
                            "true".equals(((Element) imagesList.get(j)).getChildText("enabled")),
                            Integer.parseInt(((Element) imagesList.get(j)).getChildText("order")));
                    game.getImages().add(image);
                }
                game.setEnabledImages();
                sequenceGameType.getGames().add(game);
            }

            for (Game game : sequenceGameType.getGames()) {
                if (game.isEnabled()) {
                    sequenceGameType.getEnabledGames().add((SequenceGame) game);
                }
            }
            gameModule.getGameTypes().add(sequenceGameType);
        }

        //set the similar games
        Element similarGamesNode = gameNode.getChild("similarityGames");
        if (similarGamesNode != null) {
            GameType similarityGameType = new GameType(similarGamesNode.getChildText("name"),
                    similarGamesNode.getChildText("image"),
                    "true".equals(similarGamesNode.getChildText("enabled")),
                    "similarityGame");

            if (similarGamesNode.getChildText("sound").isEmpty()) {
                similarityGameType.setSound("resources/sounds/Βρες το όμοιο.mp3");
            } else {
                similarityGameType.setSound(similarGamesNode.getChildText("sound"));
            }

            if (similarGamesNode.getChildText("image").isEmpty()) {
                similarityGameType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/similarity_game.png"));
            } else {
                similarityGameType.setImage(similarGamesNode.getChildText("image"));
            }

            if (similarGamesNode.getChildText("winSound").isEmpty()) {
                similarityGameType.setWinSound(null);
            } else {
                similarityGameType.setWinSound(similarGamesNode.getChildText("winSound"));
            }

            if (similarGamesNode.getChildText("errorSound").isEmpty()) {
                similarityGameType.setErrorSound(null);
            } else {
                similarityGameType.setErrorSound(similarGamesNode.getChildText("errorSound"));
            }

            List gamesList = similarGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                SimilarityGame game = new SimilarityGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));

                if (((Element) gamesList.get(i)).getChildText("winSound") == null || ((Element) gamesList.get(i)).getChildText("winSound").isEmpty()) {
                    game.setWinSound(null);
                } else {
                    game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("errorSound") == null || ((Element) gamesList.get(i)).getChildText("errorSound").isEmpty()) {
                    game.setErrorSound(null);
                } else {
                    game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));
                }

                if (((Element) gamesList.get(i)).getChildText("image").isEmpty()) {
                    game.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/similarity_game.png"));
                } else {
                    game.setImage(((Element) gamesList.get(i)).getChildText("image"));
                }

                List imagesList = ((Element) gamesList.get(i)).getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    GameImage image = new GameImage(((Element) imagesList.get(j)).getChildText("path"),
                            "true".equals(((Element) imagesList.get(j)).getChildText("enabled")),
                            Integer.parseInt(((Element) imagesList.get(j)).getChildText("order")));
                    game.getImages().add(image);
                }
                game.setEnabledImages();
                similarityGameType.getGames().add(game);
            }

            for (Game game : similarityGameType.getGames()) {
                if (game.isEnabled()) {
                    similarityGameType.getEnabledGames().add((SimilarityGame) game);
                }
            }
            gameModule.getGameTypes().add(similarityGameType);
        }

        return gameModule;
    }

    /**
     * Recursive function to get the user categories
     *
     * @param categoriesNode
     * @param categories
     * @return
     */
    private List<Category> getCategories(List<String> imageAndSoundPaths, Element categoriesNode, List<Category> categories, Category parent) {

        if (categoriesNode == null) {
            return categories;
        } else {
            //get the user categories

            for (int i = 0; i < categoriesNode.getChildren().size(); i++) {

                Element categoryEl = (Element) categoriesNode.getChildren().get(i);

                Category category = new Category(
                        categoryEl.getAttributeValue("name"),
                        categoryEl.getChildText("image"));

                category.setSound(categoryEl.getChildText("sound"));

                if (categoryEl.getChildText("rows") != null && !categoryEl.getChildText("rows").isEmpty()) {
                    category.setRows(Integer.parseInt(categoryEl.getChildText("rows")));
                }

                if (categoryEl.getChildText("columns") != null && !categoryEl.getChildText("columns").isEmpty()) {
                    category.setColumns(Integer.parseInt(categoryEl.getChildText("columns")));
                }

                if (categoryEl.getChildText("editable") != null) {
                    category.setEditable(Boolean.parseBoolean(categoryEl.getChildText("editable")));
                } else {
                    category.setEditable(true);
                }

                if (parent != null) {
                    category.setParentCategory(new Category(parent.getName()));
                }

                if (categoryEl.getChildText("editable") != null) {
                    category.setEditable(Boolean.parseBoolean(categoryEl.getChildText("editable")));
                } else {
                    category.setEditable(true);
                }

                if (categoryEl.getChildText("order") != null) {
                    category.setOrder(Integer.parseInt(categoryEl.getChildText("order")));
                } else {
                    category.setOrder(0);
                }

                if (categoryEl.getChildText("hasSound") != null) {
                    category.setHasSound("true".equals(categoryEl.getChildText("hasSound")));
                } else {
                    category.setHasSound(true);
                }

                if (categoryEl.getChildText("hasImage") != null) {
                    category.setHasImage("true".equals(categoryEl.getChildText("hasImage")));
                } else {
                    category.setHasImage(true);
                }

                if (categoryEl.getChildText("hasText") != null) {
                    category.setHasText("true".equals(categoryEl.getChildText("hasText")));
                } else {
                    category.setHasText(true);
                }

                if (categoryEl.getChildText("enabled") != null) {
                    category.setEnabled("true".equals(categoryEl.getChildText("enabled")));
                } else {
                    category.setEnabled(true);
                }

                if (parent != null) {
                    category.setParentCategory(parent);
                }

                List<Category> categoriesArray = new ArrayList<>();

                Element subCategories = (Element) categoryEl.getChild("categories");
                categoriesArray = getCategories(imageAndSoundPaths, subCategories, categoriesArray, category);

                category.setSubCategories((ArrayList<Category>) categoriesArray);
                categories.add(category);

                if (!categoryEl.getChildText("sound").isEmpty()) {
                    imageAndSoundPaths.add(categoryEl.getChildText("sound"));
                }
                if (!categoryEl.getChildText("image").isEmpty()) {
                    imageAndSoundPaths.add(categoryEl.getChildText("image"));
                }

            }
            return categories;
        }
    }

    /**
     * For a certain user, check that all the files exist (in case the files
     * must be configured again)
     *
     * @param username
     * @return
     */
    public boolean hasBrokenFiles(String username) {
        for (Map.Entry<String, List<String>> entry : userFiles.entrySet()) {
            if (entry.getKey().equals(username)) {
                for (String path : entry.getValue()) {
                    if (!(new File(path).isFile())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void update() throws Exception {
        this.writeToXmlFile();
        this.parseConfigurationFileXML();
    }

    public List<String> getBrokenFiles(String username) {
        List<String> brokenFiles = new ArrayList();

        for (Map.Entry<String, List<String>> entry : userFiles.entrySet()) {
            if (entry.getKey().equals(username)) {
                for (String path : entry.getValue()) {
                    if (!(new File(path).isFile())) {
                        brokenFiles.add(path);
                    }
                }
            }
        }

        return brokenFiles;
    }

    /**
     * Write the new data to the xml file
     */
    private void writeToXmlFile() throws Exception {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(configurationXmlDocument, new OutputStreamWriter(
                new FileOutputStream(new File(configurationFilePath)), "UTF-8"));
    }

}
