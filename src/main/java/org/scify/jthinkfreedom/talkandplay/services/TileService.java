package org.scify.jthinkfreedom.talkandplay.services;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.scify.jthinkfreedom.talkandplay.models.Category;
import org.scify.jthinkfreedom.talkandplay.models.Tile;
import org.scify.jthinkfreedom.talkandplay.models.User;
import org.scify.jthinkfreedom.talkandplay.utils.ConfigurationHandler;

public class TileService {

    private ConfigurationHandler configurationHandler;
    String projectPath;

    public TileService() {
        configurationHandler = new ConfigurationHandler();
    }

    /**
     * Save a new image
     *
     * @param category
     * @param user
     */
    public void save(Tile image, User user) throws Exception {

        Element profile = configurationHandler.getProfileElement(user.getName());

        if (profile != null) {

            Element tile = new Element("tile");
            tile.addContent(new Element("name").setText(image.getName()));
            tile.addContent(new Element("image").setText(image.getImage()));
            tile.addContent(new Element("sound").setText(image.getSound()));
            tile.addContent(new Element("order").setText(String.valueOf(image.getOrder())));

            attachToParent(profile.getChild("communication").getChild("categories"), image.getCategory().getName(), tile);

            configurationHandler.writeToXmlFile();
        }
    }

    /**
     * Update a tile
     *
     * @param category
     * @param user
     */
    public List<Category> update(Category category, User user, String oldName) throws Exception {

        return null;
        /* Element profile = configurationHandler.getProfileElement(user.getName());

         if (profile != null) {
         Element categoryChild = new Element("category");
         categoryChild.setAttribute(new Attribute("name", category.getName()));
         categoryChild.addContent(new Element("rows").setText(String.valueOf(category.getRows())));
         categoryChild.addContent(new Element("columns").setText(String.valueOf(category.getColumns())));
         categoryChild.addContent(new Element("image").setText(category.getImage()));

         updateToParent(profile.getChild("communication").getChild("categories"), oldName, category);

         configurationHandler.writeToXmlFile();
         return configurationHandler.getUser(user.getName()).getCategories();
         } else {
         return null;
         }*/
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
     * Find the category parent and add the tile
     *
     * @param categoryNode
     * @param name
     * @return
     */
    private Element attachToParent(Element categoryNode, String name, Element tile) {

        if (name.equals(categoryNode.getAttributeValue("name"))) {

            if (categoryNode.getChild("tiles") == null) {
                Element tiles = new Element("tiles");
                tiles.addContent(tile);
                categoryNode.addContent(tiles);
            } else {
                categoryNode.getChild("tiles").addContent(tile);
            }
            return categoryNode;

        } else {

            for (int i = 0; i < categoryNode.getChildren().size(); i++) {
                Element categoryEl = (Element) categoryNode.getChildren().get(i);
                attachToParent(categoryEl, name, tile);
            }
        }
        return categoryNode;
    }

    /**
     * For a given category, find the tile and update it
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
            categoryNode.getChild("image").setText(categoryChild.getImage());

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
