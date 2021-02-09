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
package org.scify.talkandplay.models.sensors;

/**
 * The Mouse Sensor holds the button clicked (left, middle or right click) and
 * the number of clicks.
 *
 * @author christina
 */
public class MouseSensor extends Sensor {

    private int button;
    private int clickCount;

    public MouseSensor(int button, int clickCount, String name) {
        super(name);
        this.button = button;
        this.clickCount = clickCount;
    }

    public MouseSensor getCopy() {
        return new MouseSensor(button, clickCount, name);
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

    @Override
    public boolean isAltered(Sensor sensor) {
        if (super.isAltered(sensor))
            return true;
        else if (sensor instanceof MouseSensor) {
            MouseSensor s = (MouseSensor) sensor;
            if (s.button != button)
                return true;
            if (s.clickCount != button)
                return true;
            return false;
        } else
            return true;
    }
}
