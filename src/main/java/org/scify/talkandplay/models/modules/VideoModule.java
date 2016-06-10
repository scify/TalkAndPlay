package org.scify.talkandplay.models.modules;

public class VideoModule extends Module {

    private String folderPath;

    public VideoModule() {
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
}
