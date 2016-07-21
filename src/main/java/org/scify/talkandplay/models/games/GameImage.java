package org.scify.talkandplay.models.games;

/**
 *
 * @author christina
 */
public class GameImage {

    private String name;
    private String image;
    private String sound;
    private int order;
    private boolean enabled;

    public GameImage() {
    }

    public GameImage(String name, String image, int order) {
        this.name = name;
        this.image = image;
        this.order = order;
    }

    public GameImage(String image, boolean enabled, int order) {
        this.image = image;
        this.enabled = enabled;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
