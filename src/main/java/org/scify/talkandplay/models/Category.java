/**
* Copyright 2016 SciFY
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.scify.talkandplay.models;

import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.SoundResource;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private ImageResource image;
    private SoundResource sound;
    private Integer rows, columns;
    private int order;
    private boolean editable;
    private boolean enabled;
    private Category parentCategory;
    private List<Category> subCategories;
    private boolean hasSound;
    private boolean hasImage;
    private boolean hasText;
    protected ResourceManager rm;

    public Category(String name) {
        this.rm = ResourceManager.getInstance();
        this.name = name;
    }

    public Category(String name, List<Tile> tiles, Category parentCategory, List<Category> subCategories) {
        this.rm = ResourceManager.getInstance();
        this.subCategories = new ArrayList<>();
        this.name = name;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public Category(String name, Integer rows, Integer columns, ImageResource image) {
        this.rm = ResourceManager.getInstance();
        this.subCategories = new ArrayList<>();
        this.rows = rows;
        this.columns = columns;
        this.name = name;
        this.image = image;
    }

    public Category(String name, ImageResource image) {
        this.rm = ResourceManager.getInstance();
        this.subCategories = new ArrayList<>();
        this.name = name;
        this.image = image;
    }

    public Category() {
        this.rm = ResourceManager.getInstance();
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
        return rm.decodeTextIfRequired(name);
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

    public ImageResource getImage() {
        return image;
    }

    public void setImage(ImageResource image) {
        this.image = image;
    }

    public SoundResource getSound() {
        return sound;
    }

    public void setSound(SoundResource sound) {
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
