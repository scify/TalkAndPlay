package org.scify.jthinkfreedom.talkandplay.services;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.scify.jthinkfreedom.talkandplay.models.Category;
import org.scify.jthinkfreedom.talkandplay.models.User;
import org.scify.jthinkfreedom.talkandplay.utils.ConfigurationHandler;

public class CategoryService {

    private ConfigurationHandler configurationHandler;
    String projectPath;

    public CategoryService() {
        configurationHandler = new ConfigurationHandler();
    }

    public Category getCategory(String categoryName, String userName) {
        List<Category> categories = getCategories(userName);
        Category category = null;

        for (Category cat : categories) {
            if (cat.getName().equals(categoryName)) {
                category = cat;
            }
        }
        return category;
    }

    /**
     * Set all the categories, along with the modules that should be displayed
     *
     * @param userName
     * @return
     */
    public List<Category> getCategories(String userName) {
        List<Category> categories = new ArrayList<>();

        User user = configurationHandler.getUser(userName);

        //set the communication category
        if (user.getCommunicationModule().isEnabled()) {
            Category communication = new Category(user.getCommunicationModule().getName(),
                    user.getCommunicationModule().getRows(),
                    user.getCommunicationModule().getColumns(),
                    user.getCommunicationModule().getImage());

            List<Category> subCategories = new ArrayList<>();

            communication.setSubCategories(getCategories(user.getCommunicationModule().getCategories(), subCategories));
            categories.add(communication);
        }

        //set the entertainment category
        if (user.getEntertainmentModule().isEnabled()) {
            Category entertainment = new Category();
            entertainment.setName(user.getEntertainmentModule().getName());
            entertainment.setImage(user.getEntertainmentModule().getImage());

            categories.add(entertainment);
        }

        //set the games category
        if (user.getGameModule().isEnabled()) {
            Category games = new Category();
            games.setName(user.getGameModule().getName());
            games.setImage(user.getGameModule().getImage());

            categories.add(games);
        }

        return categories;
    }

    private List<Category> getCategories(List<Category> userCategories, List<Category> categories) {

        if (userCategories == null) {
            return categories;
        } else {
            for (Category category : userCategories) {
                categories.add(category);
                getCategories(category.getSubCategories(), categories);
            }
            return categories;
        }
    }

    public Category getCategoriesWithRootParent(User user) {
        Category communication = new Category(user.getCommunicationModule().getName(),
                user.getCommunicationModule().getRows(),
                user.getCommunicationModule().getColumns(),
                user.getCommunicationModule().getImage());

        List<Category> subCategories = user.getCommunicationModule().getCategories();

        for (Category category : subCategories) {
            category.setParentCategory(communication);
        }

        communication.setSubCategories(subCategories);

        return communication;
    }

    /**
     * Save a new category
     *
     * @param category
     * @param user
     */
    public void save(Category category, User user) throws Exception {

        Element profile = configurationHandler.getProfileElement(user.getName());

        if (profile != null) {

            Element categoryChild = new Element("category");
            categoryChild.setAttribute(new Attribute("name", category.getName()));
            categoryChild.addContent(new Element("rows").setText(String.valueOf(category.getRows())));
            categoryChild.addContent(new Element("columns").setText(String.valueOf(category.getColumns())));
            categoryChild.addContent(new Element("image").setText(category.getImage()));
            categoryChild.addContent(new Element("order").setText(String.valueOf(category.getOrder())));

            attachToParent(profile.getChild("communication").getChild("categories"), category.getParentCategory().getName(), categoryChild);

            configurationHandler.writeToXmlFile();
        }
    }

    /**
     * Update a category
     *
     * @param category
     * @param user
     */
    public List<Category> update(Category category, User user, String oldName) throws Exception {

        Element profile = configurationHandler.getProfileElement(user.getName());

        if (profile != null) {
            Element categoryChild = new Element("category");
            categoryChild.setAttribute(new Attribute("name", category.getName()));
            categoryChild.addContent(new Element("rows").setText(String.valueOf(category.getRows())));
            categoryChild.addContent(new Element("columns").setText(String.valueOf(category.getColumns())));
            categoryChild.addContent(new Element("image").setText(category.getImage()));
            categoryChild.addContent(new Element("order").setText(String.valueOf(category.getOrder())));

            updateToParent(profile.getChild("communication").getChild("categories"), oldName, category);

            configurationHandler.writeToXmlFile();
            return configurationHandler.getUser(user.getName()).getCommunicationModule().getCategories();
        } else {
            return null;
        }
    }

    /**
     * Delete a category
     *
     * @param category
     * @param user
     */
    public void delete(String categoryName, User user) throws Exception {
        Element profile = configurationHandler.getProfileElement(user.getName());

        if (profile != null) {

            deleteFromParent(profile.getChild("communication").getChild("categories"), categoryName);

            configurationHandler.writeToXmlFile();
        }
    }

    /**
     * Find the category parent and add the categoryChild
     *
     * @param categoryNode
     * @param name
     * @return
     */
    private Element attachToParent(Element categoryNode, String name, Element categoryChild) {

        if (name.equals(categoryNode.getAttributeValue("name"))) {

            if (categoryNode.getChild("categories") == null) {
                Element categories = new Element("categories");
                categories.addContent(categoryChild);
                categoryNode.addContent(categories);
            } else {
                categoryNode.getChild("categories").addContent(categoryChild);
            }

            return categoryNode;

        } else {

            for (int i = 0; i < categoryNode.getChildren().size(); i++) {
                Element categoryEl = (Element) categoryNode.getChildren().get(i);
                attachToParent(categoryEl, name, categoryChild);
            }
        }
        return categoryNode;
    }

    /**
     * For a given category, find the category and update it
     *
     * @param categoryNode
     * @param name
     * @param categoryChild
     * @return
     */
    private void updateToParent(Element categoryNode, String oldName, Category categoryChild) {

        if (oldName.equals(categoryNode.getAttributeValue("name"))) {

            categoryNode.getAttribute("name").setValue(categoryChild.getName());
            categoryNode.getChild("rows").setText(String.valueOf(categoryChild.getRows()));
            categoryNode.getChild("columns").setText(String.valueOf(categoryChild.getColumns()));
            categoryNode.getChild("order").setText(String.valueOf(categoryChild.getOrder()));

            if (categoryChild.getImage() == null) {
                categoryNode.getChild("image").setText(categoryNode.getChildText("image"));
            } else {
                categoryNode.getChild("image").setText(categoryChild.getImage());
            }
        } else {
            for (int i = 0; i < categoryNode.getChildren().size(); i++) {
                Element categoryEl = (Element) categoryNode.getChildren().get(i);
                updateToParent(categoryEl, oldName, categoryChild);
            }
        }
    }

    /**
     * Delete a given category
     *
     * @param categoryNode
     * @param categoryName
     */
    private void deleteFromParent(Element categoryNode, String categoryName) {

        if (categoryName.equals(categoryNode.getAttributeValue("name"))) {
            categoryNode.detach();
        } else {
            for (int i = 0; i < categoryNode.getChildren().size(); i++) {
                Element categoryEl = (Element) categoryNode.getChildren().get(i);
                deleteFromParent(categoryEl, categoryName);
            }
        }
    }

}
