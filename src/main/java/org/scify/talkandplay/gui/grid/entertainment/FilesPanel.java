/**
 * Copyright 2016 SciFY
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
package org.scify.talkandplay.gui.grid.entertainment;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.scify.talkandplay.gui.grid.BaseMediaPanel;
import org.scify.talkandplay.gui.grid.selectors.Selector;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.Tile;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;

public class FilesPanel extends javax.swing.JPanel {

    private User user;
    private List<File> files;
    private int start, end, step, empties;
    private List<JPanel> panelList;
    private List<String> currentFiles;
    private ArrayList<Tile> tileList;
    private BaseMediaPanel parent;
    private JPanel prevSongs, nextSongs, controls, back;
    private String currentFile;
    private Selector selector;
    private SensorService sensorService;
    protected ResourceManager rm;

    public FilesPanel(User user, List<File> files, BaseMediaPanel parent) {
        this.user = user;
        this.files = new ArrayList();
        this.tileList = new ArrayList();
        this.parent = parent;
        this.selector = parent.getSelector();
        this.sensorService = new SensorService(user);
        this.files = files;
        this.rm = ResourceManager.getInstance();
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.white);

        selector.setDefaultBackgroundColor(UIConstants.grey);

        step = user.getEntertainmentModule().getMusicModule().getPlaylistSize() - 3;
        start = 0;
        if (files.size() < step) {
            end = files.size();
        } else {
            end = step;
        }
        drawFiles();
    }

    private void drawFiles() {
        removeAll();
        panelList = new ArrayList();
        currentFiles = new ArrayList();

        empties = 0;

        String prevText, nextText, backText;
        if (parent instanceof MusicPanel) {
            prevText = rm.getTextOfXMLTag("previousSongs");
            nextText = rm.getTextOfXMLTag("nextSongs");
            backText = rm.getTextOfXMLTag("musicControl");
        } else {
            prevText = rm.getTextOfXMLTag("previousVideos");
            nextText = rm.getTextOfXMLTag("nextVideos");
            backText = rm.getTextOfXMLTag("back");
        }

        if (start == 0) {
            prevSongs = drawControl(prevText, new ImageIcon(rm.getImage("left-icon-disabled.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "icon");
            prevSongs.getComponent(1).setForeground(Color.decode(UIConstants.disabledColor));
        } else {
            prevSongs = drawControl(prevText, new ImageIcon(rm.getImage("left-icon.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "icon");
        }
        if (end >= files.size()) {
            nextSongs = drawControl(nextText, new ImageIcon(rm.getImage("right-icon-disabled.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "text");
            nextSongs.getComponent(0).setForeground(Color.decode(UIConstants.disabledColor));
        } else {
            nextSongs = drawControl(nextText, new ImageIcon(rm.getImage("right-icon.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "text");
        }

        controls = drawControl(backText, new ImageIcon(rm.getImage("down-icon.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "icon");
        back = drawControl(backText, new ImageIcon(rm.getImage("back-icon.png", ResourceType.FROM_JAR).getScaledInstance(20, 20, Image.SCALE_SMOOTH)), "icon");

        add(prevSongs);
        panelList.add(prevSongs);

        for (int i = start; i < end; i++) {
            if (i >= files.size()) {
                empties++;
            } else {
                JPanel panel = drawFile(files.get(i).getName(), i + 1);
                add(panel);
                panelList.add(panel);
                currentFiles.add(files.get(i).getName());
                addFileListener(panel, files.get(i));
            }
        }

        add(nextSongs);
        if (parent instanceof MusicPanel) {
            add(controls);
        } else if (parent instanceof VideoPanel) {
            add(back);
        }

        for (int i = 0; i < empties; i++) {
            add(drawEmpty());
        }

        panelList.add(nextSongs);

        if (parent instanceof MusicPanel) {
            panelList.add(controls);
        } else if (parent instanceof VideoPanel) {
            panelList.add(back);
        }

        addListeners();
        revalidate();
        repaint();
    }

    private JPanel drawControl(String text, ImageIcon icon, String first) {
        JLabel iconLabel = new JLabel();
        JLabel fileLabel = new JLabel(text);
        iconLabel.setIcon(icon);

        fileLabel.setFont(new Font(UIConstants.mainFont, Font.BOLD, 18));
        fileLabel.setBorder(new EmptyBorder(3, 3, 3, 3));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setBorder((new LineBorder(Color.white, 5)));

        if ("icon".equals(first)) {
            panel.add(iconLabel);
            panel.add(fileLabel);
        } else {
            panel.add(fileLabel);
            panel.add(iconLabel);
        }

        return panel;
    }

    private JPanel drawFile(String text, int i) {
        JLabel numberLabel = new JLabel(i + ". ");
        JLabel fileLabel = new JLabel(text);

        numberLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        numberLabel.setBorder(new EmptyBorder(3, 3, 3, 0));
        fileLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        fileLabel.setBorder(new EmptyBorder(3, 0, 3, 0));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setBorder((new LineBorder(Color.white, 5)));

        panel.add(numberLabel);
        panel.add(fileLabel);

        return panel;
    }

    private JPanel drawEmpty() {

        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        emptyLabel.setBorder(new EmptyBorder(3, 3, 3, 3));

        JPanel empty = new JPanel();
        empty.setLayout(new FlowLayout());
        empty.setBackground(Color.white);
        empty.setBorder((new LineBorder(Color.white, 5)));

        empty.add(emptyLabel);

        return empty;
    }

    private void addListeners() {

        prevSongs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    if (files.size() > step && start > 0) {
                        selector.cancel();
                        selector.unselectAll();
                        configurePrevSongs();
                        drawFiles();

                        selector.setList(panelList);
                        selector.start();
                    }
                }
            }
        });
        prevSongs.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    if (files.size() > step && start > 0) {
                        selector.cancel();
                        configurePrevSongs();
                        drawFiles();

                        selector.setList(panelList);
                        selector.start();
                    }
                }
            }
        });

        nextSongs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    if (files.size() > step && end < files.size()) {
                        selector.cancel();
                        configureNextSongs();
                        drawFiles();

                        selector.setList(panelList);
                        selector.start();
                    }
                }
            }
        });
        nextSongs.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    if (files.size() > step && end < files.size()) {
                        selector.cancel();
                        configureNextSongs();
                        drawFiles();

                        selector.setList(panelList);
                        selector.start();
                    }
                }
            }
        });

        if (parent instanceof MusicPanel) {
            controls.addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        selector.cancel();
                        selector.unselect();
                        selector.setList(parent.getControlsList());
                        selector.start();
                    }
                }
            });
            controls.addKeyListener(new KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        selector.cancel();
                        selector.unselect();
                        selector.setList(parent.getControlsList());
                        selector.start();
                    }
                }
            });
        } else if (parent instanceof VideoPanel) {
            back.addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        if (parent instanceof VideoPanel) {
                            ((VideoPanel) parent).getMediaPlayer().release();
                        }
                        selector.cancel();
                        parent.goBack();
                    }
                }
            });
            back.addKeyListener(new KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        if (parent instanceof VideoPanel) {
                            ((VideoPanel) parent).getMediaPlayer().release();
                        }
                        selector.cancel();
                        parent.goBack();
                    }
                }
            });
        }
    }

    private void addFileListener(JPanel panel, final File file) {
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    currentFile = file.getName();
                    setSelected(currentFile);
                    parent.playFile(currentFile);
                }
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    currentFile = file.getName();
                    setSelected(currentFile);
                    parent.playFile(currentFile);
                }
            }
        });
    }

    private void configureNextSongs() {
        if (end <= files.size()) {
            start = end;
            end += step;
        }
    }

    private void configurePrevSongs() {
        if (start >= step) {
            end = start;
            start -= step;
        }
    }

    public List<JPanel> getPanelList() {
        return panelList;
    }

    public List<String> getFileList() {
        return currentFiles;
    }

    public int getSelected() {
        for (int i = 0; i < currentFiles.size(); i++) {
            if (currentFile.equals(currentFiles.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void setSelected(String selected) {
        currentFile = selected;
        for (int i = 0; i < panelList.size(); i++) {
            if (currentFile.equals(((JLabel) panelList.get(i).getComponent(1)).getText())) {
                panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(selector.getBorderColor()), selector.getBorderSize()));
                panelList.get(i).setBackground(Color.decode(selector.getBackgroundColor()));
            } else {
                panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, selector.getBorderSize()));
                panelList.get(i).setBackground(Color.decode(selector.getDefaultBackgroundColor()));
            }
        }
    }

    public void setSelected(int selected) {
        currentFile = currentFiles.get(selected);//((JLabel) panelList.get(selected).getComponent(0)).getText();

        for (int i = 0; i < panelList.size(); i++) {
            if (currentFile.equals(((JLabel) panelList.get(i).getComponent(1)).getText())) {
                panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(selector.getBorderColor()), selector.getBorderSize()));
                panelList.get(i).setBackground(Color.decode(selector.getBackgroundColor()));
            } else {
                panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, selector.getBorderSize()));
                panelList.get(i).setBackground(Color.decode(selector.getDefaultBackgroundColor()));
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
