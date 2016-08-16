/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.selectors;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JPanel;
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
            /*  if (selected == 0 || (selected < panelList.size() - 1 && selected > 0)) {
             selected++;
             } else if (selected == panelList.size() - 1) {
             selected = 0;
             }

             unselectAll();
             setSelected(selected);

             panelList.get(selected).setFocusable(true);
             panelList.get(selected).grabFocus();*/
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
