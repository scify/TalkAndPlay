package org.scify.talkandplay.models.sensors;

/**
 * The Keyboard Sensor holds the key code (i.e 32, 68 etc) and the corresponding
 * character
 *
 * @author christina
 */
public class KeyboardSensor extends Sensor {

    private int keyCode;
    private String keyChar;

    public KeyboardSensor() {
        super();
    }

    public KeyboardSensor(int keyCode, String keyChar, String name) {
        super(name);
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyChar() {
        return keyChar;
    }

    public void setKeyChar(String keyChar) {
        this.keyChar = keyChar;
    }

}
