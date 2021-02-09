package org.scify.talkandplay.utils;

import javax.swing.*;
import java.awt.*;

public class ImageResource {
    protected String path;
    protected ResourceType resourceType;
    protected ResourceManager rm;

    public ImageResource(String path, ResourceType resourceType) {
        this.path = path;
        this.resourceType = resourceType;
        rm = ResourceManager.getInstance();
    }

    public ImageResource(ImageResource imageResource) {
        this.path = imageResource.path;
        this.resourceType = imageResource.resourceType;
        rm = ResourceManager.getInstance();
    }

    public ImageResource getCopy() {
        return new ImageResource(this);
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

    public boolean isAltered (ImageResource imageResource) {
        if (path.equals(imageResource.path) && resourceType == imageResource.getResourceType())
            return false;
        else
            return true;
    }
}
