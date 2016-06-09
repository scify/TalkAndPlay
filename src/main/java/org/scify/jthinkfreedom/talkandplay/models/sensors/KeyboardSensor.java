package org.scify.jthinkfreedom.talkandplay.models.sensors;

/**
 * The Keyboard Sensor holds the key code (i.e 32, 68 etc) and the corresponding
 * character
 *
 * @author christina
 */
public class KeyboardSensor extends Sensor {

    private int keyCode;
    private char keyChar;

    public KeyboardSensor() {
        super();
    }

    public KeyboardSensor(int keyCode, char keyChar, String name) {
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

    public char getKeyChar() {
        return keyChar;
    }

    public void setKeyChar(char keyChar) {
        this.keyChar = keyChar;
    }

}
