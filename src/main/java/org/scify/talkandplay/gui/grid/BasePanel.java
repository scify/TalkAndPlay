package org.scify.talkandplay.gui.grid;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JPanel;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;

/**
 *
 * @author christina
 */
public class BasePanel {
    
    
    protected String name;
    protected String image;
    protected String sound;
    
    
     private JPanel createCategoryItem(final Category category) throws IOException {
        JPanel panel = guiHelper.createImagePanel(category.getImage(), category.getName(), parent);
        panelList.add(panel);
        imagesPanel.add(panel);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    categoryItemAction(category);
                }
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), evt.getKeyChar(), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    categoryItemAction(category);
                }
            }
        });
        return panel;
    }
     
     
            
}
