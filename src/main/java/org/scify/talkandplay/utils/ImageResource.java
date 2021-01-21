package org.scify.talkandplay.utils;

import javax.swing.*;
import java.awt.*;

public class ImageResource {
    protected String path;
    protected ResourceType resourceType;
    protected ResourceManager rm = ResourceManager.getInstance();

    public ImageResource(String path, ResourceType resourceType) {
        this.path = path;
        this.resourceType = resourceType;
    }

    public String getPath() {
        return path;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Image getImage() {
        return rm.getImage(path, resourceType);
    }

    public ImageIcon getImageIcon() {
        return rm.getImageIcon(path, resourceType);
    }
}
