package org.scify.talkandplay.models.games;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christina
 */
public class Game {

    private String name;
    private String image;
    private String sound;
    private String winSound;
    private String errorSound;
    private boolean enabled;

    //used only to display default ImageIcons from the app jar
    private URL imageURL;

    private List<Game> games;

    public Game() {
        this.games = new ArrayList();
    }

    public Game(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
        this.games = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getWinSound() {
        return winSound;
    }

    public void setWinSound(String winSound) {
        this.winSound = winSound;
    }

    public String getErrorSound() {
        return errorSound;
    }

    public void setErrorSound(String errorSound) {
        this.errorSound = errorSound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

}
