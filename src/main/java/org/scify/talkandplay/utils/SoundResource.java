package org.scify.talkandplay.utils;

import java.io.File;

public class SoundResource extends MultimediaResource {

    public SoundResource(String path, ResourceType resourceType) {
        super(path, resourceType);
    }

    public SoundResource(SoundResource soundResource) {
        super(soundResource);
    }

    public File getSound() {
        return rm.getSound(path, resourceType);
    }
}
