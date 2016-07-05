package org.scify.talkandplay.utils;

import java.util.List;
import org.jdom.Element;
import org.scify.talkandplay.models.User;

public class ConfigurationFile {

    private ConfigurationHandler configurationHandler;
    private List<User> users;

    private static ConfigurationFile instance = new ConfigurationFile();

    private ConfigurationFile() {
        init();
    }

    private void init() {
        configurationHandler = new ConfigurationHandler();
        users = configurationHandler.getUsers();
    }

    public static ConfigurationFile getInstance() {
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void update() throws Exception {
        configurationHandler.writeToXmlFile();
        configurationHandler.refreshXmlFile();
        users = configurationHandler.getUsers();
    }

    public Element getRootElement() throws Exception {
        return configurationHandler.getRootElement();
    }

    public Element getUserElement(String name) throws Exception {
        return configurationHandler.getUserElement(name);
    }

}
