/**
 * Copyright 2016 SciFY
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.services;

import org.jdom.Element;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;

public class ModuleService {

    private TalkAndPlayProfileConfiguration talkAndPlayConfigurationFile;

    public ModuleService() {
        this.talkAndPlayConfigurationFile = TalkAndPlayProfileConfiguration.getInstance();
    }

    public void update(User user) throws Exception {

        Element profile = talkAndPlayConfigurationFile.getConfigurationHandler().getUserElement(user.getName());

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

            talkAndPlayConfigurationFile.getConfigurationHandler().update();
        }
    }
}
