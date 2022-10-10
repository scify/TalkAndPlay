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
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.scify.talkandplay.gui.MainFrame;
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
 */
public class XMLConfigurationHandler {

    protected File dataDir;
    protected File localConfFile;
    protected File globalConfFile;
    //protected String defaultUserConfigurationFilePath;
    protected Document configurationXmlDocument;

    protected List<User> users;

    protected CommunicationModule downloadedCommunicationModule;
    protected File downloadedCommunicationCardsFile;

    protected List<Game> downloadedStimulusReactionGames;
    protected List<Game> downloadedSequenceGames;
    protected List<Game> downloadedSimilarityGames;
    protected File downloadedGameCardsFile;

    //for each user we add his files in a hashmap
    protected Map<String, List<MultimediaResource>> userFiles;
    //contains all the image and sound paths that were found in the xml configuration.
    //we use this to check if all the paths exist on hard drive.
    protected List<MultimediaResource> imageAndSoundResources;
    // a hidden user that could be used to create new users
    protected User defaultUser;
    Properties properties;
    static Logger logger = Logger.getLogger(XMLConfigurationHandler.class);
    protected ResourceManager rm;

    public XMLConfigurationHandler(File dataDir) {
        this.dataDir = dataDir;
        downloadedStimulusReactionGames = new ArrayList<>();
        downloadedSequenceGames = new ArrayList<>();
        downloadedSimilarityGames = new ArrayList<>();
        downloadedCommunicationCardsFile = new File(dataDir, "communicationCards");
        if (!downloadedCommunicationCardsFile.exists())
            downloadedCommunicationCardsFile.mkdir();

        downloadedGameCardsFile = new File(dataDir, "gameCards");
        if (!downloadedGameCardsFile.exists())
            downloadedGameCardsFile.mkdir();

        properties = Properties.getInstance();
        localConfFile = new File(dataDir, "conf.xml");
        globalConfFile = new File(properties.getApplicationFolder() + File.separator + "conf.xml");
        rm = ResourceManager.getInstance();
        initConfigurationFile();
        try {
            loadDownloadedCommunicationCards();
        } catch (Exception e) {
            logger.error(e);
            Sentry.capture(e.getMessage());
        }
        try {
            loadDownloadedGameCards();
        } catch (Exception e) {
            logger.error(e);
            Sentry.capture(e.getMessage());
        }

    }

    private void initConfigurationFile() {
        try {
            double versionOfGlobalConf = getConfVersion(globalConfFile);
            logger.info("Searching for conf.xml at: " + localConfFile.getAbsolutePath());
            if (!localConfFile.exists()) {
                logger.info("Local conf.xml file not found!");
                createLocalConfigurationFile();
            } else {
                logger.info("Loading version of: " + localConfFile.getAbsolutePath());
                double versionOfLocalConf = getConfVersion(localConfFile.getAbsoluteFile());
                logger.info("Global version: " + versionOfGlobalConf);
                logger.info("Local version: " + versionOfLocalConf);
                if (versionOfLocalConf == versionOfGlobalConf) {
                    logger.info("Versions match, loading: " + localConfFile.getAbsolutePath());
                } else {
                    logger.info("Versions differ creating a new local conf.xml");
                    createLocalConfigurationFile();
                }
            }
            if (parseConfigurationFileXML(true))
                writeToXmlFile();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Sentry.capture(e.getMessage());
        }
    }

    private void createLocalConfigurationFile() throws IOException {
        logger.info("Copying default configuration file from: " + globalConfFile.getAbsolutePath() + " to: " + localConfFile.getAbsolutePath());
        FileUtils.copyFile(globalConfFile, localConfFile);
    }

    public User getDefaultUser() {
        return defaultUser;
    }

