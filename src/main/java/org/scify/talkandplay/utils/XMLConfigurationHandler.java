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
    protected ResourceManager rm;

    public XMLConfigurationHandler() {
        properties = Properties.getInstance();
        configurationFilePath = properties.getApplicationDataFolder() + File.separator + "conf.xml";
        defaultUserConfigurationFilePath = properties.getApplicationFolder() + File.separator + "defaultUser.xml";
        rm = ResourceManager.getInstance();
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

        String image = communicationNode.getChildText("image");
        if (image.isEmpty()) {
            communicationModule.setImage("defaultImgs/communication_module.png", ResourceType.FROM_JAR);
        } else {
            communicationModule.setImage(image, ResourceType.FROM_RESOURCES);
        }

        String sound = communicationNode.getChildText("sound");
        if (sound.isEmpty()) {
            communicationModule.setSound("sounds/communication.mp3", ResourceType.FROM_RESOURCES);
        } else {
            communicationModule.setSound(sound, ResourceType.FROM_RESOURCES);
        }

        communicationModule.setEnabled("true".equals(communicationNode.getChildText("enabled")));
        communicationModule.setCategories(categoriesArray);

        return communicationModule;
    }

    private EntertainmentModule getEntertainmentModule(Element entertainmentNode) {
        //set the entertainment module settings
        EntertainmentModule entertainmentModule = new EntertainmentModule();
        entertainmentModule.setName(entertainmentNode.getChildText("name"));
        entertainmentModule.setEnabled("true".equals(entertainmentNode.getChildText("enabled")));

        String image = entertainmentNode.getChildText("image");
        if (image.isEmpty()) {
            entertainmentModule.setImage("defaultImgs/entertainment_module.png", ResourceType.FROM_JAR);
        } else {
            entertainmentModule.setImage(image, ResourceType.FROM_RESOURCES);
        }

        String sound = entertainmentNode.getChildText("sound");
        if (sound.isEmpty()) {
            entertainmentModule.setSound("sounds/entertainment.mp3", ResourceType.FROM_RESOURCES);
        } else {
            entertainmentModule.setSound(sound, ResourceType.FROM_RESOURCES);
        }

        //set the music module
        Element musicNode = (Element) entertainmentNode.getChild("music");
        MusicModule musicModule = new MusicModule();
        musicModule.setName(musicNode.getChildText("name"));
        musicModule.setSound(musicNode.getChildText("sound"), ResourceType.FROM_RESOURCES);
        musicModule.setFolderPath(musicNode.getChildText("path"));
        musicModule.setPlaylistSize(Integer.parseInt(musicNode.getChildText("playlistSize")));
        musicModule.setEnabled("true".equals(musicNode.getChildText("name")));

        String musicImage = musicNode.getChildText("image");
        if (musicImage.isEmpty()) {
            musicModule.setImage("defaultImgs/music_module.png", ResourceType.FROM_JAR);
        } else {
            musicModule.setImage(musicImage, ResourceType.FROM_RESOURCES);
        }

        String musicSound = musicNode.getChildText("sound");
        if (musicSound.isEmpty()) {
            musicModule.setSound("sounds/music.mp3", ResourceType.FROM_RESOURCES);
        } else {
            musicModule.setSound(musicSound, ResourceType.FROM_RESOURCES);
        }

        //set the video module
        Element videoNode = (Element) entertainmentNode.getChild("video");
        VideoModule videoModule = new VideoModule();
        videoModule.setName(videoNode.getChildText("name"));
        videoModule.setFolderPath(videoNode.getChildText("path"));
        videoModule.setPlaylistSize(Integer.parseInt(videoNode.getChildText("playlistSize")));
        videoModule.setEnabled("true".equals(videoNode.getChildText("name")));

        String videoImage = videoNode.getChildText("image");
        if (videoImage.isEmpty()) {
            videoModule.setImage("defaultImgs/video_module.png", ResourceType.FROM_JAR);
        } else {
            videoModule.setImage(videoImage, ResourceType.FROM_RESOURCES);
        }

        String videoSound = videoNode.getChildText("sound");
        if (videoSound.isEmpty()) {
            videoModule.setSound("sounds/video.mp3", ResourceType.FROM_RESOURCES);
        } else {
            videoModule.setSound(videoSound, ResourceType.FROM_RESOURCES);
        }

        entertainmentModule.setMusicModule(musicModule);
        entertainmentModule.setVideoModule(videoModule);

        return entertainmentModule;
    }

    private GameModule getGameModule(Element gameNode) {
        //set the game module settings
        GameModule gameModule = new GameModule();
        gameModule.setName(gameNode.getChildText("name"));
        gameModule.setEnabled("true".equals(gameNode.getChildText("enabled")));

        String image = gameNode.getChildText("image");
        if (image.isEmpty()) {
            gameModule.setImage("defaultImgs/games_module.png", ResourceType.FROM_JAR);
        } else {
            gameModule.setImage(image, ResourceType.FROM_RESOURCES);
        }

        String sound = gameNode.getChildText("sound");
        if (sound.isEmpty()) {
            gameModule.setSound("sounds/games.mp3", ResourceType.FROM_RESOURCES);
        } else {
            gameModule.setSound(sound, ResourceType.FROM_RESOURCES);
        }

        //set the stimulus reaction games
        Element stimulusReactionGamesNode = gameNode.getChild("stimulusReactionGames");
        if (stimulusReactionGamesNode != null) {
            String stimulusName = stimulusReactionGamesNode.getChildText("name");

            boolean stimulusEnabled = "true".equals(stimulusReactionGamesNode.getChildText("enabled"));
            GameType stimulusReactionType = new GameType(stimulusName, stimulusEnabled, "stimulusReactionGame");

            String stimulusSound = stimulusReactionGamesNode.getChildText("sound");
            if (stimulusSound.isEmpty()) {
                stimulusReactionType.setSound("sounds/stimulus_reaction.mp3", ResourceType.FROM_RESOURCES);
            } else {
                stimulusReactionType.setSound(stimulusSound, ResourceType.FROM_RESOURCES);
            }

            String stimulusImage = stimulusReactionGamesNode.getChildText("image");
            if (stimulusImage.isEmpty()) {
                stimulusReactionType.setImage("defaultImgs/stimulus_game.png", ResourceType.FROM_JAR);
            } else {
                stimulusReactionType.setImage(stimulusImage, ResourceType.FROM_RESOURCES);
            }

            String stimulusWinSound = stimulusReactionGamesNode.getChildText("winSound");
            if (stimulusWinSound.isEmpty()) {
                stimulusReactionType.setWinSound("", ResourceType.MISSING);
            } else {
                stimulusReactionType.setWinSound(stimulusWinSound, ResourceType.FROM_RESOURCES);
            }

            String stimulusErrorSound = stimulusReactionGamesNode.getChildText("errorSound");
            if (stimulusErrorSound.isEmpty()) {
                stimulusReactionType.setErrorSound("", ResourceType.MISSING);
            } else {
                stimulusReactionType.setErrorSound(stimulusErrorSound, ResourceType.FROM_RESOURCES);
            }

            List<Element> gamesList = stimulusReactionGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                Element e = gamesList.get(i);
                String name = e.getChildText("name");
                boolean enabled = "true".equals(e.getChildText("enabled"));
                int difficulty = Integer.parseInt(e.getChildText("difficulty"));
                StimulusReactionGame game = new StimulusReactionGame(name, enabled, difficulty);


                String winSound = e.getChildText("winSound");
                if (winSound == null || winSound.isEmpty()) {
                    game.setWinSound("sounds/win.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setWinSound(winSound, ResourceType.FROM_RESOURCES);
                }

                String errorSound = e.getChildText("errorSound");
                if (errorSound == null || errorSound.isEmpty()) {
                    game.setErrorSound("sounds/error.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setErrorSound(errorSound, ResourceType.FROM_RESOURCES);
                }

                String gameImage = e.getChildText("image");
                if (gameImage.isEmpty()) {
                    game.setImage("defaultImgs/stimulus_game.png", ResourceType.FROM_JAR);
                } else {
                    game.setImage(gameImage, ResourceType.FROM_RESOURCES);
                }

                List<Element> imagesList = e.getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    game.getImages().add(extractGameImage(imagesList.get(j)));
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
            String sequenceGamesName = sequenceGamesNode.getChildText("name");
            boolean enabled = "true".equals(sequenceGamesNode.getChildText("enabled"));
            GameType sequenceGameType = new GameType(sequenceGamesName, enabled, "sequenceGame");

            String sequenceGamesSound = sequenceGamesNode.getChildText("sound");
            if (sequenceGamesSound.isEmpty()) {
                sequenceGameType.setSound("sounds/time_sequence.mp3", ResourceType.FROM_RESOURCES);
            } else {
                sequenceGameType.setSound(sequenceGamesSound, ResourceType.FROM_RESOURCES);
            }

            String sequenceGamesImage = sequenceGamesNode.getChildText("image");
            if (sequenceGamesImage.isEmpty()) {
                sequenceGameType.setImage("defaultImgs/sequence_game.png", ResourceType.FROM_JAR);
            } else {
                sequenceGameType.setImage(sequenceGamesImage, ResourceType.FROM_RESOURCES);
            }

            String winSound = sequenceGamesNode.getChildText("winSound");
            if (winSound.isEmpty()) {
                sequenceGameType.setWinSound("", ResourceType.MISSING);
            } else {
                sequenceGameType.setWinSound(winSound, ResourceType.FROM_RESOURCES);
            }

            String errorSound = sequenceGamesNode.getChildText("errorSound");
            if (errorSound.isEmpty()) {
                sequenceGameType.setErrorSound("", ResourceType.MISSING);
            } else {
                sequenceGameType.setErrorSound(errorSound, ResourceType.FROM_RESOURCES);
            }

            List gamesList = sequenceGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                Element e = (Element) gamesList.get(i);
                String name = e.getChildText("name");
                enabled = "true".equals(e.getChildText("enabled"));
                int difficulty = Integer.parseInt(e.getChildText("difficulty"));
                SequenceGame game = new SequenceGame(name, enabled, difficulty);

                winSound = e.getChildText("winSound");
                if (winSound == null || winSound.isEmpty()) {
                    game.setWinSound("sounds/win.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setWinSound(winSound, ResourceType.FROM_RESOURCES);
                }

                errorSound = e.getChildText("errorSound");
                if (errorSound == null || errorSound.isEmpty()) {
                    game.setErrorSound("error/win.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setErrorSound(errorSound, ResourceType.FROM_RESOURCES);
                }

                image = e.getChildText("image");
                if (image.isEmpty()) {
                    game.setImage("defaultImgs/sequence_game.png", ResourceType.FROM_JAR);
                } else {
                    game.setImage(image, ResourceType.FROM_RESOURCES);
                }

                List<Element> imagesList = e.getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    game.getImages().add(extractGameImage(imagesList.get(j)));
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
            String name = similarGamesNode.getChildText("name");
            boolean enabled = "true".equals(similarGamesNode.getChildText("enabled"));
            GameType similarityGameType = new GameType(name, enabled, "similarityGame");

            String similarGamesSound = similarGamesNode.getChildText("sound");
            if (similarGamesSound.isEmpty()) {
                similarityGameType.setSound("sounds/find_the_similar.mp3", ResourceType.FROM_RESOURCES);
            } else {
                similarityGameType.setSound(similarGamesSound, ResourceType.FROM_RESOURCES);
            }

            String similarGamesImage = similarGamesNode.getChildText("image");
            if (similarGamesImage.isEmpty()) {
                similarityGameType.setImage("defaultImgs/similarity_game.png", ResourceType.FROM_JAR);
            } else {
                similarityGameType.setImage(similarGamesImage, ResourceType.FROM_RESOURCES);
            }

            String winSound = similarGamesNode.getChildText("winSound");
            if (winSound == null || winSound.isEmpty()) {
                similarityGameType.setWinSound("", ResourceType.MISSING);
            } else {
                similarityGameType.setWinSound(winSound, ResourceType.FROM_RESOURCES);
            }

            String errorSound = similarGamesNode.getChildText("errorSound");
            if (errorSound == null || errorSound.isEmpty()) {
                similarityGameType.setErrorSound("", ResourceType.MISSING);
            } else {
                similarityGameType.setErrorSound(errorSound, ResourceType.FROM_RESOURCES);
            }


            List<Element> gamesList = similarGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                Element e = gamesList.get(i);
                name = e.getChildText("name");
                enabled = "true".equals(e.getChildText("enabled"));
                int difficulty = Integer.parseInt(e.getChildText("difficulty"));
                SimilarityGame game = new SimilarityGame(name, enabled, difficulty);

                winSound = e.getChildText("winSound");
                if (winSound == null || winSound.isEmpty()) {
                    game.setWinSound("sounds/win.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setWinSound(winSound, ResourceType.FROM_RESOURCES);
                }

                errorSound = e.getChildText("errorSound");
                if (errorSound == null || errorSound.isEmpty()) {
                    game.setErrorSound("error/win.mp3", ResourceType.FROM_JAR);
                } else {
                    game.setErrorSound(errorSound, ResourceType.FROM_RESOURCES);
                }

                String gameImage = e.getChildText("image");
                if (gameImage.isEmpty()) {
                    game.setImage("defaultImgs/similarity_game.png", ResourceType.FROM_JAR);
                } else {
                    game.setImage(gameImage, ResourceType.FROM_RESOURCES);
                }

                List<Element> imagesList = e.getChild("gameImages").getChildren();

                for (int j = 0; j < imagesList.size(); j++) {
                    game.getImages().add(extractGameImage(imagesList.get(j)));
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
                String categoryName = categoryEl.getAttributeValue("name");
                ImageResource categoryImage = new ImageResource(categoryEl.getChildText("image"), ResourceType.FROM_RESOURCES);

                Category category = new Category(categoryName, categoryImage);

                SoundResource categorySoundResource = new SoundResource(categoryEl.getChildText("sound"), ResourceType.FROM_RESOURCES);
                category.setSound(categorySoundResource);

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
                    File file = rm.getFileOfResource(path, ResourceType.FROM_RESOURCES);
                    if (file == null || !file.isFile()) {
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

    protected GameImage extractGameImage(Element element) {
        String path = element.getChildText("path");
        ImageResource imageResource = new ImageResource(path, ResourceType.FROM_RESOURCES);
        boolean enabled = "true".equals(element.getChildText("enabled"));
        int order = Integer.parseInt(element.getChildText("order"));
        return new GameImage(imageResource, enabled, order);
    }
}
