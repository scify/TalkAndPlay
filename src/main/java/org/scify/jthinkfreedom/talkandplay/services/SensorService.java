package org.scify.jthinkfreedom.talkandplay.services;

import org.scify.jthinkfreedom.talkandplay.models.User;
import org.scify.jthinkfreedom.talkandplay.models.sensors.KeyboardSensor;
import org.scify.jthinkfreedom.talkandplay.models.sensors.MouseSensor;
import org.scify.jthinkfreedom.talkandplay.models.sensors.Sensor;

/**
 * Service responsible for the sensor related actions
 *
 * @author christina
 */
public class SensorService {

    private User user;

    public SensorService(User user) {
        this.user = user;
    }

    public boolean shouldSelect(Sensor sensor) {

        if (sensor instanceof MouseSensor && user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            if (((MouseSensor) sensor).getButton() == ((MouseSensor) user.getConfiguration().getSelectionSensor()).getButton()
                    && ((MouseSensor) sensor).getClickCount() == ((MouseSensor) user.getConfiguration().getSelectionSensor()).getClickCount()) {
                return true;
            }
        } else if (sensor instanceof KeyboardSensor && user.getConfiguration().getSelectionSensor() instanceof KeyboardSensor) {
            if (((KeyboardSensor) sensor).getKeyChar() == ((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyChar()
                    && ((KeyboardSensor) sensor).getKeyCode() == ((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldNavigate(Sensor sensor) {

        /*   if (sensor instanceof MouseSensor) {
         if (((MouseSensor) sensor).getButton() == ((MouseSensor) user.getConfiguration().getSelectionSensor()).getButton()
         && ((MouseSensor) sensor).getClickCount() == ((MouseSensor) user.getConfiguration().getSelectionSensor()).getClickCount()) {
         return true;
         }
         } else if (sensor instanceof KeyboardSensor) {
         if (((KeyboardSensor) sensor).getKeyChar() == ((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyChar()
         && ((KeyboardSensor) sensor).getKeyCode() == ((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyCode()) {
         return true;
         }
         }*/
        return false;
    }
}
