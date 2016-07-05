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

            profile.getChild("entertainment").getChild("music").getChild("path").setText(user.getEntertainmentModule().getMusicModule().getFolderPath());
            profile.getChild("entertainment").getChild("music").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

            profile.getChild("entertainment").getChild("video").getChild("path").setText(user.getEntertainmentModule().getVideoModule().getFolderPath());
            profile.getChild("entertainment").getChild("video").getChild("playlistSize").setText(String.valueOf(user.getEntertainmentModule().getMusicModule().getPlaylistSize()));

            configurationFile.update();
        }

    }

}
