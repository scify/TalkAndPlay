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
package org.scify.talkandplay.models;

import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

public class Configuration {

    private int rotationSpeed;
    private int defaultGridRow;
    private int defaultGridColumn;
    private boolean sound;
    private boolean image;
    private boolean text;

    private Sensor selectionSensor;
    private Sensor navigationSensor;

    public Configuration() {

    }

    public Configuration(Configuration configuration) {
        rotationSpeed = configuration.rotationSpeed;
        defaultGridRow = configuration.defaultGridRow;
        defaultGridColumn = configuration.defaultGridColumn;
        sound = configuration.sound;
        image = configuration.image;
        text = configuration.text;

        selectionSensor = null;
        if (configuration.selectionSensor != null) {
            if (configuration.selectionSensor instanceof MouseSensor)
                selectionSensor = new MouseSensor((MouseSensor) configuration.selectionSensor);
            else if (configuration.selectionSensor instanceof KeyboardSensor)
                selectionSensor = new KeyboardSensor((KeyboardSensor) configuration.selectionSensor);
        }

        navigationSensor = null;
        if (configuration.navigationSensor != null) {
            if (configuration.navigationSensor instanceof MouseSensor)
                navigationSensor = new MouseSensor((MouseSensor) configuration.navigationSensor);
            else if (configuration.navigationSensor instanceof KeyboardSensor)
                navigationSensor = new KeyboardSensor((KeyboardSensor) configuration.navigationSensor);
        }

    }

    public boolean isAltered(Configuration configuration) {
        if (configuration.rotationSpeed != rotationSpeed ||
                configuration.defaultGridRow != defaultGridRow ||
                configuration.defaultGridColumn != defaultGridColumn ||
                configuration.sound != sound ||
                configuration.image != image ||
                configuration.text != text)
            return true;

        if (selectionSensor != null && selectionSensor.isAltered(configuration.selectionSensor) ||
                selectionSensor == null && configuration.selectionSensor != null)
            return true;

        if (navigationSensor != null && navigationSensor.isAltered(configuration.navigationSensor) ||
                navigationSensor == null && configuration.navigationSensor != null)
            return true;

        return false;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getDefaultGridRow() {
        return defaultGridRow;
    }

    public void setDefaultGridRow(int defaultGridRow) {
        this.defaultGridRow = defaultGridRow;
    }

    public int getDefaultGridColumn() {
        return defaultGridColumn;
    }

    public void setDefaultGridColumn(int defaultGridColumn) {
        this.defaultGridColumn = defaultGridColumn;
    }

    public Sensor getSelectionSensor() {
        return selectionSensor;
    }

    public void setSelectionSensor(Sensor selectionSensor) {
        this.selectionSensor = selectionSensor;
    }

    public Sensor getNavigationSensor() {
        return navigationSensor;
    }

    public void setNavigationSensor(Sensor navigationSensor) {
        this.navigationSensor = navigationSensor;
    }

    public boolean hasSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean hasImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean hasText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }
}
