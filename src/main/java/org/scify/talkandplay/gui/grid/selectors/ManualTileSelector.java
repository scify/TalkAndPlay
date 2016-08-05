package org.scify.talkandplay.gui.grid.selectors;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JPanel;
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
public class ManualTileSelector extends TileSelector {

    protected int BORDER_SIZE = 5;
    private SensorService sensorService;
    private User user;
    private int currI;

    public ManualTileSelector(User user, List<JPanel> panelList, long nextExecutionTime, long period) {
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
        //  panelList.get(selected).requestFocusInWindow();

        setFocusListeners(selected);

        /*  panelList.get(selected).addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent ke) {
         Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
         if (sensorService.shouldNavigate(sensor)) {

         if (selected == 0 || (selected < panelList.size() - 1 && selected > 0)) {
         selected++;
         } else if (selected == panelList.size() - 1) {
         selected = 0;
         }

         unselectAll();
         setSelected(selected);

         panelList.get(selected).setFocusable(true);
         panelList.get(selected).grabFocus();

         setFocusListeners(selected);

         System.out.println("selected " + selected);
         //  panelList.get(selected).requestFocus();
         //  panelList.get(selected).requestFocusInWindow();
         }
         }
         });*/
        for (int i = 0; i < panelList.size(); i++) {
            currI = i;
            panelList.get(i).addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldNavigate(sensor)) {
                        if (selected == currI) {

                            if (selected == 0 || (selected < panelList.size() - 1 && selected > 0)) {
                                selected++;
                            } else if (selected == panelList.size() - 1) {
                                selected = 0;
                            }

                            unselectAll();
                            setSelected(selected);

                            panelList.get(selected).setFocusable(true);
                            panelList.get(selected).grabFocus();

                            setFocusListeners(selected);

                            System.out.println("selected " + selected);
                        }
                    }
                }
            });
        }
    }

    private void setFocusListeners(final int selected) {
        panelList.get(selected).addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                System.out.println("pressed key ");
            }
        });
        /*  panelList.get(selected).addFocusListener(new FocusAdapter() {
         public void focusLost(FocusEvent ev) {
         System.out.println(selected + " lost focus");
         }

         public void focusGained(FocusEvent fe) {
         System.out.println(selected + " gained focus");
         }
         });*/
    }

    @Override
    public void cancel() {

    }

}
