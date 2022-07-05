/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.sentry.Sentry;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.scify.talkandplay.gui.MainFrame;
import org.scify.talkandplay.gui.users.UserFormPanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.modules.CommunicationModule;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.utils.*;

public class UserService {

    protected TalkAndPlayProfileConfiguration talkAndPlayProfileconfiguration;
    protected XMLConfigurationHandler xmlConfHandler;
    protected ResourceManager rm;

    public UserService() {
        this.rm = ResourceManager.getInstance();
        this.talkAndPlayProfileconfiguration = TalkAndPlayProfileConfiguration.getInstance();
        this.xmlConfHandler = talkAndPlayProfileconfiguration.getConfigurationHandler();
    }

    public User getUser(String name) {
        for (User user : xmlConfHandler.getUsers()) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        List<User> users = talkAndPlayProfileconfiguration.getConfigurationHandler().getUsers();
        return users;
    }

    /**
     * Check if name is already used.
     *
     * @param name User name to check if it is used
     * @return True if name is used, False otherwise
     */
    private boolean nameIsUsed(String name) {
        return this.getUser(name) != null;  //if a user exists, then we already have him
    }

    private String getUniqueUserName(String userName) {
        List<User> users = getUsers();
        int counter = 2;
        boolean foundUserName = false;
        String tempName = null;
        while (!foundUserName) {
            tempName = userName + counter;
            int counterCopy = counter;
            for (User u : users) {
                //if user name is found try again with the incremented counter
                if (u.getName().equals(tempName)) {
                    counter++;
                    break;
                }
            }
            //if the counter hasn't been changed, the unique user name has been found
            if (counterCopy == counter) {
                foundUserName = true;
            }
        }
        return tempName;
    }

    public void createUserAsCopyOfDefaultUser() throws Exception {
        String name = xmlConfHandler.getDefaultUser().getName();
        if (nameIsUsed(name)) {
            String uniqueName = getUniqueUserName(name);
            if (uniqueName != null) {
                name = uniqueName;
            }
        }
        xmlConfHandler.createNewUser(name);
        xmlConfHandler.update();
    }

    public void createUserAsCopyOfDefaultUser(String name) throws Exception{
        xmlConfHandler.createNewUser(name);
        xmlConfHandler.update();
    }


    public void update(User oldUser, User newUser) throws Exception {
        xmlConfHandler.updateUser(oldUser, newUser);
        xmlConfHandler.update();
    }

    /**
     * Upload a user from xml file
     *
     * @param file
     */
    public boolean uploadUserFromFile(File file) {

        User user = xmlConfHandler.createNewUser(file);
        if (user == null)
            return false;

        String userName = user.getName();
        if (nameIsUsed(userName))
            user.setName(getUniqueUserName(userName));

        try {
            xmlConfHandler.addUser(user);
            xmlConfHandler.update();
        } catch (Exception exception) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, exception);
            Sentry.captureMessage(exception.getMessage());
            return false;
        }
        return true;
    }

    public boolean storeUserToExternalFile(String userName, String folderPath) {
        //if folder does not exist, create it
        File folder = new File(folderPath);
        if (!folder.exists())
            folder.mkdir();

        //set file
        String filePath = folderPath + File.separator + userName + ".xml";
        File file = new File(filePath);
        return xmlConfHandler.exportUserToFile(userName, file);
    }

    /**
     * Remove a user from the xml file
     *
     * @param user
     */
    public void delete(User user) throws Exception {
        xmlConfHandler.deleteUser(user.getName());
        xmlConfHandler.update();
    }

    public boolean hasBrokenFiles(String username) {
        return xmlConfHandler.hasBrokenFiles(username);
    }

    public List<String> getBrokenFiles(String username) {
        return xmlConfHandler.getBrokenFiles(username);
    }
}