    public List<User> getUsers(String usersOfAccount) {
        List<User> ret = new ArrayList<>();
        for (User user: users) {
            if (user.getUserOfAccount().equals(usersOfAccount))
                ret.add(user);
        }
        return ret;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name))
                return user;
        }
        return null;
    }

    protected double getConfVersion(File file) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document xmlDoc = builder.build(file);
        Element root = xmlDoc.getRootElement();
        String version = root.getChild("defaultProfile").getAttributeValue("version");
        return Double.parseDouble(version);
    }

    /**
     * Parse the XML file that holds all users' configuration
     *
     * @return
     * @throws Exception
     */
    private boolean parseConfigurationFileXML(boolean fromInit) throws Exception {

        boolean needsSave = false;

        SAXBuilder builder = new SAXBuilder();
        configurationXmlDocument = builder.build(localConfFile);
        userFiles = new HashMap();
        users = new ArrayList();

        Element root = configurationXmlDocument.getRootElement();
        this.defaultUser = extractUserFromXml(root.getChild("defaultProfile"), true);

        Element userProfilesEl = root.getChild("userProfiles");
        List<Element> usersEl = userProfilesEl.getChildren();
        if (usersEl.isEmpty() && fromInit) {
            Element newUser = createNewProfile(defaultUser.getName(), "");
            userProfilesEl.addContent(newUser);
            needsSave = true;
        }
        imageAndSoundResources = new ArrayList();
        for (int i = 0; i < usersEl.size(); i++) {

            User user = extractUserFromXml(usersEl.get(i), false);
            users.add(user);
            List<MultimediaResource> userImageAndSoundResources = userFiles.get(user.getName());
            for (MultimediaResource resource: userImageAndSoundResources) {
                imageAndSoundResources.add(resource);
            }
        }

        return needsSave;
    }

    protected User extractUserFromXml(Element userEl, boolean isDefault) {

        Attribute userOfAccount = userEl.getAttribute("userOfAccount");


        ArrayList<MultimediaResource> imageAndSoundResources = new ArrayList();

        String userName = userEl.getChildText("name");
        ImageResource imageResource = extractImageResource(userEl.getChild("image"));

        User user = new User(userName, imageResource);
        if (userOfAccount != null)
            user.setUserOfAccount(userOfAccount.getValue());

        Element confEl = userEl.getChild("configuration");
        if (confEl.getChildren().size() > 0)
            user.setConfiguration(extractConfiguration(confEl));
        else
            user.setConfiguration(new Configuration(defaultUser.getConfiguration()));

        Element communication = userEl.getChild("communication");
        user.setCommunicationModule(extractCommunicationModule(imageAndSoundResources, communication, isDefault));
        user.getCommunicationModule().setRows(user.getConfiguration().getDefaultGridRow());
        user.getCommunicationModule().setColumns(user.getConfiguration().getDefaultGridColumn());


        Element entertainment = userEl.getChild("entertainment");
        user.setEntertainmentModule(extractEntertainmentModule(entertainment, isDefault));


        Element games = userEl.getChild("games");
        user.setGameModule(extractGameModule(games, isDefault));
        if (!isDefault)
            userFiles.put(user.getName(), imageAndSoundResources);
        return user;
    }

    protected Configuration extractConfiguration(Element configurationNode) {
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

    protected void loadDownloadedCommunicationCards() throws Exception {
        downloadedCommunicationModule = new CommunicationModule();
        //extract all zip files
        for (File communicationCardFile: downloadedCommunicationCardsFile.listFiles()) {
            if (communicationCardFile.isFile() && communicationCardFile.getName().endsWith(".zip")) {
                new ZipFile(communicationCardFile).extractAll(communicationCardFile.getParent());
            }
        }
        //load all folders
        for (File communicationCardFolder: downloadedCommunicationCardsFile.listFiles()) {
            if (communicationCardFolder.isDirectory()) {
                File structureFile = new File(communicationCardFolder, "structure.xml");
                SAXBuilder builder = new SAXBuilder();
                Element root = builder.build(structureFile).getRootElement();
                String catName = root.getAttributeValue("name");
                Category category = new Category(catName);
                category.setEnabled("true".equals(root.getAttributeValue("enabled")));
                String supportedLanguages = root.getAttributeValue("languages");
                HashSet<String> languages = new HashSet<>();
                languages.add(supportedLanguages);
                downloadedCommunicationModule.addCategory(category, languages);

                String image = root.getChild("image").getValue();
                File imageFile = new File (communicationCardFolder, image);
                category.setImage(new ImageResource(imageFile.getAbsolutePath(),ResourceType.LOCAL));

                String sound = root.getChild("sound").getValue();
                File soundFile = new File (communicationCardFolder, sound);
                category.setSound(new SoundResource(soundFile.getAbsolutePath(), ResourceType.LOCAL));

                Element subCategoriesEl = root.getChild("categories");
                if (subCategoriesEl != null) {
                    List<Element> subCategoriesChildren = subCategoriesEl.getChildren();
                    List<Category> subCategoriesList = new ArrayList<>();
                    for (Element subCategoryEl : subCategoriesChildren) {
                        String categoryName = subCategoryEl.getAttributeValue("name");
                        Category subCategory = new Category(categoryName);
                        subCategory.setEnabled("true".equals(subCategoryEl.getAttributeValue("enabled")));
                        subCategory.setParentCategory(category);

                        image = subCategoryEl.getChild("image").getValue();
                        imageFile = new File (communicationCardFolder, image);
                        subCategory.setImage(new ImageResource(imageFile.getAbsolutePath(), ResourceType.LOCAL));


                        sound = subCategoryEl.getChild("sound").getValue();
                        soundFile = new File(communicationCardFolder, sound);
                        subCategory.setSound(new SoundResource(soundFile.getAbsolutePath(), ResourceType.LOCAL));
                        subCategoriesList.add(subCategory);
                    }
                    category.setSubCategories(subCategoriesList);
                }
            }
        }

    }

    protected CommunicationModule extractCommunicationModule(List<MultimediaResource> imageAndSoundResources, Element communicationNode, boolean isDefault) {
        CommunicationModule communicationModule = new CommunicationModule();

        if (isDefault)
            communicationModule.setName(communicationNode.getChildText("name"));
        else
            communicationModule.setName(defaultUser.getCommunicationModule().getNameUnmodified());

        communicationModule.setEnabled("true".equals(communicationNode.getAttributeValue("enabled")));

        ImageResource imageResource = extractImageResource(communicationNode.getChild("image"));
        if (imageResource == null)
            imageResource = new ImageResource(defaultUser.getCommunicationModule().getImageResource());
        communicationModule.setImage(imageResource);


        SoundResource soundResource = extractSoundResource(communicationNode.getChild("sound"));
        if (soundResource == null)
            soundResource = new SoundResource(defaultUser.getCommunicationModule().getSoundResource());
        communicationModule.setSound(soundResource);

        Element categoriesEl = communicationNode.getChild("categories");
        extractCommunicationCategories(communicationModule, imageAndSoundResources, categoriesEl);

        return communicationModule;
    }

    protected void extractCommunicationCategories(CommunicationModule communicationModule, List<MultimediaResource> imageAndSoundResources, Element categoriesNode) {
        List<Element> categories = categoriesNode.getChildren();

        for (Element categoryEl : categories) {
            String categoryName = categoryEl.getAttributeValue("name");

            if (categoryEl.getChildren().isEmpty()) {//use default
                Category category = new Category(defaultUser.getCommunicationModule().getCategory(categoryName));
                HashSet<String> languages = defaultUser.getCommunicationModule().getSupportedLanguages(categoryName);
                category.setEnabled("true".equals(categoryEl.getAttributeValue("enabled")));
                communicationModule.addCategory(category, languages);
            } else {//add new Category
                Category category = new Category(categoryName);
                category.setEnabled("true".equals(categoryEl.getAttributeValue("enabled")));
                String supportedLanguages = categoryEl.getAttributeValue("languages");
                HashSet<String> languages = new HashSet<>();
                for (String language : supportedLanguages.trim().split(" "))
                    languages.add(language);
                communicationModule.addCategory(category, languages);

                ImageResource image = extractImageResource(categoryEl.getChild("image"));
                category.setImage(image);
                imageAndSoundResources.add(image);

                SoundResource sound = extractSoundResource(categoryEl.getChild("sound"));
                category.setSound(sound);
                imageAndSoundResources.add(sound);

                List<Category> subCategories = new ArrayList<>();
                category.setSubCategories(subCategories);
                Element subCategoriesEl = categoryEl.getChild("categories");
                if (subCategoriesEl != null) {
                    List<Element> subCategoriesChildren = subCategoriesEl.getChildren();
                    List<Category> subCategoriesList = new ArrayList<>();
                    for (Element subCategoryEl : subCategoriesChildren) {
                        categoryName = subCategoryEl.getAttributeValue("name");
                        Category subCategory = new Category(categoryName);
                        subCategory.setEnabled("true".equals(subCategoryEl.getAttributeValue("enabled")));
                        subCategory.setParentCategory(category);

                        ImageResource subCatImage = extractImageResource(subCategoryEl.getChild("image"));
                        subCategory.setImage(subCatImage);
                        imageAndSoundResources.add(subCatImage);

                        SoundResource subCatSound = extractSoundResource(subCategoryEl.getChild("sound"));
                        subCategory.setSound(subCatSound);
                        imageAndSoundResources.add(subCatSound);

                        subCategoriesList.add(subCategory);
                    }
                    category.setSubCategories(subCategoriesList);
                }
            }
        }
    }

    protected EntertainmentModule extractEntertainmentModule(Element entertainmentNode, boolean isDefault) {

        EntertainmentModule entertainmentModule = new EntertainmentModule();

        if (isDefault)
            entertainmentModule.setName(entertainmentNode.getChildText("name"));
        else
            entertainmentModule.setName(defaultUser.getEntertainmentModule().getNameUnmodified());

        entertainmentModule.setEnabled("true".equals(entertainmentNode.getAttributeValue("enabled")));

        ImageResource image = extractImageResource(entertainmentNode.getChild("image"));
        if (image == null)
            image = new ImageResource(defaultUser.getEntertainmentModule().getImageResource());
        entertainmentModule.setImage(image);

        SoundResource sound = extractSoundResource(entertainmentNode.getChild("sound"));
        if (sound == null)
            sound = new SoundResource(defaultUser.getEntertainmentModule().getSoundResource());
        entertainmentModule.setSound(sound);


        //set the music module
        Element musicNode = entertainmentNode.getChild("music");
        if (musicNode == null) {
            entertainmentModule.setMusicModule(new MusicModule(defaultUser.getEntertainmentModule().getMusicModule()));
        } else {
            MusicModule musicModule = new MusicModule();
            Element musicNameEl = musicNode.getChild("name");
            if (musicNameEl == null)
                musicModule.setName(defaultUser.getEntertainmentModule().getMusicModule().getNameUnmodified());
            else
                musicModule.setName(musicNameEl.getText());
            musicModule.setFolderPath(musicNode.getChildText("path"));
            musicModule.setPlaylistSize(Integer.parseInt(musicNode.getChildText("playlistSize")));
            musicModule.setEnabled("true".equals(musicNode.getAttributeValue("enabled")));

            ImageResource musicImage = extractImageResource(musicNode.getChild("image"));
            if (musicImage == null)
                musicImage = new ImageResource(defaultUser.getEntertainmentModule().getMusicModule().getImageResource());
            musicModule.setImage(musicImage);

            SoundResource musicSound = extractSoundResource(musicNode.getChild("sound"));
            if (musicSound == null)
                musicSound = new SoundResource(defaultUser.getEntertainmentModule().getMusicModule().getSoundResource());
            musicModule.setSound(musicSound);

            entertainmentModule.setMusicModule(musicModule);
        }

        //set the video module
        Element videoNode = entertainmentNode.getChild("video");
        if (videoNode == null) {
            entertainmentModule.setVideoModule(new VideoModule(defaultUser.getEntertainmentModule().getVideoModule()));
        } else {
            VideoModule videoModule = new VideoModule();
            Element videoNameEl = videoNode.getChild("name");
            if (videoNameEl == null)
                videoModule.setName(defaultUser.getEntertainmentModule().getVideoModule().getNameUnmodified());
            else
                videoModule.setName(videoNameEl.getText());
            videoModule.setFolderPath(videoNode.getChildText("path"));
            videoModule.setPlaylistSize(Integer.parseInt(videoNode.getChildText("playlistSize")));
            videoModule.setEnabled("true".equals(videoNode.getAttributeValue("enabled")));

            ImageResource videoImage = extractImageResource(videoNode.getChild("image"));
            if (videoImage == null)
                videoImage = new ImageResource(defaultUser.getEntertainmentModule().getVideoModule().getImageResource());
            videoModule.setImage(videoImage);

            SoundResource videoSound = extractSoundResource(videoNode.getChild("sound"));
            if (videoSound == null)
                videoSound = new SoundResource(defaultUser.getEntertainmentModule().getVideoModule().getSoundResource());
            videoModule.setSound(videoSound);

            entertainmentModule.setVideoModule(videoModule);
        }

        return entertainmentModule;
    }

    protected void loadDownloadedGameCards() throws Exception {
        downloadedStimulusReactionGames.clear();
        downloadedSequenceGames.clear();
        downloadedSimilarityGames.clear();
        //extract all zip files
        for (File gameCardFile: downloadedGameCardsFile.listFiles()) {
            if (gameCardFile.isFile() && gameCardFile.getName().endsWith(".zip")) {
                new ZipFile(gameCardFile).extractAll(gameCardFile.getParent());
            }
        }
        //load all folders
        for (File gameCardFolder: downloadedGameCardsFile.listFiles()) {
            if (gameCardFolder.isDirectory()) {
                File structureFile = new File(gameCardFolder, "structure.xml");
                SAXBuilder builder = new SAXBuilder();
                Element root = builder.build(structureFile).getRootElement();

                String gameName = root.getAttributeValue("name");
                boolean enabled = "true".equals(root.getAttributeValue("enabled"));
                String gameType = root.getAttributeValue("gameType");

                Game game;
                GameCollection gameCollection;
                if (gameType.equals("stimulusReactionGame")) {
                    int difficulty = Integer.parseInt(root.getChildText("difficulty"));
                    game = new StimulusReactionGame(gameName, enabled, difficulty);
                    gameCollection = defaultUser.getGameModule().getGameCollection("stimulusReactionGame");
                    downloadedStimulusReactionGames.add(game);
                } else if (gameType.equals("sequenceGame")) {
                    int difficulty = Integer.parseInt(root.getChildText("difficulty"));
                    game = new SequenceGame(gameName, enabled, difficulty);
                    gameCollection = defaultUser.getGameModule().getGameCollection("sequenceGame");
                    downloadedSequenceGames.add(game);
                } else {
                    int difficulty = Integer.parseInt(root.getChildText("difficulty"));
                    game = new SimilarityGame(gameName, enabled, difficulty);
                    gameCollection = defaultUser.getGameModule().getGameCollection("similarityGame");
                    downloadedSimilarityGames.add(game);
                }

                Element imageEl = root.getChild("image");
                if (imageEl == null)
                    game.setImage(gameCollection.getImage());
                else {
                    String fullPath = new File(gameCardFolder, imageEl.getValue()).getAbsolutePath();
                    game.setImage(new ImageResource(fullPath, ResourceType.LOCAL));
                }

                Element soundEl = root.getChild("sound");
                if (soundEl == null)
                    game.setSound(gameCollection.getSound());
                else {
                    String fullPath = new File(gameCardFolder, soundEl.getValue()).getAbsolutePath();
                    game.setSound(new SoundResource(fullPath, ResourceType.LOCAL));
                }

                Element winSoundEl = root.getChild("winSound");
                if (winSoundEl == null)
                    game.setWinSound(gameCollection.getWinSound());
                else {
                    String fullPath = new File(gameCardFolder, winSoundEl.getValue()).getAbsolutePath();
                    game.setWinSound(new SoundResource(fullPath, ResourceType.LOCAL));
                }

                Element errorSoundEl = root.getChild("errorSoundEl");
                if (errorSoundEl == null)
                    game.setErrorSound(gameCollection.getErrorSound());
                else {
                    String fullPath = new File(gameCardFolder, errorSoundEl.getValue()).getAbsolutePath();
                    game.setErrorSound(new SoundResource(fullPath, ResourceType.LOCAL));
                }

                List<Element> gameImages = root.getChild("gameImages").getChildren();
                for (Element gameImage : gameImages)
                    game.getImages().add(extractGameImage(gameImage, gameCardFolder));

            }
        }

    }

    protected GameModule extractGameModule(Element gameNode, boolean isDefault) {
        GameModule gameModule = new GameModule();

        if (isDefault)
            gameModule.setName(gameNode.getChildText("name"));
        else
            gameModule.setName(defaultUser.getGameModule().getNameUnmodified());

        gameModule.setEnabled("true".equals(gameNode.getAttributeValue("enabled")));

        ImageResource image = extractImageResource(gameNode.getChild("image"));
        if (image == null)
            image = new ImageResource(defaultUser.getGameModule().getImageResource());
        gameModule.setImage(image);

        SoundResource sound = extractSoundResource(gameNode.getChild("sound"));
        if (sound == null)
            sound = new SoundResource(defaultUser.getGameModule().getSoundResource());
        gameModule.setSound(sound);

        //set the stimulus reaction games
        Element stimulusReactionGamesNode = gameNode.getChild("stimulusReactionGames");
        if (stimulusReactionGamesNode != null) {
            boolean enabled = "true".equals(stimulusReactionGamesNode.getAttributeValue("enabled"));

            String stimulusName = stimulusReactionGamesNode.getChildText("name");
            GameCollection stimulusReactionType;
            if (isDefault) {
                stimulusReactionType = new GameCollection(stimulusName, enabled, "stimulusReactionGame");
                extractGameCollectionFields(stimulusReactionType, stimulusReactionGamesNode, null);
            } else {
                GameCollection defaultGameCollection = defaultUser.getGameModule().getGameCollection("stimulusReactionGame");
                stimulusReactionType = new GameCollection(defaultGameCollection.getNameUnmodified(), enabled, "stimulusReactionGame");
                extractGameCollectionFields(stimulusReactionType, stimulusReactionGamesNode, defaultGameCollection);
            }

            extractGames(stimulusReactionType, stimulusReactionGamesNode);
            gameModule.getGameTypes().add(stimulusReactionType);
        }
        //set the sequence games
        Element sequenceGamesNode = gameNode.getChild("sequenceGames");
        if (sequenceGamesNode != null) {
            boolean enabled = "true".equals(sequenceGamesNode.getChildText("enabled"));

            String sequenceGamesName = sequenceGamesNode.getChildText("name");
            GameCollection sequenceGameCollection;
            if (isDefault) {
                sequenceGameCollection = new GameCollection(sequenceGamesName, enabled, "sequenceGame");
                extractGameCollectionFields(sequenceGameCollection, sequenceGamesNode, null);
            } else {
                GameCollection defaultGameCollection = defaultUser.getGameModule().getGameCollection("sequenceGame");
                sequenceGameCollection = new GameCollection(defaultGameCollection.getNameUnmodified(), enabled, "sequenceGame");
                extractGameCollectionFields(sequenceGameCollection, sequenceGamesNode, defaultGameCollection);
            }

            extractGames(sequenceGameCollection, sequenceGamesNode);
            gameModule.getGameTypes().add(sequenceGameCollection);
        }

        //set the similar games
        Element similarGamesNode = gameNode.getChild("similarityGames");
        if (similarGamesNode != null) {
            boolean enabled = "true".equals(similarGamesNode.getChildText("enabled"));

            String similarGamesName = similarGamesNode.getChildText("name");
            GameCollection similarityGameCollection;
            if (isDefault) {
                similarityGameCollection = new GameCollection(similarGamesName, enabled, "similarityGame");
                extractGameCollectionFields(similarityGameCollection, similarGamesNode, null);
            } else {
                GameCollection defaultGameCollection = defaultUser.getGameModule().getGameCollection("similarityGame");
                similarityGameCollection = new GameCollection(defaultGameCollection.getNameUnmodified(), enabled, "similarityGame");
                extractGameCollectionFields(similarityGameCollection, similarGamesNode, defaultGameCollection);
            }
            extractGames(similarityGameCollection, similarGamesNode);
            gameModule.getGameTypes().add(similarityGameCollection);
        }

        return gameModule;
    }

    protected void extractGameCollectionFields(GameCollection gameCollection, Element parentNode, GameCollection defGameCollection) {
        Element imageEl = parentNode.getChild("image");
        ImageResource image = extractImageResource(imageEl);
        if (image == null)
            image = new ImageResource(defGameCollection.getImage());
        gameCollection.setImage(image);

        Element soundEl = parentNode.getChild("sound");
        SoundResource sound = extractSoundResource(soundEl);
        if (sound == null)
            sound = new SoundResource(defGameCollection.getSound());
        gameCollection.setSound(sound);

        Element soundWinEl = parentNode.getChild("winSound");
        SoundResource soundWin = extractSoundResource(soundWinEl);
        if (soundWin == null)
            soundWin = new SoundResource(defGameCollection.getWinSound());
        gameCollection.setWinSound(soundWin);

        Element soundErrorEl = parentNode.getChild("errorSound");
        SoundResource soundError = extractSoundResource(soundErrorEl);
        if (soundError == null)
            soundError = new SoundResource(defGameCollection.getErrorSound());
        gameCollection.setErrorSound(soundError);


    }

    protected void extractGames(GameCollection gameCollection, Element parentNode) {
        Element gamesEl = parentNode.getChild("games");
        if (gamesEl != null) {
            List<Element> gamesList = gamesEl.getChildren();
            for (Element gameEl : gamesList) {
                String name = gameEl.getAttributeValue("name");
                String enabled = gameEl.getAttributeValue("enabled");
                //check if Game has altered data or default should be used
                if (gameEl.getChildren().isEmpty()) {

                    Game defGame = defaultUser.getGameModule().getGameCollection(gameCollection.getGameType()).getGame(name);
                    Game game = null;

                    switch (gameCollection.getGameType()) {
                        case "stimulusReactionGame":
                            game = new StimulusReactionGame((StimulusReactionGame) defGame);
                            break;
                        case "sequenceGame":
                            game = new SequenceGame((SequenceGame) defGame);
                            break;
                        case "similarityGame":
                            game = new SimilarityGame((SimilarityGame) defGame);
                            break;
                        default:
                    }

                    if ("true".equals(enabled))
                        game.setEnabled(true);
                    else
                        game.setEnabled(false);
                    gameCollection.addGame(game);
                } else {
                    int difficulty = Integer.parseInt(gameEl.getChildText("difficulty"));

                    Game game = null;
                    switch (gameCollection.getGameType()) {
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

                    Element imageEl = gameEl.getChild("image");
                    ImageResource imageResource = extractImageResource(imageEl);
                    if (imageResource == null)
                        imageResource = new ImageResource(gameCollection.getImage());
                    game.setImage(imageResource);

                    Element soundEl = gameEl.getChild("sound");
                    SoundResource soundResource = extractSoundResource(soundEl);
                    if (soundResource == null)
                        soundResource = new SoundResource(gameCollection.getSound());
                    game.setSound(soundResource);

                    Element winSoundEl = gameEl.getChild("winSound");
                    SoundResource winSoundResource = extractSoundResource(winSoundEl);
                    if (winSoundResource == null)
                        winSoundResource = new SoundResource(gameCollection.getWinSound());
                    game.setWinSound(winSoundResource);

                    Element errorSoundEl = gameEl.getChild("errorSoundEl");
                    SoundResource errorSoundResource = extractSoundResource(errorSoundEl);
                    if (errorSoundResource == null)
                        errorSoundResource = new SoundResource(gameCollection.getErrorSound());
                    game.setErrorSound(errorSoundResource);


                    List<Element> gameImages = gameEl.getChild("gameImages").getChildren();
                    for (Element gameImage : gameImages)
                        game.getImages().add(extractGameImage(gameImage));

                    gameCollection.addGame(game);
                }
            }
        }
    }

    protected GameImage extractGameImage(Element element) {
        boolean enabled = "true".equals(element.getAttributeValue("enabled"));
        int order = Integer.parseInt(element.getAttributeValue("order"));
        ResourceType imageResourceType = ResourceType.valueOf(element.getAttributeValue("resourceType"));
        ImageResource imageResource = new ImageResource(element.getText(), imageResourceType);
        return new GameImage(imageResource, enabled, order);
    }

    protected GameImage extractGameImage(Element element, File pathToDownloadedGame) {
        boolean enabled = "true".equals(element.getAttributeValue("enabled"));
        int order = Integer.parseInt(element.getAttributeValue("order"));
        String fullPath = new File(pathToDownloadedGame, element.getText()).getAbsolutePath();
        ImageResource imageResource = new ImageResource(fullPath, ResourceType.LOCAL);
        return new GameImage(imageResource, enabled, order);
    }

    protected ImageResource extractImageResource(Element imageEl) {
        if (imageEl == null)
            return null;
        String imagePath = imageEl.getText();
        if (imagePath.isEmpty())
            return null;
        ResourceType imageResourceType = ResourceType.valueOf(imageEl.getAttributeValue("resourceType"));
        return new ImageResource(imagePath, imageResourceType);
    }

    protected SoundResource extractSoundResource(Element soundEl) {
        if (soundEl == null)
            return null;
        String soundPath = soundEl.getText();
        if (soundPath.isEmpty())
            return null;
        ResourceType resourceType = ResourceType.valueOf(soundEl.getAttributeValue("resourceType"));
        return new SoundResource(soundPath, resourceType);
    }

    /**
     * For a certain user, check that all the files exist (in case the files
     * must be configured again)
     *
     * @param username
     * @return
     */
    public boolean hasBrokenFiles(String username) {
        for (Map.Entry<String, List<MultimediaResource>> entry : userFiles.entrySet()) {
            if (entry.getKey().equals(username)) {
                for (MultimediaResource resource : entry.getValue()) {
                    if (resource == null)
                        return true;
                    File file = rm.getFileOfResource(resource.getPath(), resource.getResourceType());
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

        for (Map.Entry<String, List<MultimediaResource>> entry : userFiles.entrySet()) {
            if (entry.getKey().equals(username)) {
                for (MultimediaResource resource : entry.getValue()) {
                    if (resource != null) {
                        File file = rm.getFileOfResource(resource.getPath(), resource.getResourceType());
                        if (file == null || !file.isFile()) {
                            brokenFiles.add(resource.getPath());
                        }
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
                new FileOutputStream(localConfFile), "UTF-8"));
    }

    protected Element createNewProfile(String name, String userOfAccount) {
        Element profile = new Element("profile");
        if (!userOfAccount.equals(""))
            profile.setAttribute("userOfAccount", userOfAccount);
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
        Element categories = new Element("categories");
        CommunicationModule defComModule = defaultUser.getCommunicationModule();
        for (Category category : defComModule.getCategories()) {
            String catName = category.getNameUnmodified();
            Element categoryEl = convertToXMLElement("category", "name", catName, "enabled", category.isEnabled());
            categories.addContent(categoryEl);
        }
        communication.addContent(categories);
        profile.addContent(communication);

        Element entertainment = new Element("entertainment").setAttribute("enabled", "true");

        profile.addContent(entertainment);

        Element games = new Element("games").setAttribute("enabled", "true");

        games.addContent(createDefaultGameCollectionElement("stimulusReactionGames", defaultUser.getGameModule().getGameCollection("stimulusReactionGame")));
        games.addContent(createDefaultGameCollectionElement("sequenceGames", defaultUser.getGameModule().getGameCollection("sequenceGame")));
        games.addContent(createDefaultGameCollectionElement("similarityGames", defaultUser.getGameModule().getGameCollection("similarityGame")));

        profile.addContent(games);

        return profile;
    }

    protected Element createDefaultGameCollectionElement(String name, GameCollection defGameCollection) {
        Element gameColEl = convertToXMLElement(name, "enabled", true);
        gameColEl.addContent(new Element("image"));
        gameColEl.addContent(new Element("sound"));
        gameColEl.addContent(new Element("winSound"));
        gameColEl.addContent(new Element("errorSound"));
        Element gamesEl = new Element("games");
        for (Game game : defGameCollection.getGames()) {
            Element gameEl = convertToXMLElement("game", "name", game.getNameUnmodified(), "enabled", game.isEnabled());
            gamesEl.addContent(gameEl);
        }
        gameColEl.addContent(gamesEl);
        return gameColEl;
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

    public void createNewUser(String name, String userOfAccount) {
        Element profile = createNewProfile(name, userOfAccount);
        Element profiles = configurationXmlDocument.getRootElement().getChild("userProfiles");
        profiles.addContent(profile);
    }

    public User createNewUser(File profileFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document profileXml = builder.build(profileFile);
            Element profile = profileXml.getRootElement();
            return extractUserFromXml(profile, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.capture(ex.getMessage());
            return null;
        }
    }

    public void addUser(User user) {
        Element userProfile = new Element("userProfile");
        userProfile.addContent(new Element("name").setText(user.getName()));
        userProfile.addContent(convertToXMLElement("image", user.getImage()));
        userProfile.addContent(convertToXMLElement(user.getConfiguration(), defaultUser.getConfiguration()));
        userProfile.addContent(convertToXMLElement(user.getCommunicationModule(), defaultUser.getCommunicationModule()));
        userProfile.addContent(convertToXMLElement(user.getEntertainmentModule(), defaultUser.getEntertainmentModule()));
        userProfile.addContent(convertToXMLElement(user.getGameModule(), defaultUser.getGameModule()));
        configurationXmlDocument.getRootElement().getChild("userProfiles").addContent(userProfile);
    }

    public Element getUserElement(String name) {
        List<Element> userProfiles = configurationXmlDocument.getRootElement().getChild("userProfiles").getChildren();
        for (Element userProfile : userProfiles) {
            if (userProfile.getChildText("name").equals(name))
                return userProfile;
        }
        return null;
    }


    protected void attachAllElementsOfUser(Element userEl, User user) {
        userEl.addContent(new Element("name").setText(user.getName()));
        userEl.addContent(convertToXMLElement("image", user.getImage()));
        userEl.addContent(convertToXMLElement(user.getConfiguration(), defaultUser.getConfiguration()));
        userEl.addContent(convertToXMLElement(user.getCommunicationModule(), defaultUser.getCommunicationModule()));
        userEl.addContent(convertToXMLElement(user.getEntertainmentModule(), defaultUser.getEntertainmentModule()));
        userEl.addContent(convertToXMLElement(user.getGameModule(), defaultUser.getGameModule()));
    }

    public void updateUser(User oldUser, User newUser) {
        Element userEl = getUserElement(oldUser.getName());
        oldUser.setName(newUser.getName());
        ImageResource imageResource = newUser.getImage();
        if (imageResource == null)
            imageResource = defaultUser.getImage();
        oldUser.setImage(imageResource);
        oldUser.setConfiguration(newUser.getConfiguration());
        userEl.removeContent();
        attachAllElementsOfUser(userEl, oldUser);
    }

    public void updateUser(User user) {
        Element userEl = getUserElement(user.getName());
        userEl.removeContent();
        attachAllElementsOfUser(userEl, user);
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
        for (Category category : communicationModule.getCategories()) {
            String catName = category.getNameUnmodified();
            if (defaultComMod == null || category.isAltered(defaultComMod.getCategory(catName))) {
                Element categoryEl = convertToXMLElement(category, communicationModule.getSupportedLanguages(catName));
                categoriesEl.addContent(categoryEl);
            } else {
                Element categoryEl = convertToXMLElement("category", "name", catName, "enabled", category.isEnabled());
                categoriesEl.addContent(categoryEl);
            }
        }
        communicationModuleEl.addContent(categoriesEl);

        return communicationModuleEl;
    }

    protected Element addLanguageAttribute(Element categoryEl, HashSet<String> languages) {
        String languagesString = "";
        for (String language : languages) {
            languagesString = languagesString + " " + language;
        }
        categoryEl.setAttribute("languages", languagesString.trim());
        return categoryEl;
    }

    protected Element convertToXMLElement(Category category, HashSet<String> languages) {
        Element categoryEl = convertToXMLElement(category);
        if (category.isEnabled())
            categoryEl.setAttribute("enabled", "true");
        else
            categoryEl.setAttribute("enabled", "false");
        addLanguageAttribute(categoryEl, languages);
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
        Element categoryEl = convertToXMLElement("category", "name", category.getNameUnmodified(), "enabled", category.isEnabled());
        categoryEl.addContent(convertToXMLElement("image", category.getImage()));
        categoryEl.addContent(convertToXMLElement("sound", category.getSound()));
        return categoryEl;
    }

    protected Element convertToXMLElement(EntertainmentModule entertainmentModule, EntertainmentModule defaultEntMod) {
        Element entertainmentModuleEl = convertToXMLElement("entertainment", "enabled", entertainmentModule.isEnabled());

        MusicModule musicModule = entertainmentModule.getMusicModule();
        if (defaultEntMod == null || musicModule.isAltered(defaultEntMod.getMusicModule())) {
            Element musicModuleEl = convertToXMLElement("music", "enabled", musicModule.isEnabled());
            musicModuleEl.addContent(new Element("path").setText(musicModule.getFolderPath()));
            musicModuleEl.addContent(new Element("playlistSize").setText(musicModule.getPlaylistSize() + ""));
            entertainmentModuleEl.addContent(musicModuleEl);
        }

        VideoModule videoModule = entertainmentModule.getVideoModule();
        if (defaultEntMod == null || videoModule.isAltered(defaultEntMod.getVideoModule())) {
            Element videoModuleEl = convertToXMLElement("video", "enabled", videoModule.isEnabled());
            videoModuleEl.addContent(new Element("path").setText(videoModule.getFolderPath()));
            videoModuleEl.addContent(new Element("playlistSize").setText(videoModule.getPlaylistSize() + ""));
            entertainmentModuleEl.addContent(videoModuleEl);
        }
        return entertainmentModuleEl;
    }

    protected Element convertToXMLElement(GameModule gameModule, GameModule defaultGameMod) {
        Element gameModuleEl = convertToXMLElement("games", "enabled", gameModule.isEnabled());

        String gameType = "stimulusReactionGame";
        Element stimulusReactionGamesEl = convertToXMLElement(gameType, gameModule.getGameCollection(gameType), defaultGameMod);
        gameModuleEl.addContent(stimulusReactionGamesEl);

        gameType = "sequenceGame";
        Element sequenceGamesEl = convertToXMLElement(gameType, gameModule.getGameCollection(gameType), defaultGameMod);
        gameModuleEl.addContent(sequenceGamesEl);

        gameType = "similarityGame";
        Element similarityGamesEl = convertToXMLElement(gameType, gameModule.getGameCollection(gameType), defaultGameMod);
        gameModuleEl.addContent(similarityGamesEl);

        return gameModuleEl;
    }

    protected Element convertToXMLElement(String gameType, GameCollection gameCollection, GameModule defaultGameMod) {
        Element gameCollectionEl = convertToXMLElement(gameType + "s", "enabled", gameCollection.isEnabled());
        Element gamesEl = new Element("games");
        if (defaultGameMod == null) {
            gameCollectionEl.addContent(convertToXMLElement("image", gameCollection.getImage()));
            gameCollectionEl.addContent(convertToXMLElement("sound", gameCollection.getSound()));
            gameCollectionEl.addContent(convertToXMLElement("winSound", gameCollection.getWinSound()));
            gameCollectionEl.addContent(convertToXMLElement("errorSound", gameCollection.getErrorSound()));
        } else {
            GameCollection defGameCollection = defaultGameMod.getGameCollection(gameType);

            ImageResource imageResource = gameCollection.getImage();
            if (imageResource == null) {
                gameCollection.setImage(defGameCollection.getImage());
                gameCollectionEl.addContent(new Element("image"));
            } else if (imageResource.isAltered(defGameCollection.getImage())) {
                gameCollectionEl.addContent(convertToXMLElement("image", imageResource));
            } else {
                gameCollectionEl.addContent(new Element("image"));
            }

            SoundResource soundResource = gameCollection.getSound();
            if (soundResource == null) {
                gameCollection.setSound(defGameCollection.getSound());
                gameCollectionEl.addContent(new Element("sound"));
            } else if (soundResource.isAltered(defGameCollection.getSound())) {
                gameCollectionEl.addContent(convertToXMLElement("sound", soundResource));
            } else {
                gameCollectionEl.addContent(new Element("sound"));
            }


            SoundResource winSoundResource = gameCollection.getWinSound();
            if (winSoundResource == null) {
                gameCollection.setWinSound(defGameCollection.getWinSound());
                gameCollectionEl.addContent(new Element("winSound"));
            } else if (winSoundResource.isAltered(defGameCollection.getWinSound())) {
                gameCollectionEl.addContent(convertToXMLElement("winSound", winSoundResource));
            } else {
                gameCollectionEl.addContent(new Element("winSound"));
            }


            SoundResource errorSoundResource = gameCollection.getErrorSound();
            if (errorSoundResource == null) {
                gameCollection.setErrorSound(defGameCollection.getErrorSound());
                gameCollectionEl.addContent(new Element("errorSound"));
            } else if (errorSoundResource.isAltered(defGameCollection.getErrorSound())) {
                gameCollectionEl.addContent(convertToXMLElement("errorSound", errorSoundResource));
            } else {
                gameCollectionEl.addContent(new Element("errorSound"));
            }

        }

        GameCollection defGameCollection = null;
        if (defaultGameMod != null) {
            defGameCollection = defaultGameMod.getGameCollection(gameType);
        }


        for (Game game : gameCollection.getGames()) {
            String name = game.getNameUnmodified();
            Element gameEl = convertToXMLElement(game, gameCollection);
            if (defaultGameMod == null || defGameCollection.getGame(name) == null || defGameCollection.getGame(name).isAltered(game))
                gamesEl.addContent(gameEl);
            else
                gamesEl.addContent(convertToXMLElement("game", "name", game.getNameUnmodified(), "enabled", game.isEnabled()));
        }

        gameCollectionEl.addContent(gamesEl);
        return gameCollectionEl;
    }

    protected Element convertToXMLElement(Game game, GameCollection parent) {
        Element gameEl = convertToXMLElement("game", "name", game.getNameUnmodified(), "enabled", game.isEnabled());
        String difficulty = "";
        switch (parent.getGameType()) {
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

        ImageResource image = game.getImage();
        if (parent.getImage().isAltered(image))
            gameEl.addContent(convertToXMLElement("image", image));

        SoundResource sound = game.getSound();
        if (parent.getSound().isAltered(sound))
            gameEl.addContent(convertToXMLElement("sound", sound));

        SoundResource winSound = game.getWinSound();
        if (parent.getWinSound().isAltered(winSound))
            gameEl.addContent(convertToXMLElement("winSound", winSound));

        SoundResource errorSound = game.getErrorSound();
        if (parent.getErrorSound().isAltered(errorSound))
            gameEl.addContent(convertToXMLElement("errorSound", errorSound));

        Element gameImagesEl = new Element("gameImages");
        for (GameImage gameImage : game.getImages()) {
            Element gameImageEl = convertToXMLElement("gameImage", gameImage.isEnabled(), gameImage.getImage(), gameImage.getOrder());
            gameImagesEl.addContent(gameImageEl);
        }
        gameEl.addContent(gameImagesEl);

        return gameEl;
    }

    protected Element convertToXMLElement(String name, boolean value, ImageResource imageResource, int order) {
        Element e = new Element(name);
        if (value)
            e.setAttribute("enabled", "true");
        else
            e.setAttribute("enabled", "false");
        e.setAttribute("resourceType", imageResource.resourceType.name());
        e.addContent(imageResource.path);
        e.setAttribute("order", order + "");
        return e;
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
            Sentry.capture(ex.getMessage());
            return false;
        }
        return true;
    }

    public CommunicationModule getDownloadedCommunicationModule() {
        return downloadedCommunicationModule;
    }

    public List<Game> getDownloadedStimulusReactionGames() {
        return downloadedStimulusReactionGames;
    }

    public List<Game> getDownloadedSequenceGames (){
        return downloadedSequenceGames;
    }

    public List<Game> getDownloadedSimilarityGames() {
        return downloadedSimilarityGames;
    }
}
