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
package org.scify.talkandplay.services;

import java.util.ArrayList;
import java.util.List;

import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.utils.TalkAndPlayProfileConfiguration;
import org.scify.talkandplay.utils.XMLConfigurationHandler;

public class CategoryService {

    private TalkAndPlayProfileConfiguration talkAndPlayProfilesConfigurationFile;

    public CategoryService() {
        this.talkAndPlayProfilesConfigurationFile = TalkAndPlayProfileConfiguration.getInstance();
    }

    public Category getCategoriesWithRootParent(User user) {
        Category communication = new Category(user.getCommunicationModule().getName(),
                user.getCommunicationModule().getRows(),
                user.getCommunicationModule().getColumns(),
                user.getCommunicationModule().getImageResource());

        List<Category> subCategories = user.getCommunicationModule().getCategoriesOfSelectedLanguage();
        for (Category category : subCategories) {
            category.setParentCategory(communication);
        }

        XMLConfigurationHandler ch = TalkAndPlayProfileConfiguration.getInstance().getConfigurationHandler();
        List<Category> categoriesOfDownloadedComModule = ch.getDownloadedCommunicationModule().getCategoriesOfSelectedLanguage();
        for (Category category : categoriesOfDownloadedComModule) {
            category.setParentCategory(communication);
        }
        categoriesOfDownloadedComModule.addAll(subCategories);
        communication.setSubCategories(categoriesOfDownloadedComModule);

        return communication;
    }

    public List<Category> getLinearCategories(User user) {
        List<Category> categories = new ArrayList();
        categories.add(new Category(user.getCommunicationModule().getName()));
        for (Category category : user.getCommunicationModule().getCategoriesOfSelectedLanguage()) {
            categories.add(category);
        }

        //getLinearCategories(categories, user.getCommunicationModule().getCategoriesOfSelectedLanguage());

        return categories;
    }

    protected void insertAsNewCategory(Category newCategory, User user) {
        Category parent = newCategory.getParentCategory();
        newCategory.setRows(user.getConfiguration().getDefaultGridRow());
        newCategory.setColumns(user.getConfiguration().getDefaultGridColumn());
        if (parent == null)
            user.getCommunicationModule().addCategory(newCategory);
        else
            parent.addSubcategory(newCategory);
    }

    public void save(Category oldCategory, Category newCategory, User user) throws Exception {
        if (oldCategory == null) {
            insertAsNewCategory(newCategory, user);
        } else if ((oldCategory.getParentCategory() == null && newCategory.getParentCategory() != null) ||
                (oldCategory.getParentCategory() != null && newCategory.getParentCategory() == null) ||
                (!oldCategory.getParentCategory().getNameUnmodified().equals(newCategory.getParentCategory().getNameUnmodified()))) {
            delete(oldCategory, user, false);
            insertAsNewCategory(newCategory, user);
        } else {
            if (oldCategory.getParentCategory() == null)
                user.getCommunicationModule().alterCategoryName(oldCategory.getNameUnmodified(), newCategory.getNameUnmodified());
            oldCategory.setName(newCategory.getNameUnmodified());
            oldCategory.setEnabled(newCategory.isEnabled());
            oldCategory.setImage(newCategory.getImage());
            oldCategory.setSound(newCategory.getSound());
        }
        talkAndPlayProfilesConfigurationFile.getConfigurationHandler().updateUser(user);
        talkAndPlayProfilesConfigurationFile.getConfigurationHandler().update();
    }

    public void delete(Category category, User user, boolean requestUpdate) throws Exception {
        Category parent = category.getParentCategory();
        if (parent == null)
            user.getCommunicationModule().deleteCategory(category.getNameUnmodified());
        else
            parent.deleteSubCategory(category.getNameUnmodified());

        if (requestUpdate) {
            talkAndPlayProfilesConfigurationFile.getConfigurationHandler().updateUser(user);
            talkAndPlayProfilesConfigurationFile.getConfigurationHandler().update();
        }
    }
}
