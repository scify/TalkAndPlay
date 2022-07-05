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
package org.scify.talkandplay.gui.helpers;

import io.sentry.Sentry;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static final String lightBlue = "#add8e6";

    public static final String blue = "#229ac1";

    public static final String mainFont = "DejaVu Sans";

    private String path;

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

    public String getPath() {
        try {
            return new File(UIConstants.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getAbsolutePath() + File.separator;
        } catch (URISyntaxException ex) {
            Logger.getLogger(UIConstants.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.captureMessage(ex.getMessage());
            return "";
        }
    }

}
