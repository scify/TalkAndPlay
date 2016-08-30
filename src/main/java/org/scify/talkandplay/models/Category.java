package org.scify.talkandplay.models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private String image;
    private String sound;
    private Integer rows, columns;
    private int order;
    private boolean editable;
    private boolean enabled;
    private Category parentCategory;
    private List<Category> subCategories;

    private boolean hasSound;
    private boolean hasImage;
    private boolean hasText;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, List<Tile> tiles, Category parentCategory, List<Category> subCategories) {
        this.subCategories = new ArrayList<>();
        this.name = name;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public Category(String name, Integer rows, Integer columns, String image) {
        this.subCategories = new ArrayList<>();
        this.rows = rows;
        this.columns = columns;
        this.name = name;
        this.image = image;
    }

    public Category(String name, String image) {
        this.subCategories = new ArrayList<>();
        this.name = name;
        this.image = image;
    }

    public Category() {
//        this.parentCategory = new Category();
        this.subCategories = new ArrayList<>();
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean hasSound() {
        return hasSound;
    }

    public void setHasSound(boolean hasSound) {
        this.hasSound = hasSound;
    }

    public boolean hasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public boolean hasText() {
        return hasText;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Category{" + "name=" + name + '}';
    }

}
