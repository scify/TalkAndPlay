package org.scify.talkandplay.utils;

import javax.swing.*;
import java.awt.*;

public class ImageResource extends MultimediaResource{

    public ImageResource(String path, ResourceType resourceType) {
        super(path, resourceType);
    }

    public ImageResource(ImageResource imageResource) {
        super(imageResource);
    }

    public Image getImage() {
        return rm.getImage(path, resourceType);
    }

    public ImageIcon getImageIcon() {
        return rm.getImageIcon(path, resourceType);
    }
}
