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

    public boolean hasBrokenFiles(String username) {
        return configurationHandler.hasBrokenFiles(username);
    }

    
    public List<String> getBrokenFiles(String username){
        return configurationHandler.getBrokenFiles(username);
    }
}
