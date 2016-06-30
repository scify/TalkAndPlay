package org.scify.talkandplay.models.modules;

public class MusicModule extends Module {

    private String folderPath;
    private int playlistSize;

    public MusicModule() {
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
