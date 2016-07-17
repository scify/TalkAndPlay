package org.scify.talkandplay.gui.helpers;

public class UIConstants {

    private static UIConstants singleton = new UIConstants();

    private UIConstants() {
    }

    public static UIConstants getInstance() {
        return singleton;
    }

    public static final String disabledColor = "#A6AAA9";

    public static final String green = "#4BA145";

    public static final String grey = "#d2d4d3";

    public static final String lightBlue = "#def7ff";

    public static final String blue = "#5bd8ff";

    public static final String mainFont = "DejaVu Sans";

    protected int width, height, rows, columns;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

}
