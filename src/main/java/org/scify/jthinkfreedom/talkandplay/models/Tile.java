package org.scify.jthinkfreedom.talkandplay.models;

public class Tile {

    private String name;
    private String image;
    private String sound;
    private int order;
    private Category category;

    public Tile() {

    }

    public Tile(String name, String image, String sound, int order) {
        this.image = image;
        this.name = name;
        this.sound = sound;
        this.order = order;
    }

    public Tile(String name, String image, String sound, Category category) {
        this.image = image;
        this.name = name;
        this.sound = sound;
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
