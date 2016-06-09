package org.scify.jthinkfreedom.talkandplay.models.modules;

public class EntertainmentModule extends Module {

    private String musicPath;
    private String videosPath;

    public EntertainmentModule() {
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getVideosPath() {
        return videosPath;
    }

    public void setVideosPath(String videosPath) {
        this.videosPath = videosPath;
    }

}
