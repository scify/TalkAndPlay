package org.scify.talkandplay.services;

import org.jdom.Element;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ConfigurationHandler;

public class ModuleService {

    private ConfigurationHandler configurationHandler;
    String projectPath;

    public ModuleService() {
        configurationHandler = new ConfigurationHandler();
    }

    public void update(User user) throws Exception {

        Element profile = configurationHandler.getProfileElement(user.getName());

        if (profile != null) {

            profile.getChild("entertainment").getChild("music").getChild("path").setText(user.getEntertainmentModule().getMusicModule().getFolderPath());
            profile.getChild("entertainment").getChild("music").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));
            
            profile.getChild("entertainment").getChild("video").getChild("path").setText(user.getEntertainmentModule().getVideoModule().getFolderPath());
            profile.getChild("entertainment").getChild("video").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

            configurationHandler.writeToXmlFile();
        }

    }

}
