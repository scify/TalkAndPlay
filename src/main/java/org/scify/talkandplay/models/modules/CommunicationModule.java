/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scify.talkandplay.models.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.scify.talkandplay.models.Category;

public class CommunicationModule extends Module {

    private int rows;
    private int columns;
    private List<Category> categories;
    private HashMap<String, Integer> categoriesDict;
    protected HashMap<String, HashSet<String>> supportedLangPerCat;

    public CommunicationModule() {
        super();
        categories = new ArrayList<>();
        categoriesDict = new HashMap<>();
        supportedLangPerCat = new HashMap<>();
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

    public void addCategory(Category category) {
        HashSet<String> supportedLanguages = new HashSet<>();
        supportedLanguages.add(rm.getSelectedLanguage());
        addCategory(category, supportedLanguages);
    }

    public void addCategory(Category category, HashSet<String> supportedLanguages) {
        String name = category.getNameUnmodified();
        categories.add(category);
        categoriesDict.put(name, categories.size() - 1);
        HashSet<String> languages = new HashSet<>();
        supportedLangPerCat.put(category.getNameUnmodified(), languages);
        for (String language: supportedLanguages)
            addLanguageToCategory(name, language);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void deleteCategory(String name) {

        int index = categoriesDict.get(name);
        categoriesDict.remove(name);
        categories.remove(index);
        supportedLangPerCat.remove(name);
    }

    public List<Category> getCategoriesOfSelectedLanguage() {
        List<Category> languageCategories = new ArrayList<>();
        for (Category category : categories) {
            if (isLanguageSupportedByCategory(category.getNameUnmodified(), rm.getSelectedLanguage()))
                languageCategories.add(category);
        }
        return languageCategories;
    }

    protected void addLanguageToCategory(String category, String language) {
        supportedLangPerCat.get(category).add(language);
    }

    protected boolean isLanguageSupportedByCategory(String category, String language) {
        if (supportedLangPerCat.get(category).contains(language))
            return true;
        else
            return false;
    }

    public void alterCategoryName(String oldName, String newName) {
        HashSet<String> languages = supportedLangPerCat.get(oldName);
        supportedLangPerCat.remove(oldName);
        supportedLangPerCat.put(newName, languages);

        int index = categoriesDict.get(oldName);
        categoriesDict.remove(oldName);
        categoriesDict.put(newName, index);
    }

    public HashSet<String> getSupportedLanguages(String category) {
        return supportedLangPerCat.get(category);
    }

    public Category getCategory(String categoryName) {
        if (categoriesDict.containsKey(categoryName)) {
            int index = categoriesDict.get(categoryName);
            return categories.get(index);
        } else {
            return null;
        }
    }

}
