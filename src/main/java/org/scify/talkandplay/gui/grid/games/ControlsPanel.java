/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import org.scify.talkandplay.gui.grid.selectors.ButtonSelector;
import org.scify.talkandplay.gui.grid.selectors.ManualButtonSelector;
import org.scify.talkandplay.gui.grid.selectors.MouseSelector;
import org.scify.talkandplay.gui.grid.selectors.Selector;
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
    private JPanel parent, newGamePanel, playAgainPanel, exitPanel;
    private List<JPanel> controls;
    private Selector selector;
    
    public ControlsPanel(User user, JPanel parent) {
        this.sensorService = new SensorService(user);
        this.user = user;
        this.parent = parent;
        
        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            this.selector = new MouseSelector(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else if (user.getConfiguration().getNavigationSensor() != null) {
            this.selector = new ManualButtonSelector(user, null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else {
            this.selector = new ButtonSelector(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        }
        
        this.selector.setDefaultBackgroundColor(UIConstants.grey);
        
        initComponents();
        initCustomComponents();
    }
    
    private void initCustomComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        
        controls = new ArrayList();
        
        JLabel congratsLabel = new JLabel(Message.getRandomCongrats());
        congratsLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 30));
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        congratsLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        JPanel congratsPanel = new JPanel();
        congratsPanel.setBackground(Color.white);
        congratsPanel.setLayout(new FlowLayout());
        congratsPanel.add(congratsLabel);
        
        newGamePanel = drawControl("Νέο παιχνίδι");
        playAgainPanel = drawControl("Παίξε το ίδιο ξανά");
        exitPanel = drawControl("Έξοδος");
        
        c.gridwidth=3;
        add(congratsPanel, c);
        c.gridwidth=1;
        c.gridy++;
        add(newGamePanel, c);
        c.gridx++;
        add(playAgainPanel, c);
        c.gridx++;
        add(exitPanel, c);
        
        newGamePanel.setVisible(false);
        playAgainPanel.setVisible(false);
        exitPanel.setVisible(false);
        
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
        } else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).newGame();
        } else if (parent instanceof SimilarityGamePanel) {
            ((SimilarityGamePanel) parent).newGame();
        }
    }
    
    private void playAgain() {
        if (parent instanceof StimulusReactionGamePanel) {
            ((StimulusReactionGamePanel) parent).playAgain();
        } else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).playAgain();
        } else if (parent instanceof SimilarityGamePanel) {
            ((SimilarityGamePanel) parent).playAgain();
        }
    }
    
    private void exit() {
        if (parent instanceof StimulusReactionGamePanel) {
            ((StimulusReactionGamePanel) parent).exit();
        } else if (parent instanceof SequenceGamePanel) {
            ((SequenceGamePanel) parent).exit();
        } else if (parent instanceof SimilarityGamePanel) {
            ((SimilarityGamePanel) parent).exit();
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
    
    public Selector getSelector() {
        return selector;
    }
    
    public void showControls() {
        newGamePanel.setVisible(true);
        playAgainPanel.setVisible(true);
        exitPanel.setVisible(true);
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
