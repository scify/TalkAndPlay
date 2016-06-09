package org.scify.jthinkfreedom.talkandplay.models.sensors;

/**
 * A Sensor can be a mouse or keyboard
 *
 * @author christina
 */
public class Sensor {

    private String name;

    public Sensor() {
    }

    public Sensor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
