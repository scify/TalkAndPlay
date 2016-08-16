package org.scify.talkandplay.gui.grid.selectors;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JPanel;
import org.scify.talkandplay.models.Tile;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.Sensor;
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
        if (selected == 0 || (selected < panelList.size() - 1 && selected > 0)) {
            selected++;
        } else if (selected == panelList.size() - 1) {
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
