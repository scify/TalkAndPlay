package org.scify.talkandplay.utils;

import java.io.File;

public class SoundResource {
    protected String path;
    protected ResourceType resourceType;
    protected ResourceManager rm = ResourceManager.getInstance();

    public SoundResource(String path, ResourceType resourceType) {
        this.path = path;
        this.resourceType = resourceType;
    }

    public SoundResource getCopy() {
        return new SoundResource(path, resourceType);
    }

    public String getPath() {
        return path;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public File getSound() {
        return rm.getSound(path, resourceType);
    }

    public boolean isAltered (SoundResource soundResource) {
        if (path.equals(soundResource.path) && resourceType == soundResource.getResourceType())
            return false;
        else
            return true;
    }
}
