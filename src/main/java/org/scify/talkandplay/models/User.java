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
package org.scify.talkandplay.models;

import org.scify.talkandplay.models.modules.CommunicationModule;
import org.scify.talkandplay.models.modules.EntertainmentModule;
import org.scify.talkandplay.models.modules.GameModule;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;

public class User {

    private String name;
    private ImageResource image;
    private Configuration configuration;

    private CommunicationModule communicationModule;
    private EntertainmentModule entertainmentModule;
    private GameModule gameModule;
    private ResourceManager rm;

    public User(String name, ImageResource image) {
        rm = ResourceManager.getInstance();
        this.name = name;
        this.image = image;
        configuration = new Configuration();
    }

    public String getName() {
        return rm.decodeTextIfRequired(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageResource getImage() {
        return image;
    }

    public void setImage(ImageResource image) {
        this.image = image;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public CommunicationModule getCommunicationModule() {
        return communicationModule;
    }

    public void setCommunicationModule(CommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    public EntertainmentModule getEntertainmentModule() {
        return entertainmentModule;
    }

    public void setEntertainmentModule(EntertainmentModule entertainmentModule) {
        this.entertainmentModule = entertainmentModule;
    }

    public GameModule getGameModule() {
        return gameModule;
    }

    public void setGameModule(GameModule gameModule) {
        this.gameModule = gameModule;
    }

}
