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
