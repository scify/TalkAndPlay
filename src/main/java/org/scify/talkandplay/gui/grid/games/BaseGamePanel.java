/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.tiles.TileCreator;
import org.scify.talkandplay.gui.grid.timers.MouseTimerManager;
import org.scify.talkandplay.gui.grid.timers.TileTimerManager;
import org.scify.talkandplay.gui.grid.timers.TimerManager;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.models.games.SequenceGame;
import org.scify.talkandplay.models.games.SimilarityGame;
import org.scify.talkandplay.models.games.StimulusReactionGame;
import org.scify.talkandplay.models.sensors.MouseSensor;

public class BaseGamePanel extends javax.swing.JPanel {

    protected User user;
    protected GridFrame parent;
    protected Game game;
    protected GridBagConstraints c1;
    protected Random randomGenerator;
    protected TimerManager timer;
    protected TileCreator tileCreator;
    protected String type;
    protected JPanel topPanel, bottomPanel, topMsgPanel, bottomMsgPanel;
    protected List<GameImage> randomImages;
    protected ArrayList<JPanel> panelList;

    public BaseGamePanel(User user, GridFrame parent, String type, Game game) {
        this.user = user;
        this.parent = parent;
        this.type = type;
        this.game = game;

        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            this.timer = new MouseTimerManager(panelList, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else {
            this.timer = new TileTimerManager(panelList, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        }

        this.tileCreator = new TileCreator(user, user.getConfiguration().getDefaultGridRow(), user.getConfiguration().getDefaultGridColumn());

        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);

        topPanel = new JPanel();
        topPanel.setBackground(Color.white);
        topPanel.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode(UIConstants.grey)));
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.pink);
        bottomMsgPanel = new JPanel();
        bottomMsgPanel.setBackground(Color.white);
        topMsgPanel = new JPanel();
        topMsgPanel.setBackground(Color.white);
        topMsgPanel.setVisible(false);
        add(topMsgPanel);
        add(topPanel);
        add(bottomPanel);
        add(bottomMsgPanel);

        topPanel.setLayout(new GridBagLayout());
        bottomPanel.setLayout(new GridBagLayout());
        c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.BOTH;
        c1.anchor = GridBagConstraints.FIRST_LINE_START;
        c1.weightx = 20;
        c1.weighty = 20;
        c1.gridx = 0;
        c1.gridy = 0;

        panelList = new ArrayList();
        randomImages = new ArrayList();
        randomGenerator = new Random();

        if (game == null) {
            getRandomGame();
        }
        
        
    }

    /**
     * Select a random game based on its type
     *
     * @return
     */
    protected void getRandomGame() {
        for (GameType gameType : user.getGameModule().getGameTypes()) {
            if (type.equals(gameType.getType())) {
                for (int j = 0; j < gameType.getGames().size(); j++) {
                    int i = randomGenerator.nextInt(gameType.getGames().size());
                    if (gameType.getGames().get(i).isEnabled()) {

                        if (type.equals("stimulusReactionGame")) {
                            game = (StimulusReactionGame) gameType.getGames().get(i);
                        } else if (type.equals("sequenceGame")) {
                            game = (SequenceGame) gameType.getGames().get(i);
                        } else if (type.equals("similarityGame")) {
                            game = (SimilarityGame) gameType.getGames().get(i);
                        }
                        break;
                    }
                }
            }
        }
    }
    
    protected void setTopMessage(String text) {
        topMsgPanel.setVisible(true);
        topMsgPanel.removeAll();
        JLabel msgLabel = new JLabel(text);
        msgLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topMsgPanel.add(msgLabel);
        topMsgPanel.revalidate();
        topMsgPanel.repaint();
    }

    protected void setBottomMessage(String text) {
        bottomMsgPanel.removeAll();
        JLabel msgLabel = new JLabel(text);
        msgLabel.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 20));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomMsgPanel.add(msgLabel);
        bottomMsgPanel.revalidate();
        bottomMsgPanel.repaint();
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
