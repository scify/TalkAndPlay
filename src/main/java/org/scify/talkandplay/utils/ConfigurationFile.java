package org.scify.talkandplay.utils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.scify.talkandplay.models.User;

public class ConfigurationFile {

    private ConfigurationHandler configurationHandler;

    private static ConfigurationFile instance = new ConfigurationFile();

    private ConfigurationFile() {

        configurationHandler = new ConfigurationHandler();
    }

    public static ConfigurationFile getInstance() {
        return instance;
    }

 /*   public List<User> getProfiles() {
        try {
            return configurationHandler.parseXML();
        } catch (Exception ex) {
            return null;
        }
    }

    public User getUser(String name) {
        List<User> profiles = getProfiles();
        for (User user : profiles) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User refreshAndGetUser(String name) {
        try {
            List<User> profiles = refreshXMLFile();

            for (User user : profiles) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
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
*/
}
