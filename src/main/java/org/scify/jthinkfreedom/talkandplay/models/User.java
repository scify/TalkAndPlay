package org.scify.jthinkfreedom.talkandplay.models;

import org.scify.jthinkfreedom.talkandplay.models.modules.CommunicationModule;
import org.scify.jthinkfreedom.talkandplay.models.modules.EntertainmentModule;
import org.scify.jthinkfreedom.talkandplay.models.modules.GameModule;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String image;
    private boolean preselected;

    private Configuration configuration;
    private CommunicationModule communicationModule;
    private EntertainmentModule entertainmentModule;
    private GameModule gameModule;

    public User() {
        configuration = new Configuration();
        communicationModule = new CommunicationModule();
        entertainmentModule = new EntertainmentModule();
        gameModule = new GameModule();
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String image) {
        this.name = name;
        this.image = image;
        configuration = new Configuration();
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

    public boolean isPreselected() {
        return preselected;
    }

    public void setPreselected(boolean preselected) {
        this.preselected = preselected;
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
