package org.scify.jthinkfreedom.talkandplay.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.scify.jthinkfreedom.talkandplay.models.Category;
import org.scify.jthinkfreedom.talkandplay.models.modules.CommunicationModule;
import org.scify.jthinkfreedom.talkandplay.models.Configuration;
import org.scify.jthinkfreedom.talkandplay.models.modules.EntertainmentModule;
import org.scify.jthinkfreedom.talkandplay.models.modules.GameModule;
import org.scify.jthinkfreedom.talkandplay.models.Tile;
import org.scify.jthinkfreedom.talkandplay.models.User;
import org.scify.jthinkfreedom.talkandplay.models.sensors.KeyboardSensor;
import org.scify.jthinkfreedom.talkandplay.models.sensors.MouseSensor;
import org.scify.jthinkfreedom.talkandplay.models.sensors.Sensor;

/**
 * ConfigurationHandler is responsible for parsing the xml and other xml-related
 * functions.
 *
 * @author christina
 */
public class ConfigurationHandler {

    private Document configurationFile;
    private List<User> profiles;
    private File file;
    private String projectPath;

    private static String DEFAULT_SOUND;

    public ConfigurationHandler() {
        DEFAULT_SOUND = getClass().getResource("/org/scify/jthinkfreedom/talkandplay/resources/sounds/cat.mp3").getPath();

        try {
            projectPath = System.getProperty("user.dir") + File.separator + "conf.xml";
            file = new File(projectPath);
            if (!file.exists() || file.isDirectory()) {
                PrintWriter writer = new PrintWriter(projectPath, "UTF-8");
                writer.println("<?xml version=\"1.0\"?>\n"
                        + "<profiles></profiles>");
                writer.close();
            }

            SAXBuilder builder = new SAXBuilder();
            configurationFile = (Document) builder.build(file);

            profiles = parseXML();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public Document getConfigurationFile() {
        return configurationFile;
    }

    public List<User> getProfiles() {
        try {
            profiles = parseXML();
            return profiles;
        } catch (Exception ex) {
            return null;
        }
    }

    public User getUser(String name) {
        for (User user : getProfiles()) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public Element getProfileElement(String name) throws Exception {
        Element profile = null;
        SAXBuilder builder = new SAXBuilder();
        configurationFile = (Document) builder.build(file);
        List profiles = configurationFile.getRootElement().getChildren();

        for (int i = 0; i < profiles.size(); i++) {

            profile = (Element) profiles.get(i);

            if (name.equals(profile.getChildText("name"))) {
                break;
            }
        }
        return profile;
    }

    /**
     * Parse the XML file that holds all users' configuration
     *
     * @return
     * @throws Exception
     */
    private List<User> parseXML() throws Exception {
        List<User> list = new ArrayList<>();
        List profiles = configurationFile.getRootElement().getChildren();

        for (int i = 0; i < profiles.size(); i++) {

            Element profile = (Element) profiles.get(i);
            User user = new User(profile.getChildText("name"), profile.getChildText("image"));

            if (profile.getAttributeValue("preselected") != null) {
                user.setPreselected(Boolean.parseBoolean(profile.getAttributeValue("preselected")));
            } else {
                user.setPreselected(false);
            }

            Element configuration = (Element) profile.getChild("configuration");
            user.setConfiguration(getConfiguration(configuration));

            //set the communication module settings
            List<Category> categoriesArray = new ArrayList<>();

            Element categories = (Element) profile.getChild("communication").getChild("categories");
            categoriesArray = getCategories(categories, categoriesArray, null);

            CommunicationModule communicationModule = new CommunicationModule();
            communicationModule.setName(profile.getChild("communication").getChildText("name"));
            communicationModule.setImage(profile.getChild("communication").getChildText("image"));
            communicationModule.setSound(getSound(profile.getChild("communication").getChildText("sound")));
            communicationModule.setRows(Integer.parseInt(profile.getChild("communication").getChildText("rows")));
            communicationModule.setColumns(Integer.parseInt(profile.getChild("communication").getChildText("columns")));

            communicationModule.setEnabled("true".equals(profile.getChild("communication").getChildText("enabled")));
            communicationModule.setCategories(categoriesArray);

            //set the entertainment module settings
            EntertainmentModule entertainmentModule = new EntertainmentModule();
            entertainmentModule.setName(profile.getChild("entertainment").getChildText("name"));
            entertainmentModule.setImage(profile.getChild("entertainment").getChildText("image"));
            entertainmentModule.setSound(getSound(profile.getChild("communication").getChildText("sound")));
            entertainmentModule.setEnabled("true".equals(profile.getChild("entertainment").getChildText("enabled")));

            //set the game module settings
            GameModule gameModule = new GameModule();
            gameModule.setName(profile.getChild("games").getChildText("name"));
            gameModule.setImage(profile.getChild("games").getChildText("image"));
            gameModule.setSound(getSound(profile.getChild("communication").getChildText("sound")));
            gameModule.setEnabled("true".equals(profile.getChild("games").getChildText("enabled")));

            user.setCommunicationModule(communicationModule);
            user.setEntertainmentModule(entertainmentModule);
            user.setGameModule(gameModule);

            list.add(user);
        }

        return list;
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

        Element selectionSensorEl = configurationNode.getChild("selectionSensor");
        if ("mouse".equals(selectionSensorEl.getChildText("type"))) {
            selectionSensor = new MouseSensor(Integer.parseInt(selectionSensorEl.getChildText("button")),
                    Integer.parseInt(selectionSensorEl.getChildText("clickCount")), selectionSensorEl.getChildText("mouse"));
        } else if ("keyboard".equals(selectionSensorEl.getChildText("type"))) {
            selectionSensor = new KeyboardSensor(Integer.parseInt(selectionSensorEl.getChildText("keyCode")),
                    selectionSensorEl.getChildText("keyChar").charAt(0), selectionSensorEl.getChildText("mouse"));
        }

        Element navigationSensorEl = configurationNode.getChild("navigationSensor");
        if (navigationSensorEl != null) {
            if ("mouse".equals(navigationSensorEl.getChildText("type"))) {
                navigationSensor = new MouseSensor(Integer.parseInt(navigationSensorEl.getChildText("button")),
                        Integer.parseInt(navigationSensorEl.getChildText("clickCount")), navigationSensorEl.getChildText("mouse"));
            } else if ("keyboard".equals(navigationSensorEl.getChildText("type"))) {
                navigationSensor = new KeyboardSensor(Integer.parseInt(navigationSensorEl.getChildText("keyCode")),
                        navigationSensorEl.getChildText("keyChar").charAt(0), navigationSensorEl.getChildText("mouse"));
            }
            configuration.setNavigationSensor(navigationSensor);
        }

        configuration.setSelectionSensor(selectionSensor);

        return configuration;
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
                        Integer.parseInt(categoryEl.getChildText("rows")),
                        Integer.parseInt(categoryEl.getChildText("columns")),
                        categoryEl.getChildText("image"));

                category.setSound(getSound(categoryEl.getChildText("sound")));

                if (parent != null) {
                    category.setParentCategory(new Category(parent.getName()));
                }

                if (categoryEl.getAttributeValue("editable") != null) {
                    category.setEditable(Boolean.parseBoolean(categoryEl.getAttributeValue("editable")));
                } else {
                    category.setEditable(true);
                }

                if (categoryEl.getAttributeValue("order") != null) {
                    category.setOrder(Integer.parseInt(categoryEl.getAttributeValue("order")));
                } else {
                    category.setOrder(0);
                }

                //set the tiles
                if (categoryEl.getChild("tiles") != null) {
                    Element tileEl;
                    for (int j = 0; j < categoryEl.getChild("tiles").getChildren().size(); j++) {
                        tileEl = (Element) categoryEl.getChild("tiles").getChildren().get(j);

                        int order = 0;
                        if (tileEl.getAttributeValue("order") != null) {
                            order = Integer.parseInt(categoryEl.getAttributeValue("order"));
                        }

                        category.getTiles().add(new Tile(tileEl.getChildText("name"), tileEl.getChildText("image"), tileEl.getChildText("sound"), order));
                    }
                }

                if (parent != null) {
                    category.setParentCategory(parent);
                }

                List<Category> categoriesArray = new ArrayList<>();

                Element subCategories = (Element) categoryEl.getChild("categories");
                categoriesArray = getCategories(subCategories, categoriesArray, category);

                category.setSubCategories((ArrayList<Category>) categoriesArray);
                categories.add(category);

            }
            return categories;
        }
    }

    /**
     * Set the sound, either the path given or the default one
     *
     * @param path
     * @return
     */
    private String getSound(String path) {
        if (path != null) {
            return path;
        } else {
            return DEFAULT_SOUND;
        }
    }

    public List refreshXMLFile() throws Exception {
        SAXBuilder builder = new SAXBuilder();
        configurationFile = (Document) builder.build(file);
        profiles = parseXML();
        return profiles;
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
