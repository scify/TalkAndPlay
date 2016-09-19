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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.selectors;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;

/**
 *
 * @author christina
 */
public class ManualListener implements KeyListener {

    private Selector selector;
    private SensorService sensorService;

    public ManualListener(Selector selector, SensorService sensorService) {
        this.selector = selector;
        this.sensorService = sensorService;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
        if (sensorService.shouldNavigate(sensor)) {
            selector.act();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
