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
package org.scify.talkandplay.gui.grid.selectors;

import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JPanel;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.SensorService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Selector used when the manual scanning is configured
 *
 * @author christina
 */
public class ManualButtonSelector extends ButtonSelector {

    private SensorService sensorService;
    private User user;

    public ManualButtonSelector(User user, List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);

        this.user = user;
        this.sensorService = new SensorService(user);
    }

    @Override
    public void start() {
        selected = 0;
        unselectAll();
        setSelected(selected);
        panelList.get(selected).setFocusable(true);
        panelList.get(selected).grabFocus();
        addListeners();
    }

    @Override
    public void addListeners() {
        for (int i = 0; i < panelList.size(); i++) {

            for (KeyListener listener : panelList.get(i).getKeyListeners()) {
                if (listener instanceof ManualListener) {
                    panelList.get(i).removeKeyListener(listener);
                }
            }

            panelList.get(i).addKeyListener(new ManualListener(this, sensorService));
        }
    }

    public void act() {
        if (selected == 0) {
            if (panelList.size() > 1)
                selected ++;
        }else if (selected < panelList.size() - 1) {
            selected++;
        } else  {
            selected = 0;
        }

        unselectAll();
        setSelected(selected);

        panelList.get(selected).setFocusable(true);
        panelList.get(selected).grabFocus();
    }

    @Override
    public void cancel() {
    }
}
