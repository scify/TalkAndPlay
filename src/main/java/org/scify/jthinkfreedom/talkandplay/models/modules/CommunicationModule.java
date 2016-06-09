package org.scify.jthinkfreedom.talkandplay.models.modules;

import java.util.List;
import org.scify.jthinkfreedom.talkandplay.models.Category;

public class CommunicationModule extends Module {

    private int rows;
    private int columns;
    private List<Category> categories;

    public CommunicationModule() {
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;

    }

}
