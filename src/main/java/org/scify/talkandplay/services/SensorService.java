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
package org.scify.talkandplay.services;

import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

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
            if (((KeyboardSensor) sensor).getKeyCode() == ((KeyboardSensor) user.getConfiguration().getSelectionSensor()).getKeyCode()) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldNavigate(Sensor sensor) {

        if (sensor instanceof MouseSensor && user.getConfiguration().getNavigationSensor()instanceof MouseSensor) {
            if (((MouseSensor) sensor).getButton() == ((MouseSensor) user.getConfiguration().getNavigationSensor()).getButton()
                    && ((MouseSensor) sensor).getClickCount() == ((MouseSensor) user.getConfiguration().getNavigationSensor()).getClickCount()) {
                return true;
            }
        } else if (sensor instanceof KeyboardSensor && user.getConfiguration().getNavigationSensor() instanceof KeyboardSensor) {
            if (((KeyboardSensor) sensor).getKeyCode() == ((KeyboardSensor) user.getConfiguration().getNavigationSensor()).getKeyCode()) {
                return true;
            }
        }
        return false;
    }
}
