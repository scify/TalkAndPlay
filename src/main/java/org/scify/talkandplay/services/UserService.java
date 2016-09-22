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

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private String getUniqueUserName(String userName, List<User> users) {
        int counter = 2;
        boolean foundUserName = false;
        String tempName = null;
        while (!foundUserName){
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
    
    private Element setNewUserCommunicationConfFromOldConfiguration(User user){
        CommunicationModule cm = user.getCommunicationModule();
                
        Element communication = new Element("communication");
        Element categories = new Element("categories");

        //add the first category settings
        communication.addContent(new Element("name").setText("Επικοινωνία"));
        communication.addContent(new Element("enabled").setText("true"));
        communication.addContent(new Element("image"));
        communication.addContent(new Element("sound"));
        communication.addContent(new Element("rows").setText(String.valueOf(user.getConfiguration().getDefaultGridRow())));
        communication.addContent(new Element("columns").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn())));        
        
        //add rest communication categories using the user's CommunicationModule
        if (!cm.getCategories().isEmpty()) {
            for (Category category : cm.getCategories()){
                //create the main category
                Element categoryEl = new Element("category").setAttribute("name", category.getName());
                categoryEl.addContent(new Element("rows").setText(String.valueOf(user.getConfiguration().getDefaultGridRow())));
                categoryEl.addContent(new Element("columns").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn())));
                categoryEl.addContent(new Element("image").setText(category.getImage()));
                categoryEl.addContent(new Element("sound").setText(category.getSound()));
                categoryEl.addContent(new Element("hasSound").setText(String.valueOf(category.hasSound())));
                categoryEl.addContent(new Element("hasImage").setText(String.valueOf(category.hasImage())));                
                categoryEl.addContent(new Element("hasText").setText(String.valueOf(category.hasText())));
                
                //add the subcategories
                if (!category.getSubCategories().isEmpty()) {
                    Element subcategoriesEl = new Element("categories");
                    for (Category subcategory : category.getSubCategories()) {                        
                        Element subcategoryEl = new Element("category").setAttribute("name", subcategory.getName());
                        subcategoryEl.addContent(new Element("rows").setText(String.valueOf(user.getConfiguration().getDefaultGridRow())));
                        subcategoryEl.addContent(new Element("columns").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn())));
                        subcategoryEl.addContent(new Element("image").setText(subcategory.getImage()));
                        subcategoryEl.addContent(new Element("sound").setText(subcategory.getSound()));
                        subcategoryEl.addContent(new Element("hasSound").setText(String.valueOf(subcategory.hasSound())));
                        subcategoryEl.addContent(new Element("hasImage").setText(String.valueOf(subcategory.hasImage())));                
                        subcategoryEl.addContent(new Element("hasText").setText(String.valueOf(subcategory.hasText())));
                        subcategoriesEl.addContent(subcategoryEl);
                    }
                    categoryEl.addContent(subcategoriesEl);
                }
                
                //add all the category inside the communication tag 
                categories.addContent(categoryEl);
            }
        }
        
        communication.addContent(categories);
        
        return communication;
    }
    
    /**
     * Copy a user's configuration to create a new one
     * 
     * @return boolean(success of method)
     */
    public boolean createUserFromOldConfiguration() throws Exception {
        Element profiles = configurationFile.getRootElement();
        
        /**
         * get the first user from users list or return false if it is not 
         * existent
         */
        List<User> users = configurationFile.getUsers();
        User user = null;
        if (!users.isEmpty()){
            user = users.get(0);
        } else {
            return false;
        }
        
        //add the general profile info
        Element profile = new Element("profile");
        
        //get new unique user name
        String newUserName = getUniqueUserName(user.getName(), users);        
        
        profile.addContent(new Element("name").setText(newUserName));
        profile.addContent(new Element("image").setText(user.getImage()));
        profile.setAttribute(new Attribute("preselected", "false"));

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
        Element communication = setNewUserCommunicationConfFromOldConfiguration(user);

        //add entertainment module settings
        Element entertainment = new Element("entertainment");
        entertainment.addContent(new Element("name").setText("Ψυχαγωγία"));
        entertainment.addContent(new Element("enabled").setText("true"));
        entertainment.addContent(new Element("image"));
        entertainment.addContent(new Element("sound"));

        //add music module settings
        Element music = new Element("music");
        music.addContent(new Element("name").setText("Μουσική"));
        music.addContent(new Element("enabled").setText("true"));
        music.addContent(new Element("image"));
        music.addContent(new Element("sound"));
        music.addContent(new Element("path"));
        music.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(music);

        //add video module settings
        Element video = new Element("video");
        video.addContent(new Element("name").setText("Βίντεο"));
        video.addContent(new Element("enabled").setText("true"));
        video.addContent(new Element("image"));
        video.addContent(new Element("sound"));
        video.addContent(new Element("path"));
        video.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(video);

        //add game module settings
        Element games = new Element("games");
        games.addContent(new Element("name").setText("Παιχνίδια"));
        games.addContent(new Element("image"));
        games.addContent(new Element("sound"));
        games.addContent(new Element("enabled").setText("true"));

        //set the default games
        GameService gameService = new GameService();

        List gamesList = gameService.setDefaultGames();
        for (int i = 0; i < gamesList.size(); i++) {
            Element elemCopy = (Element) ((Element) gamesList.get(i)).clone();
            elemCopy.detach();
            games.addContent(elemCopy);
        }

        profile.addContent(communication);
        profile.addContent(entertainment);
        profile.addContent(games);
        profiles.addContent(profile);

        configurationFile.update();
        
        return true;
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
        communication.addContent(new Element("sound"));
        communication.addContent(new Element("rows").setText(String.valueOf(user.getConfiguration().getDefaultGridRow())));
        communication.addContent(new Element("columns").setText(String.valueOf(user.getConfiguration().getDefaultGridColumn())));
        communication.addContent(categories);

        //add entertainment module settings
        Element entertainment = new Element("entertainment");
        entertainment.addContent(new Element("name").setText("Ψυχαγωγία"));
        entertainment.addContent(new Element("enabled").setText("true"));
        entertainment.addContent(new Element("image"));
        entertainment.addContent(new Element("sound"));

        //add music module settings
        Element music = new Element("music");
        music.addContent(new Element("name").setText("Μουσική"));
        music.addContent(new Element("enabled").setText("true"));
        music.addContent(new Element("image"));
        music.addContent(new Element("sound"));
        music.addContent(new Element("path"));
        music.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(music);

        //add video module settings
        Element video = new Element("video");
        video.addContent(new Element("name").setText("Βίντεο"));
        video.addContent(new Element("enabled").setText("true"));
        video.addContent(new Element("image"));
        video.addContent(new Element("sound"));
        video.addContent(new Element("path"));
        video.addContent(new Element("playlistSize").setText("10"));
        entertainment.addContent(video);

        //add game module settings
        Element games = new Element("games");
        games.addContent(new Element("name").setText("Παιχνίδια"));
        games.addContent(new Element("image"));
        games.addContent(new Element("sound"));
        games.addContent(new Element("enabled").setText("true"));

        //set the default games
        GameService gameService = new GameService();

        List gamesList = gameService.setDefaultGames();
        for (int i = 0; i < gamesList.size(); i++) {
            Element elemCopy = (Element) ((Element) gamesList.get(i)).clone();
            elemCopy.detach();
            games.addContent(elemCopy);
        }

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
     * Check if name is already used
     * 
     * @param name
     * @param users
     * @return boolean
     */
    private boolean nameIsUsed(String name, List<User> users) {
        for (User u : users) {
            //if user name is found
            if (u.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Upload a user from xml file
     * 
     * @param file
     */
    public boolean uploadUserFromFile(File file) {
        try {
            //get profile from selected file
            SAXBuilder builder = new SAXBuilder();
            Document profileXml = (Document) builder.build(file);
            Element profile = profileXml.getRootElement();
            profile.detach();
            //check if profile name is unique
            List<User> users = configurationFile.getUsers();
            String name = profile.getChild("name").getText();
            if (nameIsUsed(name, users)) {
                profile.getChild("name").setText(getUniqueUserName(name, users));
            }
            Element profiles = configurationFile.getRootElement();
            profiles.addContent(profile);
            configurationFile.update();
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean storeUserToExternalFile(String userName, File file) {
        try {
            //get profile from configuration xml               
            Element profile = configurationFile.getUserElement(userName);
            //write profile to selected file
            PrintWriter pw = new PrintWriter(file);
            pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.output(profile, pw);
            pw.close();
        } catch (Exception ex) {
            Logger.getLogger(UserFormPanel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
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

    public boolean hasBrokenFiles(String username) {
        return configurationFile.hasBrokenFiles(username);
    }

    public List<String> getBrokenFiles(String username) {
        return configurationFile.getBrokenFiles(username);
    }
}
