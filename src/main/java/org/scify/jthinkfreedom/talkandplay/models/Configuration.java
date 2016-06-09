package org.scify.jthinkfreedom.talkandplay.models;

import org.scify.jthinkfreedom.talkandplay.models.sensors.Sensor;

public class Configuration {

    private int rotationSpeed;
    private int defaultGridRow;
    private int defaultGridColumn;

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

}
