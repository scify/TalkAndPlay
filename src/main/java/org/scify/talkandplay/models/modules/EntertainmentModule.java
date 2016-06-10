package org.scify.talkandplay.models.modules;

public class EntertainmentModule extends Module {

    private MusicModule musicModule;
    private VideoModule videoModule;

    public EntertainmentModule() {
    }

    public MusicModule getMusicModule() {
        return musicModule;
    }

    public void setMusicModule(MusicModule musicModule) {
        this.musicModule = musicModule;
    }

    public VideoModule getVideoModule() {
        return videoModule;
    }

    public void setVideoModule(VideoModule videoModule) {
        this.videoModule = videoModule;
    }

}
