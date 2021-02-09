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
        categories.add(category);
        categoriesDict.put(category.getNameUnmodified(), categories.size() - 1);
        HashSet<String> languages = new HashSet<>();
        supportedLangPerCat.put(category.getNameUnmodified(), languages);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addLanguageToCategory(String category, String language) {
        supportedLangPerCat.get(category).add(language);
    }

    public boolean isLanguageSupportedByCategory (String category, String language) {
        if (supportedLangPerCat.get(category).contains(language))
            return true;
        else
            return false;
    }

    public String getSupportedLanguages (String category) {
        String languages = "";
        for (String language: supportedLangPerCat.get(category)) {
            languages = language + " ";
        }
        return languages.trim();
    }

    public Category getCategory(String categoryName) {
        int index = categoriesDict.get(categoryName);
        return categories.get(index);
    }

}
