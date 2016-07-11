package org.scify.talkandplay.services;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.utils.ConfigurationFile;

public class UserService {

    private ConfigurationFile configurationFile;

    public UserService() {
        this.configurationFile = ConfigurationFile.getInstance();
    }

    /**
     * Find one user
     *
     * @param name
     * @return
     */
    public User getUser(String name) {
        User user = configurationFile.getUser(name);

        return user;
    }

    public List<User> getUsers() {
        List<User> user = configurationFile.getUsers();

        return user;
    }

    /**
     * Save a user to the xml file
     *
     * @param user
     */
    public void save(User user) throws Exception {

        Element profiles = configurationFile.getRootElement();

        //add the general profile info
        Element profile = new Element("profile");
        profile.addContent(new Element("name").setText(user.getName()));
        profile.addContent(new Element("image").setText(user.getImage()));
        profile.setAttribute(new Attribute("preselected", String.valueOf(user.isPreselected())));

        //add the configurations
        Element configuration = new Element("configuration");
        configuration.addContent(new Element("rotationSpeed").setText(String.valueOf(user.getConfiguration().getRotationSpeed())));
        configuration.addContent(new Element("defaultGridRow").setText(String.valueOf(user.getConfiguration().getDefaultGridRow())));
        configuration.addContent(new Element("defaultGridColumn").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn())));
        configuration.addContent(new Element("sound").setText(String.valueOf(user.getConfiguration().hasSound())));
        configuration.addContent(new Element("image").setText(String.valueOf(user.getConfiguration().hasImage())));
        configuration.addContent(new Element("text").setText(String.valueOf(user.getConfiguration().hasText())));

        //add the selection sensor
        Element selectionSensor = new Element("selectionSensor");

        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            selectionSensor.addContent(new Element("type").setText("mouse"));
            selectionSensor.addContent(new Element("button").setText(String.valueOf(((MouseSensor) user.getConfiguration().getSelectionSensor()).getButton())));
            selectionSensor.addContent(new Element("clickCount").setText(String.valueOf(((MouseSensor) user.getConfiguration().getSelectionSensor()).getClickCount())));
        } else if (user.getConfiguration().getSelectionSensor() instanceof KeyboardSensor) {
            selectionSensor.addContent(new Element("type").setText("keyboard"));
            selectionSensor.addContent(new Element("keyCode").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyCode())));
            selectionSensor.addContent(new Element("keyChar").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyChar())));
        }

        //add the navigation sensor, if any
        if (user.getConfiguration().getNavigationSensor() != null) {
            Element navigationSensor = new Element("navigationSensor");

            if (user.getConfiguration().getNavigationSensor() instanceof MouseSensor) {
                navigationSensor.addContent(new Element("type").setText("mouse"));
                navigationSensor.addContent(new Element("button").setText(String.valueOf(((MouseSensor) user.getConfiguration().getNavigationSensor()).getButton())));
                navigationSensor.addContent(new Element("clickCount").setText(String.valueOf(((MouseSensor) user.getConfiguration().getNavigationSensor()).getClickCount())));
            } else if (user.getConfiguration().getNavigationSensor() instanceof KeyboardSensor) {
                navigationSensor.addContent(new Element("type").setText("keyboard"));
                navigationSensor.addContent(new Element("keyCode").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getNavigationSensor()).getKeyCode())));
                navigationSensor.addContent(new Element("keyChar").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getNavigationSensor()).getKeyChar())));
            }
            configuration.addContent(navigationSensor);
        }

        configuration.addContent(selectionSensor);
        profile.addContent(configuration);

        //add communication module settings
        Element communication = new Element("communication");
        Element categories = new Element("categories");

        //add the first category settings
        communication.addContent(new Element("name").setText("Επικοινωνία"));
        communication.addContent(new Element("enabled").setText("true"));
        communication.addContent(new Element("image"));
        communication.addContent(new Element("rows").setText("1"));
        communication.addContent(new Element("columns").setText("3"));
        communication.addContent(categories);

        //add entertainment module settings
        Element entertainment = new Element("entertainment");
        entertainment.addContent(new Element("name").setText("Ψυχαγωγία"));
        entertainment.addContent(new Element("enabled").setText("true"));
        entertainment.addContent(new Element("image"));

        //add music module settings
        Element music = new Element("music");
        music.addContent(new Element("name").setText("Μουσική"));
        music.addContent(new Element("enabled").setText("true"));
        music.addContent(new Element("image"));
        music.addContent(new Element("path"));
        music.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(music);

        //add video module settings
        Element video = new Element("video");
        video.addContent(new Element("name").setText("Βίντεο"));
        video.addContent(new Element("enabled").setText("true"));
        video.addContent(new Element("image"));
        video.addContent(new Element("path"));
        video.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(video);

        //add game module settings
        Element games = new Element("games");
        games.addContent(new Element("name").setText("Παιχνίδια"));
        games.addContent(new Element("image"));
        games.addContent(new Element("enabled").setText("true"));

        profile.addContent(communication);
        profile.addContent(entertainment);
        profile.addContent(games);
        profiles.addContent(profile);

        configurationFile.update();
    }

    /**
     * Update a user
     *
     * @param name
     */
    public void update(User user, String oldName) throws Exception {

        List profiles = configurationFile.getRootElement().getChildren();

        //find the user from the users list
        for (int i = 0; i < profiles.size(); i++) {

            Element profile = (Element) profiles.get(i);

            if (profile.getChildText("name").equals(oldName)) {

                profile.getChild("name").setText(user.getName());
                profile.getChild("configuration").getChild("rotationSpeed").setText(String.valueOf(user.getConfiguration().getRotationSpeed()));
                profile.getChild("configuration").getChild("defaultGridRow").setText(String.valueOf(user.getConfiguration().getDefaultGridRow()));
                profile.getChild("configuration").getChild("defaultGridColumn").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn()));
                profile.getChild("configuration").getChild("sound").setText(String.valueOf(user.getConfiguration().hasSound()));
                profile.getChild("configuration").getChild("image").setText(String.valueOf(user.getConfiguration().hasImage()));
                profile.getChild("configuration").getChild("text").setText(String.valueOf(user.getConfiguration().hasText()));

                profile.setAttribute(new Attribute("preselected", String.valueOf(user.isPreselected())));

                Element selectionSensor = new Element("selectionSensor");

                if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
                    selectionSensor.addContent(new Element("type").setText("mouse"));
                    selectionSensor.addContent(new Element("button").setText(String.valueOf(((MouseSensor) user.getConfiguration().getSelectionSensor()).getButton())));
                    selectionSensor.addContent(new Element("clickCount").setText(String.valueOf(((MouseSensor) user.getConfiguration().getSelectionSensor()).getClickCount())));
                } else if (user.getConfiguration().getSelectionSensor() instanceof KeyboardSensor) {
                    selectionSensor.addContent(new Element("type").setText("keyboard"));
                    selectionSensor.addContent(new Element("keyCode").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyCode())));
                    selectionSensor.addContent(new Element("keyChar").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyChar())));
                }

                profile.getChild("configuration").removeChild("selectionSensor");
                profile.getChild("configuration").addContent(selectionSensor);

                if (user.getConfiguration().getNavigationSensor() != null) {
                    Element navigationSensor = new Element("navigationSensor");

                    if (user.getConfiguration().getNavigationSensor() instanceof MouseSensor) {
                        navigationSensor.addContent(new Element("type").setText("mouse"));
                        navigationSensor.addContent(new Element("button").setText(String.valueOf(((MouseSensor) user.getConfiguration().getNavigationSensor()).getButton())));
                        navigationSensor.addContent(new Element("clickCount").setText(String.valueOf(((MouseSensor) user.getConfiguration().getNavigationSensor()).getClickCount())));
                    } else if (user.getConfiguration().getNavigationSensor() instanceof KeyboardSensor) {
                        navigationSensor.addContent(new Element("type").setText("keyboard"));
                        navigationSensor.addContent(new Element("keyCode").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getNavigationSensor()).getKeyCode())));
                        navigationSensor.addContent(new Element("keyChar").setText(String.valueOf(((KeyboardSensor) user.getConfiguration().getNavigationSensor()).getKeyChar())));
                    }

                    profile.getChild("configuration").removeChild("navigationSensor");
                    profile.getChild("configuration").addContent(navigationSensor);
                } else {
                    profile.getChild("configuration").removeChild("navigationSensor");
                }

                if (user.getImage() == null) {
                    profile.getChild("image").setText(profile.getChildText("image"));
                    user.setImage(profile.getChildText("image"));
                } else {
                    profile.getChild("image").setText(user.getImage());
                }

                configurationFile.update();
                break;
            }
        }
    }

    /**
     * Remove a user from the xml file
     *
     * @param user
     */
    public void delete(User user) throws Exception {

        List profiles = configurationFile.getRootElement().getChildren();

        //find the user from the users list
        for (int i = 0; i < profiles.size(); i++) {

            Element profile = (Element) profiles.get(i);

            if (profile.getChildText("name").equals(user.getName())) {
                profile.detach();
                configurationFile.update();
                break;
            }
        }
    }

    public boolean hasBrokenFiles(User user) {
        boolean flag = false;

        // for(File file : configurationHandler.getFiles())
        return flag;
    }
}
