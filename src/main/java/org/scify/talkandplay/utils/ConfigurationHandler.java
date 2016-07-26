package org.scify.talkandplay.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.modules.CommunicationModule;
import org.scify.talkandplay.models.Configuration;
import org.scify.talkandplay.models.modules.EntertainmentModule;
import org.scify.talkandplay.models.modules.GameModule;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.models.games.SequenceGame;
import org.scify.talkandplay.models.games.SimilarityGame;
import org.scify.talkandplay.models.games.StimulusReactionGame;
import org.scify.talkandplay.models.modules.MusicModule;
import org.scify.talkandplay.models.modules.VideoModule;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

/**
 * ConfigurationHandler is responsible for parsing the xml and other xml-related
 * functions.
 *
 * @author christina
 */
public class ConfigurationHandler {

    private Document configurationFile;
    private List<User> users;
    private String projectPath;
    private Map<String, List<String>> userFiles;
    private List<String> files;
    private User currentUser;

    public ConfigurationHandler() {
        try {
            projectPath = System.getProperty("user.dir") + File.separator + "conf.xml";
            File file = new File(projectPath);
            if (!file.exists() || file.isDirectory()) {
                PrintWriter writer = new PrintWriter(projectPath, "UTF-8");
                writer.println("<?xml version=\"1.0\"?>\n"
                        + "<profiles></profiles>");
                writer.close();
            }

            SAXBuilder builder = new SAXBuilder();
            configurationFile = (Document) builder.build(file);

            parseXML();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public Document getConfigurationFile() {
        return configurationFile;
    }

    public List<User> getUsers() {
        return users;
    }

    public Element getRootElement() {
        return configurationFile.getRootElement();
    }

    public Element getUserElement(String name) throws Exception {
        Element userEl = null;
        List profiles = configurationFile.getRootElement().getChildren();

        for (int i = 0; i < profiles.size(); i++) {

            userEl = (Element) profiles.get(i);

            if (name.equals(userEl.getChildText("name"))) {
                break;
            }
        }
        return userEl;
    }

    /**
     * Parse the XML file that holds all users' configuration
     *
     * @return
     * @throws Exception
     */
    private void parseXML() throws Exception {
        userFiles = new HashMap();
        users = new ArrayList();
        List usersEl = configurationFile.getRootElement().getChildren();

        for (int i = 0; i < usersEl.size(); i++) {

            files = new ArrayList();
            Element userEl = (Element) usersEl.get(i);
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
            user.setCommunicationModule(getCommunicationModule(communication));

            Element entertainment = (Element) userEl.getChild("entertainment");
            user.setEntertainmentModule(getEntertainmentModule(entertainment));

            Element games = (Element) userEl.getChild("games");
            user.setGameModule(getGameModule(games));

            users.add(user);
            userFiles.put(user.getName(), files);
        }
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

    private CommunicationModule getCommunicationModule(Element communicationNode) {
        //set the communication module settings
        List<Category> categoriesArray = new ArrayList();

        Element categories = (Element) communicationNode.getChild("categories");
        categoriesArray = getCategories(categories, categoriesArray, null);

        CommunicationModule communicationModule = new CommunicationModule();
        communicationModule.setName(communicationNode.getChildText("name"));
        communicationModule.setRows(Integer.parseInt(communicationNode.getChildText("rows")));
        communicationModule.setColumns(Integer.parseInt(communicationNode.getChildText("columns")));

        if (communicationNode.getChildText("image").isEmpty()) {
            communicationModule.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/communication_module.png"));
        } else {
            communicationModule.setImage(communicationNode.getChildText("image"));
        }

        if (communicationNode.getChildText("sound").isEmpty()) {
            communicationModule.setSound("demo_resources/sounds/Επικοινωνία.mp3");
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
            entertainmentModule.setSound("demo_resources/sounds/Ψυχαγωγία.mp3");
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
            musicModule.setSound("demo_resources/sounds/Μουσική.mp3");
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
            videoModule.setSound("demo_resources/sounds/Βίντεο.mp3");
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
            gameModule.setSound("demo_resources/sounds/Παιχνίδια.mp3");
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
                stimulusReactionType.setSound("demo_resources/sounds/Ερέθισμα - Αντίδραση.mp3");
            } else {
                stimulusReactionType.setSound(stimulusReactionGamesNode.getChildText("sound"));
            }

            if (stimulusReactionGamesNode.getChildText("image").isEmpty()) {
                stimulusReactionType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/stimulus_game.png"));
            } else {
                stimulusReactionType.setImage(stimulusReactionGamesNode.getChildText("image"));
            }

            if (stimulusReactionGamesNode.getChildText("winSound").isEmpty()) {
                stimulusReactionType.setSound("demo_resources/sounds/winSound.mp3");
            } else {
                stimulusReactionType.setSound(stimulusReactionGamesNode.getChildText("winSound"));
            }

            if (stimulusReactionGamesNode.getChildText("errorSound").isEmpty()) {
                stimulusReactionType.setSound("demo_resources/sounds/errorSound.mp3");
            } else {
                stimulusReactionType.setSound(stimulusReactionGamesNode.getChildText("errorSound"));
            }

            List gamesList = stimulusReactionGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                StimulusReactionGame game = new StimulusReactionGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));
                game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));

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
                stimulusReactionType.getGames().add(game);
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
                sequenceGameType.setSound("demo_resources/sounds/Χρονικής Αλληλουχίας.mp3");
            } else {
                sequenceGameType.setSound(sequenceGamesNode.getChildText("sound"));
            }

            if (sequenceGamesNode.getChildText("image").isEmpty()) {
                sequenceGameType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/sequence_game.png"));
            } else {
                sequenceGameType.setImage(sequenceGamesNode.getChildText("image"));
            }

            if (sequenceGamesNode.getChildText("winSound").isEmpty()) {
                sequenceGameType.setSound("demo_resources/sounds/winSound.mp3");
            } else {
                sequenceGameType.setSound(sequenceGamesNode.getChildText("winSound"));
            }

            if (sequenceGamesNode.getChildText("errorSound").isEmpty()) {
                sequenceGameType.setSound("demo_resources/sounds/errorSound.mp3");
            } else {
                sequenceGameType.setSound(sequenceGamesNode.getChildText("errorSound"));
            }

            List gamesList = sequenceGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                SequenceGame game = new SequenceGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));
                game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));

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
                sequenceGameType.getGames().add(game);
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
                similarityGameType.setSound("demo_resources/sounds/Βρες το όμοιο.mp3");
            } else {
                similarityGameType.setSound(similarGamesNode.getChildText("sound"));
            }

            if (similarGamesNode.getChildText("image").isEmpty()) {
                similarityGameType.setImageURL(getClass().getResource("/org/scify/talkandplay/resources/defaultImgs/similarity_game.png"));
            } else {
                similarityGameType.setImage(similarGamesNode.getChildText("image"));
            }

            if (similarGamesNode.getChildText("winSound").isEmpty()) {
                similarityGameType.setSound("demo_resources/sounds/winSound.mp3");
            } else {
                similarityGameType.setSound(similarGamesNode.getChildText("winSound"));
            }

            if (similarGamesNode.getChildText("errorSound").isEmpty()) {
                similarityGameType.setSound("demo_resources/sounds/errorSound.mp3");
            } else {
                similarityGameType.setSound(similarGamesNode.getChildText("errorSound"));
            }

            List gamesList = similarGamesNode.getChild("games").getChildren();

            for (int i = 0; i < gamesList.size(); i++) {
                SimilarityGame game = new SimilarityGame(((Element) gamesList.get(i)).getChildText("name"),
                        "true".equals(((Element) gamesList.get(i)).getChildText("enabled")),
                        Integer.parseInt(((Element) gamesList.get(i)).getChildText("difficulty")));
                game.setWinSound(((Element) gamesList.get(i)).getChildText("winSound"));
                game.setErrorSound(((Element) gamesList.get(i)).getChildText("errorSound"));

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
                similarityGameType.getGames().add(game);
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
    private List<Category> getCategories(Element categoriesNode, List<Category> categories, Category parent) {

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
                } else {
                    category.setRows(currentUser.getConfiguration().getDefaultGridRow());
                }

                if (categoryEl.getChildText("columns") != null&& !categoryEl.getChildText("columns").isEmpty()) {
                    category.setColumns(Integer.parseInt(categoryEl.getChildText("columns")));
                } else {
                    category.setColumns(currentUser.getConfiguration().getDefaultGridColumn());
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

                if (parent != null) {
                    category.setParentCategory(parent);
                }

                List<Category> categoriesArray = new ArrayList<>();

                Element subCategories = (Element) categoryEl.getChild("categories");
                categoriesArray = getCategories(subCategories, categoriesArray, category);

                category.setSubCategories((ArrayList<Category>) categoriesArray);
                categories.add(category);

                if (!categoryEl.getChildText("sound").isEmpty()) {
                    files.add(categoryEl.getChildText("sound"));
                }
                if (!categoryEl.getChildText("image").isEmpty()) {
                    files.add(categoryEl.getChildText("image"));
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

    public void refreshXmlFile() throws Exception {
        SAXBuilder builder = new SAXBuilder();
        configurationFile = (Document) builder.build(new File(projectPath));
        parseXML();
    }

    /**
     * Write the new data to the xml file
     */
    public void writeToXmlFile() throws Exception {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(configurationFile, new FileWriter(projectPath));
    }

}
