/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;

/**
 *
 * @author christina
 */
public class ControlsPanel extends javax.swing.JPanel {

    private User user;
    private SensorService sensorService;
    private JPanel parent;
    private List<JPanel> controls;

    public ControlsPanel(User user, JPanel parent) {
        this.sensorService = new SensorService(user);
        this.user = user;
        this.parent = parent;

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 20;
        c.weighty = 20;
        c.gridx = 0;
        c.gridy = 0;

        controls = new ArrayList();

        JLabel congratsLabel = new JLabel("Μπράβο!");
        congratsLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 24));
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel newGamePanel = drawControl("Νέο παιχνίδι");
        JPanel playAgainPanel = drawControl("Παίξε το ίδιο ξανά");
        JPanel exitPanel = drawControl("Έξοδος");

        add(congratsLabel, c);
        c.gridy++;
        add(newGamePanel, c);
        c.gridx++;
        add(playAgainPanel, c);
        c.gridx++;
        add(exitPanel, c);

        controls.add(newGamePanel);
        controls.add(playAgainPanel);
        controls.add(exitPanel);

        newGamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    newGame();
                }
            }
        });
        newGamePanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    newGame();
                }
            }
        });

        playAgainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    playAgain();
                }
            }
        });
        playAgainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    playAgain();
                }
            }
        });

        exitPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    exit();
                }
            }
        });
        exitPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    exit();
                }
            }
        });
    }

    private void newGame() {
        if (parent instanceof StimulusReactionGamePanel) {
            ((StimulusReactionGamePanel) parent).newGame();
        }
        else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).newGame();
        }
    }

    private void playAgain() {
        if (parent instanceof StimulusReactionGamePanel) {
            ((StimulusReactionGamePanel) parent).playAgain();
        }
        else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).playAgain();
        }
    }

    private void exit() {
        if (parent instanceof StimulusReactionGamePanel) {
            ((StimulusReactionGamePanel) parent).exit();
        }
        else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).exit();
        }
    }

    private JPanel drawControl(String text) {

        JLabel label = new JLabel(text);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        label.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setPreferredSize(new Dimension(200, 100));
        panel.setMaximumSize(new Dimension(200, 100));
        panel.setMinimumSize(new Dimension(200, 100));
        panel.setBorder((new LineBorder(Color.white, 5)));

        panel.add(label);
        return panel;
    }

    public List<JPanel> getControls() {
        return controls;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

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
