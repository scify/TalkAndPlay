package org.scify.talkandplay.services;

import org.jdom.Element;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.ConfigurationFile;

public class ModuleService {

    private ConfigurationFile configurationFile;

    public ModuleService() {
        this.configurationFile = ConfigurationFile.getInstance();
    }

    public void update(User user) throws Exception {

        Element profile = configurationFile.getUserElement(user.getName());

        if (profile != null) {

            String path;
            if (!user.getEntertainmentModule().getMusicModule().getFolderPath().endsWith("/")) {
                path = user.getEntertainmentModule().getMusicModule().getFolderPath() + "/";
            } else {
                path = user.getEntertainmentModule().getMusicModule().getFolderPath();
            }

            profile.getChild("entertainment").getChild("music").getChild("path").setText(path);
            profile.getChild("entertainment").getChild("music").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

            if (!user.getEntertainmentModule().getVideoModule().getFolderPath().endsWith("/")) {
                path = user.getEntertainmentModule().getVideoModule().getFolderPath() + "/";
            } else {
                path = user.getEntertainmentModule().getVideoModule().getFolderPath();
            }

            profile.getChild("entertainment").getChild("video").getChild("path").setText(path);
            profile.getChild("entertainment").getChild("video").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

            configurationFile.update();
        }
    }
}
