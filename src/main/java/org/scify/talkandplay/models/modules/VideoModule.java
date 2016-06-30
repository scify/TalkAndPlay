package org.scify.talkandplay.models.modules;

public class VideoModule extends Module {

    private String folderPath;
    private int playlistSize;

    public VideoModule() {
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getPlaylistSize() {
        return playlistSize;
    }

    public void setPlaylistSize(int playlistSize) {
        this.playlistSize = playlistSize;
    }
}
