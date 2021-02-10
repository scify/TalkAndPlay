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
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.scify.talkandplay.gui.users.UserFormPanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.Configuration;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.*;
import org.scify.talkandplay.models.modules.*;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * ConfigurationHandler is responsible for parsing the xml and other xml-related
 * functions.
 *
 * @author christina
 */
public class XMLConfigurationHandler {

    protected String configurationFilePath;
    //protected String defaultUserConfigurationFilePath;
    protected Document configurationXmlDocument;

    protected List<User> users;
    //for each user we add his files in a hashmap
    protected Map<String, List<String>> userFiles;
    //contains all the image and sound paths that were found in the xml configuration.
    //we use this to check if all the paths exist on hard drive.
    protected List<String> imageAndSoundFilePaths;
    // a hidden user that could be used to create new users
    protected User defaultUser;
    Properties properties;
    static Logger logger = Logger.getLogger(XMLConfigurationHandler.class);
    protected ResourceManager rm;

    public XMLConfigurationHandler() {
        properties = Properties.getInstance();
        configurationFilePath = properties.getApplicationDataFolder() + File.separator + "conf.xml";
        //defaultUserConfigurationFilePath = properties.getApplicationFolder() + File.separator + "defaultUser.xml";
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
            if (parseConfigurationFileXML(true))
                writeToXmlFile();

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

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name))
                return user;
        }
        return null;
    }

    public Element getRootElement() {
        return configurationXmlDocument.getRootElement();
    }

    /**
     * Parse the XML file that holds all users' configuration
     *
     * @return
     * @throws Exception
     */
    private boolean parseConfigurationFileXML(boolean fromInit) throws Exception {

        boolean needsSave = false;

        File configurationFile = new File(configurationFilePath);
        SAXBuilder builder = new SAXBuilder();
        configurationXmlDocument = builder.build(configurationFile);
        userFiles = new HashMap();
        users = new ArrayList();

        Element root = configurationXmlDocument.getRootElement();
        this.defaultUser = parseUserFromXml(root.getChild("defaultProfile"), true);

        Element userProfilesEl = root.getChild("userProfiles");
        List<Element> usersEl = userProfilesEl.getChildren();
        if (usersEl.isEmpty() && fromInit) {
            Element newUser = createNewProfile(defaultUser.getName());
            userProfilesEl.addContent(newUser);
            needsSave = true;
        }
        for (int i = 0; i < usersEl.size(); i++) {
            imageAndSoundFilePaths = new ArrayList();
            User user = parseUserFromXml((Element) usersEl.get(i), false);
            users.add(user);
            userFiles.put(user.getName(), imageAndSoundFilePaths);
        }

        return needsSave;
    }

    private User parseUserFromXml(Element userEl, boolean isDefault) {
        imageAndSoundFilePaths = new ArrayList();

        String userName = userEl.getChildText("name");
        Element imageEl = userEl.getChild("image");
        ImageResource imageResource;
        String imagePath = imageEl.getText();
        String imageType = imageEl.getAttributeValue("resourceType");
        ResourceType imageResourceType = ResourceType.valueOf(imageType);
        imageResource = new ImageResource(imagePath, imageResourceType);


        User user = new User(userName, imageResource);

        Element confEl = userEl.getChild("configuration");
        if (confEl.getChildren().size() > 0)
            user.setConfiguration(getConfiguration(confEl));
        else
            user.setConfiguration(defaultUser.getConfiguration().getCopy());

        Element communication = userEl.getChild("communication");
        user.setCommunicationModule(getCommunicationModule(imageAndSoundFilePaths, communication, isDefault));
        user.getCommunicationModule().setRows(user.getConfiguration().getDefaultGridRow());
        user.getCommunicationModule().setColumns(user.getConfiguration().getDefaultGridColumn());


        Element entertainment = userEl.getChild("entertainment");
        user.setEntertainmentModule(getEntertainmentModule(entertainment, isDefault));


        Element games = userEl.getChild("games");
        user.setGameModule(getGameModule(games, isDefault));

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

    private CommunicationModule getCommunicationModule(List<String> imageAndSoundPathsToFill, Element communicationNode, boolean isDefault) {
        //set the communication module settings
        CommunicationModule communicationModule = new CommunicationModule();

        if (isDefault)
            communicationModule.setName(communicationNode.getChildText("name"));
        else
            communicationModule.setName(defaultUser.getCommunicationModule().getNameUnmodified());

        communicationModule.setEnabled("true".equals(communicationNode.getAttributeValue("enabled")));

        String image = communicationNode.getChildText("image");
        if (image == null)
            communicationModule.setImage(defaultUser.getCommunicationModule().getImageResource().getCopy());
        else if (image.isEmpty())
            communicationModule.setImage(new ImageResource("defaultImgs/communication_module.png", ResourceType.JAR));
        else
            communicationModule.setImage(new ImageResource(image, ResourceType.BUNDLE));


        String sound = communicationNode.getChildText("sound");
        if (sound == null)
            communicationModule.setSound(defaultUser.getCommunicationModule().getSoundResource().getCopy());
        else if (sound.isEmpty())
            communicationModule.setSound(new SoundResource("sounds/communication.mp3", ResourceType.BUNDLE));
        else
            communicationModule.setSound(new SoundResource(sound, ResourceType.BUNDLE));

        Element categoriesEl = communicationNode.getChild("categories");
        gatherCommunicationCategories(communicationModule, imageAndSoundPathsToFill, categoriesEl, isDefault);

        return communicationModule;
    }

    private EntertainmentModule getEntertainmentModule(Element entertainmentNode, boolean isDefault) {

        EntertainmentModule entertainmentModule = new EntertainmentModule();

        if (isDefault)
            entertainmentModule.setName(entertainmentNode.getChildText("name"));
        else
            entertainmentModule.setName(defaultUser.getEntertainmentModule().getNameUnmodified());

        entertainmentModule.setEnabled("true".equals(entertainmentNode.getAttributeValue("enabled")));

        String image = entertainmentNode.getChildText("image");
        if (image == null)
            entertainmentModule.setImage(defaultUser.getEntertainmentModule().getImageResource().getCopy());
        else if (image.isEmpty())
            entertainmentModule.setImage(new ImageResource("defaultImgs/entertainment_module.png", ResourceType.JAR));
        else
            entertainmentModule.setImage(new ImageResource(image, ResourceType.BUNDLE));


        String sound = entertainmentNode.getChildText("sound");
        if (sound == null)
            entertainmentModule.setSound(defaultUser.getEntertainmentModule().getSoundResource().getCopy());
        else if (sound.isEmpty())
            entertainmentModule.setSound(new SoundResource("sounds/entertainment.mp3", ResourceType.BUNDLE));
        else
            entertainmentModule.setSound(new SoundResource(sound, ResourceType.BUNDLE));


        //set the music module
        Element musicNode = entertainmentNode.getChild("music");
        if (musicNode == null) {
            entertainmentModule.setMusicModule(defaultUser.getEntertainmentModule().getMusicModule().getCopy());
        } else {
            MusicModule musicModule = new MusicModule();
            musicModule.setName(musicNode.getChildText("name"));
            musicModule.setSound(new SoundResource(musicNode.getChildText("sound"), ResourceType.BUNDLE));
            musicModule.setFolderPath(musicNode.getChildText("path"));
            musicModule.setPlaylistSize(Integer.parseInt(musicNode.getChildText("playlistSize")));
            musicModule.setEnabled("true".equals(musicNode.getAttributeValue("enabled")));

            String musicImage = musicNode.getChildText("image");
            if (musicImage.isEmpty()) {
                musicModule.setImage(new ImageResource("defaultImgs/music_module.png", ResourceType.JAR));
            } else {
                musicModule.setImage(new ImageResource(musicImage, ResourceType.BUNDLE));
            }

            String musicSound = musicNode.getChildText("sound");
            if (musicSound.isEmpty()) {
                musicModule.setSound(new SoundResource("sounds/music.mp3", ResourceType.BUNDLE));
            } else {
                musicModule.setSound(new SoundResource(musicSound, ResourceType.BUNDLE));
            }
            entertainmentModule.setMusicModule(musicModule);
        }

        //set the video module
        Element videoNode = entertainmentNode.getChild("video");
        if (videoNode == null) {
            entertainmentModule.setVideoModule(defaultUser.getEntertainmentModule().getVideoModule().getCopy());
        } else {
            VideoModule videoModule = new VideoModule();
            videoModule.setName(videoNode.getChildText("name"));
            videoModule.setFolderPath(videoNode.getChildText("path"));
            videoModule.setPlaylistSize(Integer.parseInt(videoNode.getChildText("playlistSize")));
            videoModule.setEnabled("true".equals(videoNode.getAttributeValue("enabled")));

            String videoImage = videoNode.getChildText("image");
            if (videoImage.isEmpty()) {
                videoModule.setImage(new ImageResource("defaultImgs/video_module.png", ResourceType.JAR));
            } else {
                videoModule.setImage(new ImageResource(videoImage, ResourceType.BUNDLE));
            }

            String videoSound = videoNode.getChildText("sound");
            if (videoSound.isEmpty()) {
                videoModule.setSound(new SoundResource("sounds/video.mp3", ResourceType.BUNDLE));
            } else {
                videoModule.setSound(new SoundResource(videoSound, ResourceType.BUNDLE));
            }
            entertainmentModule.setVideoModule(videoModule);
        }

        return entertainmentModule;
    }

    private GameModule getGameModule(Element gameNode, boolean isDefault) {
        GameModule gameModule = new GameModule();

        if (isDefault)
            gameModule.setName(gameNode.getChildText("name"));
        else
            gameModule.setName(defaultUser.getGameModule().getNameUnmodified());

        gameModule.setEnabled("true".equals(gameNode.getAttributeValue("enabled")));

        String image = gameNode.getChildText("image");
        if (image == null)
            gameModule.setImage(defaultUser.getGameModule().getImageResource().getCopy());
        else if (image.isEmpty())
            gameModule.setImage(new ImageResource("defaultImgs/games_module.png", ResourceType.JAR));
        else
            gameModule.setImage(new ImageResource(image, ResourceType.BUNDLE));

        String sound = gameNode.getChildText("sound");
        if (sound == null)
            gameModule.setSound(defaultUser.getGameModule().getSoundResource().getCopy());
        else if (sound.isEmpty())
            gameModule.setSound(new SoundResource("sounds/games.mp3", ResourceType.BUNDLE));
        else
            gameModule.setSound(new SoundResource(sound, ResourceType.BUNDLE));

        //set the stimulus reaction games
        Element stimulusReactionGamesNode = gameNode.getChild("stimulusReactionGames");
        if (stimulusReactionGamesNode != null) {
            boolean enabled = "true".equals(stimulusReactionGamesNode.getAttributeValue("enabled"));

            String stimulusName = stimulusReactionGamesNode.getChildText("name");
            GameType stimulusReactionType;
            if (isDefault) {
                stimulusReactionType = new GameType(stimulusName, enabled, "stimulusReactionGame");
            } else {
                GameType defaultGameType = defaultUser.getGameModule().getGameType("stimulusReactionGame");
                stimulusReactionType = new GameType(defaultGameType.getNameUnmodified(), enabled, "stimulusReactionGame");
            }
            gatherSimpleGameTypeFields(stimulusReactionType, stimulusReactionGamesNode);
            gatherGames(stimulusReactionType, stimulusReactionGamesNode);
            if (!isDefault) {
                List<Game> defaultGames = defaultUser.getGameModule().getGameType("stimulusReactionGame").getGames();
                for (Game game : defaultGames) {
                    String gameName = game.getNameUnmodified();
                    if (!stimulusReactionType.containsGame(gameName)) {
                        stimulusReactionType.addGame(game.getCopy());
                    }
                }
            }
            gameModule.getGameTypes().add(stimulusReactionType);
        }
        //set the sequence games
        Element sequenceGamesNode = gameNode.getChild("sequenceGames");
        if (sequenceGamesNode != null) {
            boolean enabled = "true".equals(sequenceGamesNode.getChildText("enabled"));

            String sequenceGamesName = sequenceGamesNode.getChildText("name");
            GameType sequenceGameType;
            if (isDefault) {
                sequenceGameType = new GameType(sequenceGamesName, enabled, "sequenceGame");
            } else {
                GameType defaultGameType = defaultUser.getGameModule().getGameType("sequenceGame");
                sequenceGameType = new GameType(defaultGameType.getNameUnmodified(), enabled, "sequenceGame");
            }
            gatherSimpleGameTypeFields(sequenceGameType, sequenceGamesNode);
            gatherGames(sequenceGameType, sequenceGamesNode);
            if (!isDefault) {
                List<Game> defaultGames = defaultUser.getGameModule().getGameType("sequenceGame").getGames();
                for (Game game : defaultGames) {
                    String gameName = game.getNameUnmodified();
                    if (!sequenceGameType.containsGame(gameName)) {
                        sequenceGameType.addGame(game.getCopy());
                    }
                }
            }
            gameModule.getGameTypes().add(sequenceGameType);
        }

        //set the similar games
        Element similarGamesNode = gameNode.getChild("similarityGames");
        if (similarGamesNode != null) {
            boolean enabled = "true".equals(similarGamesNode.getChildText("enabled"));

            String similarGamesName = similarGamesNode.getChildText("name");
            GameType similarityGameType;
            if (isDefault) {
                similarityGameType = new GameType(similarGamesName, enabled, "similarityGame");
            } else {
                GameType defaultGameType = defaultUser.getGameModule().getGameType("similarityGame");
                similarityGameType = new GameType(defaultGameType.getNameUnmodified(), enabled, "similarityGame");
            }
            gatherSimpleGameTypeFields(similarityGameType, similarGamesNode);
            gatherGames(similarityGameType, similarGamesNode);
            if (!isDefault) {
                List<Game> defaultGames = defaultUser.getGameModule().getGameType("similarityGame").getGames();
                for (Game game : defaultGames) {
                    String gameName = game.getNameUnmodified();
                    if (!similarityGameType.containsGame(gameName)) {
                        similarityGameType.addGame(game.getCopy());
                    }
                }
            }
            gameModule.getGameTypes().add(similarityGameType);
        }

        return gameModule;
    }

    protected void gatherSimpleGameTypeFields(GameType gameType, Element parentNode) {
        Element soundEl = parentNode.getChild("sound");
        String soundPath = soundEl.getText();
        if (soundPath.isEmpty()) {
            switch (gameType.getType()) {
                case "stimulusReactionGame":
                    gameType.setSound(new SoundResource("sounds/stimulus_reaction.mp3", ResourceType.BUNDLE));
                    break;
                case "sequenceGame":
                    gameType.setSound(new SoundResource("sounds/time_sequence.mp3", ResourceType.BUNDLE));
                    break;
                case "similarityGame":
                    gameType.setSound(new SoundResource("sounds/find_the_similar.mp3", ResourceType.BUNDLE));
                    break;
                default:
            }
        } else {
            String resourceTypeStr = soundEl.getAttributeValue("resourceType");
            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
            gameType.setSound(new SoundResource(soundPath, resourceType));
        }

        Element imageEl = parentNode.getChild("image");
        String imagePath = imageEl.getText();
        if (imagePath.isEmpty()) {
            switch (gameType.getType()) {
                case "stimulusReactionGame":
                    gameType.setImage(new ImageResource("defaultImgs/stimulus_game.png", ResourceType.BUNDLE));
                    break;
                case "sequenceGame":
                    gameType.setImage(new ImageResource("defaultImgs/sequence_game.png", ResourceType.BUNDLE));
                    break;
                case "similarityGame":
                    gameType.setImage(new ImageResource("defaultImgs/similarity_game.png", ResourceType.BUNDLE));
                    break;
                default:
            }
        } else {
            String resourceTypeStr = imageEl.getAttributeValue("resourceType");
            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
            gameType.setImage(new ImageResource(imagePath, resourceType));
        }

        Element soundWinEl = parentNode.getChild("winSound");
        String soundWinPath = soundWinEl.getText();
        if (soundWinPath.isEmpty()) {
            gameType.setWinSound(null);
        } else {
            String resourceTypeStr = soundWinEl.getAttributeValue("resourceType");
            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
            gameType.setWinSound(new SoundResource(soundWinPath, resourceType));
        }

        Element soundErrorEl = parentNode.getChild("errorSound");
        String soundErrorPath = soundErrorEl.getText();
        if (soundErrorPath.isEmpty()) {
            gameType.setErrorSound(null);
        } else {
            String resourceTypeStr = soundErrorEl.getAttributeValue("resourceType");
            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
            gameType.setErrorSound(new SoundResource(soundErrorPath, resourceType));
        }
    }

    protected void gatherGames(GameType gameType, Element parentNode) {
        Element gamesEl = parentNode.getChild("games");
        if (gamesEl != null) {
            List<Element> gamesList = gamesEl.getChildren();
            for (Element gameEl : gamesList) {
                String name = gameEl.getAttributeValue("name");
                String enabled = gameEl.getAttributeValue("enabled");
                //check if Game has altered data or default should be used
                if (gameEl.getChildren().isEmpty()) {
                    Game game = defaultUser.getGameModule().getGameType(gameType.getName()).getGame(name).getCopy();
                    if ("true".equals(enabled))
                        game.setEnabled(true);
                    else
                        game.setEnabled(false);
                    gameType.addGame(game);
                } else {
                    int difficulty = Integer.parseInt(gameEl.getChildText("difficulty"));

                    Game game = null;
                    switch (gameType.getType()) {
                        case "stimulusReactionGame":
                            game = new StimulusReactionGame(name, "true".equals(enabled), difficulty);
                            break;
                        case "sequenceGame":
                            game = new SequenceGame(name, "true".equals(enabled), difficulty);
                            break;
                        case "similarityGame":
                            game = new SimilarityGame(name, "true".equals(enabled), difficulty);
                            break;
                        default:
                    }

                    Element winSoundEl = gameEl.getChild("winSound");
                    SoundResource winSoundResource = new SoundResource("sounds/win.mp3", ResourceType.JAR);
                    if (winSoundEl != null) {
                        String winSound = winSoundEl.getText();
                        if (!winSound.isEmpty()) {
                            String resourceTypeStr = winSoundEl.getAttributeValue("resourceType");
                            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
                            winSoundResource = new SoundResource(winSound, resourceType);
                        }
                    }
                    game.setWinSound(winSoundResource);

                    Element errorSoundEl = gameEl.getChild("errorSound");
                    SoundResource errorSoundResource = new SoundResource("sounds/error.mp3", ResourceType.JAR);
                    if (errorSoundEl != null) {
                        String errorSound = errorSoundEl.getText();
                        if (!errorSound.isEmpty()) {
                            String resourceTypeStr = errorSoundEl.getAttributeValue("resourceType");
                            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
                            errorSoundResource = new SoundResource(errorSound, resourceType);
                        }
                    }
                    game.setErrorSound(errorSoundResource);

                    Element imageEl = gameEl.getChild("image");
                    ImageResource imageResource = null;
                    switch (gameType.getType()) {
                        case "stimulusReactionGame":
                            imageResource = new ImageResource("defaultImgs/stimulus_game.png", ResourceType.JAR);
                            break;
                        case "sequenceGame":
                            imageResource = new ImageResource("defaultImgs/sequence_game.png", ResourceType.JAR);
                            break;
                        case "similarityGame":
                            imageResource = new ImageResource("defaultImgs/similarity_game.png", ResourceType.JAR);
                            break;
                        default:
                    }
                    if (imageEl != null) {
                        String image = imageEl.getText();
                        if (!image.isEmpty()) {
                            String resourceTypeStr = errorSoundEl.getAttributeValue("resourceType");
                            ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
                            imageResource = new ImageResource(image, resourceType);
                        }
                    }
                    game.setImage(imageResource);

                    List<Element> gameImages = gameEl.getChild("gameImages").getChildren();
                    for (Element gameImage : gameImages)
                        game.getImages().add(extractGameImage(gameImage));

                    game.setEnabledImages();
                    gameType.addGame(game);
                }
            }
        }
    }

    protected void gatherSimpleCommunicationCategoryFields(Category category, Element categoryEl) {
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
    }

    private void gatherCommunicationCategories(CommunicationModule communicationModule, List<String> imageAndSoundPaths, Element categoriesNode, boolean isDefault) {
        List<Element> categories = categoriesNode.getChildren();
        HashSet<String> categoryNamesAdded = new HashSet<>();
        if (categories != null) {
            for (Element categoryEl : categories) {

                String categoryName = categoryEl.getAttributeValue("name");
                Category category = new Category(categoryName);
                category.setEnabled(true);
                communicationModule.addCategory(category);
                categoryNamesAdded.add(categoryName);

                String supportedLanguages = categoryEl.getAttributeValue("languages");
                if (supportedLanguages != null) {
                    for (String language : supportedLanguages.trim().split(" ")) {
                        communicationModule.addLanguageToCategory(categoryName, language);
                    }
                }


                String imagePath = categoryEl.getChildText("image");
                ImageResource categoryImage = new ImageResource(imagePath, ResourceType.BUNDLE);
                category.setImage(categoryImage);
                imageAndSoundPaths.add(imagePath);

                String soundPath = categoryEl.getChildText("sound");
                SoundResource categorySoundResource = new SoundResource(soundPath, ResourceType.BUNDLE);
                category.setSound(categorySoundResource);
                imageAndSoundPaths.add(soundPath);

                gatherSimpleCommunicationCategoryFields(category, categoryEl);

                List<Category> subCategories = new ArrayList<>();
                category.setSubCategories(subCategories);
                Element subCategoriesEl = categoryEl.getChild("categories");
                if (subCategoriesEl != null) {
                    List<Element> subCategoriesChildren = subCategoriesEl.getChildren();
                    List<Category> subCategoriesList = new ArrayList<>();
                    for (Element subCategoryEl : subCategoriesChildren) {
                        categoryName = subCategoryEl.getAttributeValue("name");
                        Category subCategory = new Category(categoryName);
                        subCategory.setParentCategory(category);
                        subCategory.setEnabled(true);

                        imagePath = subCategoryEl.getChildText("image");
                        categoryImage = new ImageResource(imagePath, ResourceType.BUNDLE);
                        subCategory.setImage(categoryImage);
                        imageAndSoundPaths.add(imagePath);

                        soundPath = subCategoryEl.getChildText("sound");
                        categorySoundResource = new SoundResource(soundPath, ResourceType.BUNDLE);
                        subCategory.setSound(categorySoundResource);
                        imageAndSoundPaths.add(soundPath);

                        gatherSimpleCommunicationCategoryFields(subCategory, subCategoryEl);
                        subCategoriesList.add(subCategory);
                    }
                    category.setSubCategories(subCategoriesList);
                }
            }
        }
        if (!isDefault) {
            for (Category defCategory : defaultUser.getCommunicationModule().getCategories()) {
                if (!categoryNamesAdded.contains(defCategory.getNameUnmodified())) {
                    communicationModule.addCategory(defCategory.getCopy());
                }
            }
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
                    File file = rm.getFileOfResource(path, ResourceType.BUNDLE);
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
        this.parseConfigurationFileXML(false);
    }

    public List<String> getBrokenFiles(String username) {
        List<String> brokenFiles = new ArrayList();

        for (Map.Entry<String, List<String>> entry : userFiles.entrySet()) {
            if (entry.getKey().equals(username)) {
                for (String path : entry.getValue()) {
                    File file = rm.getFileOfResource(path, ResourceType.BUNDLE);
                    if (file == null || !file.isFile()) {
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
        Element pathEl = element.getChild("path");
        String path = pathEl.getText();
        String resourceTypeStr = pathEl.getAttributeValue("resourceType");
        ResourceType resourceType = ResourceType.valueOf(resourceTypeStr);
        ImageResource imageResource = new ImageResource(path, resourceType);
        boolean enabled = "true".equals(element.getAttributeValue("enabled"));
        int order = Integer.parseInt(element.getChildText("order"));
        return new GameImage(imageResource, enabled, order);
    }

    protected Element createNewProfile(String name) {
        Element profile = new Element("profile");
        Element defaultProfile = configurationXmlDocument.getRootElement().getChild("defaultProfile");
        profile.addContent(new Element("name").setText(name));

        Element imageOfDefaultProfile = defaultProfile.getChild("image");
        Element image = new Element(imageOfDefaultProfile.getName());
        image.addContent(imageOfDefaultProfile.getText());
        Attribute attributeOfDefImage = (Attribute) imageOfDefaultProfile.getAttributes().get(0);
        image.setAttribute(attributeOfDefImage.getName(), attributeOfDefImage.getValue());
        profile.addContent(image);

        profile.addContent(new Element("configuration"));

        Element communication = new Element("communication").setAttribute("enabled", "true");
        communication.addContent(new Element("categories"));
        profile.addContent(communication);

        Element entertainment = new Element("entertainment").setAttribute("enabled", "true");
        profile.addContent(entertainment);

        Element games = new Element("games").setAttribute("enabled", "true");

        Element stimulusReactionGamesEl = convertToXMLElement("stimulusReactionGames", "enabled", true);
        stimulusReactionGamesEl.addContent(new Element("image"));
        stimulusReactionGamesEl.addContent(new Element("sound"));
        stimulusReactionGamesEl.addContent(new Element("winSound"));
        stimulusReactionGamesEl.addContent(new Element("errorSound"));
        stimulusReactionGamesEl.addContent(new Element("games"));
        games.addContent(stimulusReactionGamesEl);

        Element sequenceGamesEl = convertToXMLElement("sequenceGames", "enabled", true);
        sequenceGamesEl.addContent(new Element("image"));
        sequenceGamesEl.addContent(new Element("sound"));
        sequenceGamesEl.addContent(new Element("winSound"));
        sequenceGamesEl.addContent(new Element("errorSound"));
        sequenceGamesEl.addContent(new Element("games"));
        games.addContent(sequenceGamesEl);

        Element similarityGamesEl = convertToXMLElement("similarityGames", "enabled", true);
        similarityGamesEl.addContent(new Element("image"));
        similarityGamesEl.addContent(new Element("sound"));
        similarityGamesEl.addContent(new Element("winSound"));
        similarityGamesEl.addContent(new Element("errorSound"));
        similarityGamesEl.addContent(new Element("games"));
        games.addContent(similarityGamesEl);
        profile.addContent(games);

        return profile;
    }

    public void deleteUser(String name) {
        List<Element> profiles = configurationXmlDocument.getRootElement().getChild("userProfiles").getChildren();
        for (Element profile : profiles) {
            if (profile.getChildText("name").equals(name)) {
                profile.detach();
                break;
            }
        }
    }

    public void createNewUser(String name) {
        Element profile = createNewProfile(name);
        Element profiles = configurationXmlDocument.getRootElement().getChild("userProfiles");
        profiles.addContent(profile);
    }

    public Element getUserElement(String name) {
        List<Element> userProfiles = configurationXmlDocument.getRootElement().getChild("userProfiles").getChildren();
        for (Element userProfile : userProfiles) {
            if (userProfile.getChildText("name").equals(name))
                return userProfile;
        }
        return null;
    }

    public void updateUser(String name, String newName, ImageResource imageResource, Configuration configuration) {
        Element userEl = getUserElement(name);
        userEl.getChild("name").setText(newName);

        if (imageResource != null) {
            userEl.getChild("image").detach();
            userEl.addContent(1, convertToXMLElement("image", imageResource));
        }

        Element oldConfigurationEl = userEl.getChild("configuration");
        Configuration oldConfiguration = defaultUser.getConfiguration();
        if (!oldConfigurationEl.getChildren().isEmpty())
            oldConfiguration = getConfiguration(oldConfigurationEl);
        oldConfigurationEl.detach();
        userEl.addContent(2, convertToXMLElement(configuration, oldConfiguration));
    }

    protected Element convertToXMLElement(String name, ImageResource imageResource) {
        Element e = new Element(name);
        if (imageResource == null)
            return e;
        e.addContent(imageResource.path);
        e.setAttribute("resourceType", imageResource.resourceType.name());
        return e;
    }

    protected Element convertToXMLElement(String name, SoundResource soundResource) {
        Element e = new Element(name);
        if (soundResource == null)
            return e;
        e.addContent(soundResource.path);
        e.setAttribute("resourceType", soundResource.resourceType.name());
        return e;
    }

    protected Element convertToXMLElement(Configuration configuration, Configuration oldConfiguration) {
        Element configurationEl = new Element("configuration");
        if (oldConfiguration == null || oldConfiguration.isAltered(configuration)) {
            configurationEl.addContent(new Element("rotationSpeed").setText(configuration.getRotationSpeed() + ""));
            configurationEl.addContent(new Element("defaultGridRow").setText(configuration.getDefaultGridRow() + ""));
            configurationEl.addContent(new Element("defaultGridColumn").setText(configuration.getDefaultGridColumn() + ""));
            configurationEl.addContent(convertToXMLElement("sound", configuration.hasSound()));
            configurationEl.addContent(convertToXMLElement("image", configuration.hasImage()));
            configurationEl.addContent(convertToXMLElement("text", configuration.hasText()));

            Sensor selectionSensor = configuration.getSelectionSensor();
            if (selectionSensor != null) {
                Element selectionSensorEl = new Element("selectionSensor");
                if (selectionSensor instanceof KeyboardSensor) {
                    KeyboardSensor keyboardSensor = (KeyboardSensor) selectionSensor;
                    selectionSensorEl.addContent(new Element("type").setText(keyboardSensor.getName()));
                    selectionSensorEl.addContent(new Element("keyCode").setText(keyboardSensor.getKeyCode() + ""));
                    selectionSensorEl.addContent(new Element("keyChar").setText(keyboardSensor.getKeyChar() + ""));
                } else if (selectionSensor instanceof MouseSensor) {
                    MouseSensor mouseSensor = (MouseSensor) selectionSensor;
                    selectionSensorEl.addContent(new Element("type").setText(mouseSensor.getName()));
                    selectionSensorEl.addContent(new Element("button").setText(mouseSensor.getButton() + ""));
                    selectionSensorEl.addContent(new Element("clickCount").setText(mouseSensor.getClickCount() + ""));
                }
                configurationEl.addContent(selectionSensorEl);
            }

            Sensor navigationSensor = configuration.getNavigationSensor();
            if (navigationSensor != null) {
                Element navigationSensorEl = new Element("navigationSensor");
                if (navigationSensor instanceof KeyboardSensor) {
                    KeyboardSensor keyboardSensor = (KeyboardSensor) navigationSensor;
                    navigationSensorEl.addContent(new Element("type").setText(keyboardSensor.getName()));
                    navigationSensorEl.addContent(new Element("keyCode").setText(keyboardSensor.getKeyCode() + ""));
                    navigationSensorEl.addContent(new Element("keyChar").setText(keyboardSensor.getKeyChar() + ""));
                } else if (navigationSensor instanceof MouseSensor) {
                    MouseSensor mouseSensor = (MouseSensor) navigationSensor;
                    navigationSensorEl.addContent(new Element("type").setText(mouseSensor.getName()));
                    navigationSensorEl.addContent(new Element("button").setText(mouseSensor.getButton() + ""));
                    navigationSensorEl.addContent(new Element("clickCount").setText(mouseSensor.getClickCount() + ""));
                }
                configurationEl.addContent(navigationSensorEl);
            }

        }
        return configurationEl;
    }

    protected Element convertToXMLElement(CommunicationModule communicationModule, CommunicationModule defaultComMod) {
        Element communicationModuleEl = convertToXMLElement("communication", "enabled", communicationModule.isEnabled());

        Element categoriesEl = new Element("categories");
        communicationModuleEl.addContent(categoriesEl);

        for (Category category : communicationModule.getCategories()) {
            String catName = category.getNameUnmodified();
            if (defaultComMod == null || category.isAltered(defaultComMod.getCategory(catName))) {
                Element categoryEl = convertToXMLElement(category, communicationModule.getSupportedLanguages(catName));
                communicationModuleEl.addContent(categoryEl);
            }
        }

        return communicationModuleEl;
    }

    protected Element convertToXMLElement(EntertainmentModule entertainmentModule, EntertainmentModule defaultEntMod) {
        Element entertainmentModuleEl = convertToXMLElement("entertainment", "enabled", entertainmentModule.isEnabled());

        MusicModule musicModule = entertainmentModule.getMusicModule();
        if (defaultEntMod == null || musicModule.isAltered(defaultEntMod.getMusicModule())) {
            Element musicModuleEl = convertToXMLElement("music", "enabled", musicModule.isEnabled());
            musicModuleEl.addContent(new Element("name").setText(musicModule.getNameUnmodified()));
            musicModuleEl.addContent(new Element("image"));
            musicModuleEl.addContent(new Element("sound"));
            musicModuleEl.addContent(new Element("path").setText(musicModule.getFolderPath()));
            musicModuleEl.addContent(new Element("playlistSize").setText(musicModule.getPlaylistSize() + ""));
            entertainmentModuleEl.addContent(musicModuleEl);
        }

        VideoModule videoModule = entertainmentModule.getVideoModule();
        if (defaultEntMod == null || videoModule.isAltered(defaultEntMod.getVideoModule())) {
            Element videoModuleEl = convertToXMLElement("video", "enabled", videoModule.isEnabled());
            videoModuleEl.addContent(new Element("name").setText(videoModule.getNameUnmodified()));
            videoModuleEl.addContent(new Element("image"));
            videoModuleEl.addContent(new Element("sound"));
            videoModuleEl.addContent(new Element("path").setText(videoModule.getFolderPath()));
            videoModuleEl.addContent(new Element("playlistSize").setText(videoModule.getPlaylistSize() + ""));
            entertainmentModuleEl.addContent(videoModuleEl);
        }
        return entertainmentModuleEl;
    }

    protected Element convertToXMLElement(GameModule gameModule, GameModule defaultGameMod) {
        Element gameModuleEl = convertToXMLElement("game", "enabled", gameModule.isEnabled());

        String gameTypeStr = "stimulusReactionGame";
        Element stimulusReactionGamesEl = convertToXMLElement(gameTypeStr + "s", gameModule.getGameType(gameTypeStr), defaultGameMod);
        gameModuleEl.addContent(stimulusReactionGamesEl);

        gameTypeStr = "sequenceGame";
        Element sequenceGamesEl = convertToXMLElement(gameTypeStr + "s", gameModule.getGameType(gameTypeStr), defaultGameMod);
        gameModuleEl.addContent(sequenceGamesEl);

        gameTypeStr = "similarityGame";
        Element similarityGamesEl = convertToXMLElement(gameTypeStr + "s", gameModule.getGameType(gameTypeStr), defaultGameMod);
        gameModuleEl.addContent(similarityGamesEl);

        return gameModuleEl;
    }

    protected Element convertToXMLElement(String gameTypeStr, GameType gameType, GameModule defaultGameMod) {
        Element gameTypeEl = convertToXMLElement(gameTypeStr, "enabled", gameType.isEnabled());
        Element games = new Element("games");
        if (defaultGameMod == null) {
            gameTypeEl.addContent(convertToXMLElement("image", gameType.getImage()));
            gameTypeEl.addContent(convertToXMLElement("sound", gameType.getSound()));
            gameTypeEl.addContent(convertToXMLElement("winSound", gameType.getWinSound()));
            gameTypeEl.addContent(convertToXMLElement("errorSound", gameType.getErrorSound()));
        } else {
            GameType defGameType = defaultGameMod.getGameType(gameTypeStr);

            ImageResource imageResource = gameType.getImage();
            if (imageResource == null || imageResource.isAltered(defGameType.getImage()))
                gameTypeEl.addContent(convertToXMLElement("image", imageResource));
            else
                gameTypeEl.addContent(new Element("image"));

            SoundResource soundResource = gameType.getSound();
            if (soundResource == null || soundResource.isAltered(defGameType.getSound()))
                gameTypeEl.addContent(convertToXMLElement("sound", soundResource));
            else
                gameTypeEl.addContent(new Element("sound"));

            SoundResource winSoundResource = gameType.getWinSound();
            if (winSoundResource == null || winSoundResource.isAltered(defGameType.getWinSound()))
                gameTypeEl.addContent(convertToXMLElement("winSound", winSoundResource));
            else
                gameTypeEl.addContent(new Element("winSound"));

            SoundResource errorSoundResource = gameType.getErrorSound();
            if (errorSoundResource == null || errorSoundResource.isAltered(defGameType.getErrorSound()))
                gameTypeEl.addContent(convertToXMLElement("errorSound", errorSoundResource));
            else
                gameTypeEl.addContent(new Element("errorSound"));
        }

        GameType defGameType =  null;
        if (defaultGameMod != null)
            defGameType = defaultGameMod.getGameType(gameTypeStr);

        for (Game game : gameType.getGames()) {
            String name = game.getNameUnmodified();
            Element gameEl = convertToXMLElement(game, gameTypeStr);
            if (defaultGameMod == null || defGameType.getGame(name) == null || defGameType.getGame(name).isAltered(game))
                games.addContent(gameEl);
        }

        gameTypeEl.addContent(games);
        return gameTypeEl;
    }

    protected Element convertToXMLElement(Category category, String languages) {
        Element categoryEl = convertToXMLElement(category);
        categoryEl.setAttribute("languages", languages);

        List<Category> subCategories = category.getSubCategories();
        if (!subCategories.isEmpty()) {
            Element categories = new Element("categories");
            for (Category subCategory : subCategories) {
                Element subCategoryEl = convertToXMLElement(subCategory);
                categories.addContent(subCategoryEl);
            }
            categoryEl.addContent(categories);
        }
        return categoryEl;
    }

    protected Element convertToXMLElement(Category category) {
        Element categoryEl = new Element("category");
        categoryEl.setAttribute("name", category.getNameUnmodified());
        categoryEl.addContent(convertToXMLElement("image", category.getImage()));
        categoryEl.addContent(convertToXMLElement("sound", category.getSound()));
        categoryEl.addContent(convertToXMLElement("hasSound", category.hasSound()));
        categoryEl.addContent(convertToXMLElement("hasImage", category.hasImage()));
        categoryEl.addContent(convertToXMLElement("hasText", category.hasText()));
        return categoryEl;
    }

    protected Element convertToXMLElement(Game game, String gameTypeStr) {
        Element gameEl = convertToXMLElement("game", "name", game.getNameUnmodified(), "enabled", game.isEnabled());
        gameEl.addContent(convertToXMLElement("image", game.getImage()));
        String difficulty = "";
        switch (gameTypeStr) {
            case "stimulusReactionGame":
                difficulty = ((StimulusReactionGame) game).getDifficulty() + "";
                break;
            case "sequenceGame":
                difficulty = ((SequenceGame) game).getDifficulty() + "";
                break;
            case "similarityGame":
                difficulty = ((SimilarityGame) game).getDifficulty() + "";
                break;
            default:
        }
        gameEl.addContent(new Element("difficulty").setText(difficulty));
        gameEl.addContent(convertToXMLElement("winSound", game.getWinSound()));
        gameEl.addContent(convertToXMLElement("errorSound", game.getErrorSound()));

        Element gameImagesEl = new Element("gameImages");
        for (GameImage gameImage : game.getImages()) {
            Element imageEl = convertToXMLElement("image", "enabled", gameImage.isEnabled());
            imageEl.addContent(new Element("name").setText(gameImage.getName()));
            imageEl.addContent(convertToXMLElement("path", gameImage.getImage()));
            imageEl.addContent(new Element("order").setText(gameImage.getOrder() + ""));
            gameImagesEl.addContent(imageEl);
        }
        gameEl.addContent(gameImagesEl);

        return gameEl;
    }

    protected Element convertToXMLElement(String name, boolean value) {
        if (value)
            return new Element(name).setText("true");
        else
            return new Element(name).setText("false");
    }

    protected Element convertToXMLElement(String name, String attributeName, boolean value) {
        if (value)
            return new Element(name).setAttribute(attributeName, "true");
        else
            return new Element(name).setAttribute(attributeName, "false");
    }

    protected Element convertToXMLElement(String name, String attributeName1, String value1, String attributeName2, boolean value2) {
        Element e = new Element(name);
        e.setAttribute(attributeName1, value1);
        if (value2)
            return e.setAttribute(attributeName2, "true");
        else
            return e.setAttribute(attributeName2, "false");
    }

    public boolean exportUserToFile(String name, File file) {
        Element profile = new Element("profile");
        User user = getUser(name);
        profile.addContent(new Element("name").setText(name));
        profile.addContent(convertToXMLElement("image", user.getImage()));
        profile.addContent(convertToXMLElement(user.getConfiguration(), null));
        profile.addContent(convertToXMLElement(user.getCommunicationModule(), null));
        profile.addContent(convertToXMLElement(user.getEntertainmentModule(), null));
        profile.addContent(convertToXMLElement(user.getGameModule(), null));
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.setFormat(Format.getPrettyFormat());
            xmlout.output(profile, osw);
            osw.close();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(UserFormPanel.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex);
            return false;
        }
        return true;
    }
}
