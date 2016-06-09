package org.scify.jthinkfreedom.talkandplay.models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int rows, columns;
    private String name;
    private String image;
    private String sound;
    private int order;
    private boolean editable;
    private Category parentCategory;
    private List<Category> subCategories;
    private List<Tile> tiles;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, List<Tile> tiles, Category parentCategory, List<Category> subCategories) {
        this.tiles = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        this.name = name;
        this.tiles = tiles;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public Category(String name, int rows, int columns, String image) {
        this.tiles = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        this.rows = rows;
        this.columns = columns;
        this.name = name;
        this.image = image;
    }

    public Category() {
//        this.parentCategory = new Category();
        this.tiles = new ArrayList<>();
        this.subCategories = new ArrayList<>();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
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

    public List<Tile> getTiles() {
        return tiles;
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

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
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

    @Override
    public String toString() {
        return "Category{" + "name=" + name + '}';
    }

}
