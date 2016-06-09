package org.scify.jthinkfreedom.talkandplay.models.sensors;

/**
 * The Mouse Sensor holds the button clicked (left, middle or right click) and
 * the number of clicks.
 *
 * @author christina
 */
public class MouseSensor extends Sensor {

    private int button;
    private int clickCount;

    public MouseSensor() {
        super();
    }

    public MouseSensor(int button, int clickCount, String name) {
        super(name);
        this.button = button;
        this.clickCount = clickCount;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

}
