package org.scify.talkandplay.utils;

public class MultimediaResource {
    protected String path;
    protected ResourceType resourceType;
    protected ResourceManager rm;

    public MultimediaResource(String path, ResourceType resourceType) {
        rm = ResourceManager.getInstance();
        this.path = path;
        this.resourceType = resourceType;
    }

    public MultimediaResource(MultimediaResource multimediaResource) {
        rm = ResourceManager.getInstance();
        this.path = multimediaResource.path;
        this.resourceType = multimediaResource.resourceType;
    }

    public String getPath() {
        return path;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public boolean isAltered (MultimediaResource multimediaResource) {
        if (multimediaResource == null)
            return true;
        else if (path.equals(multimediaResource.path) && resourceType == multimediaResource.getResourceType())
            return false;
        else
            return true;
    }
}
