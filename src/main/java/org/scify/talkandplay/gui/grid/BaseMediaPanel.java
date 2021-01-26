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
package org.scify.talkandplay.gui.grid;

import org.scify.talkandplay.gui.grid.selectors.Selector;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.scify.talkandplay.gui.grid.entertainment.EntertainmentPanel;
import org.scify.talkandplay.gui.grid.entertainment.FilesPanel;
import org.scify.talkandplay.gui.grid.entertainment.MediaPlayerPanel;
import org.scify.talkandplay.gui.grid.selectors.ButtonSelector;
import org.scify.talkandplay.gui.grid.selectors.ManualButtonSelector;
import org.scify.talkandplay.gui.grid.selectors.MouseSelector;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.ResourceType;

public class BaseMediaPanel extends BasePanel {

    protected FilesPanel filesPanel;
    protected ArrayList<JPanel> controlsList;
    protected Selector selector;
    protected MediaPlayerPanel mediaPlayerPanel;
    protected ArrayList<File> files;
    protected GridBagConstraints c;
    protected String currentFile;
    protected String[] fileExtensions;

    protected SensorService sensorService;

    public BaseMediaPanel(User user, GridFrame parent, String filesPath, String[] fileExtensions) {
        super(user, parent);
        this.sensorService = new SensorService(user);
        this.controlsList = new ArrayList();
        this.mediaPlayerPanel = new MediaPlayerPanel(this);
        this.files = new ArrayList();
        this.currentFile = "";
        this.c = new GridBagConstraints();
        this.fileExtensions = fileExtensions;
        
        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            this.selector = new MouseSelector(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else if (user.getConfiguration().getNavigationSensor() != null) {
            this.selector = new ManualButtonSelector(user, null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else {
            this.selector = new ButtonSelector(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        }
        
        if (!isEmpty(filesPath)) {
            File[] tmpFiles = (new File(filesPath)).listFiles();
            Collections.addAll(this.files, tmpFiles);
            prepareFiles();
        }
    }

    public void initLayout() {
        setWindowListener();
        
        setBorder(new EmptyBorder(0, 20, 20, 20));
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
    }

    public void playFile(String fileName) {
        return;
    }

    public Selector getSelector() {
        return selector;
    }

    public List<JPanel> getControlsList() {
        return controlsList;
    }

    public boolean isEmpty(String filesPath) {
        if (filesPath == null
                || filesPath.isEmpty()
                || !(new File(filesPath).exists())) {
            return true;
        }
        return false;
    }

    protected void drawEmpty() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel noFiles = new JLabel(rm.getTextOfXMLTag("noFilesFound"));
        noFiles.setBorder(new EmptyBorder(5, 5, 5, 5));
        noFiles.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        noFiles.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(rm.getTextOfXMLTag("back"));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        label.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel(new ImageIcon(rm.getImage("back-icon.png", ResourceType.FROM_JAR).getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        icon.setBorder(new EmptyBorder(5, 5, 5, 5));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setPreferredSize(new Dimension(180, 130));
        panel.setMaximumSize(new Dimension(180, 130));
        panel.setMinimumSize(new Dimension(180, 130));
        panel.setBorder((new LineBorder(Color.white, 10)));
        panel.add(icon);
        panel.add(label);

        add(noFiles);
        add(panel);
        controlsList.add(panel);
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    goBack();
                }
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    goBack();
                }
            }
        });
    }

    public void goBack() {
        selector.cancel();
        parent.clearGrid();
        EntertainmentPanel entPanel = new EntertainmentPanel(user, parent);
    }

    /**
     * Remove the directories and keep only the files
     */
    protected void prepareFiles() {
        String extension;

        Iterator<File> i = files.iterator();
        while (i.hasNext()) {
            File f = i.next();
            if (f.isDirectory()) {
                i.remove();
            } else {
                extension = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());
                if (!Arrays.asList(fileExtensions).contains(extension)) {
                    i.remove();
                }
            }
        }
    }
    
    private void setWindowListener() {
        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                mediaPlayerPanel.stop();
            }
        });
    }
}
