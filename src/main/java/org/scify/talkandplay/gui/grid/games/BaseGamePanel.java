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
package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.selectors.ManualTileSelector;
import org.scify.talkandplay.gui.grid.tiles.TileCreator;
import org.scify.talkandplay.gui.grid.selectors.MouseSelector;
import org.scify.talkandplay.gui.grid.selectors.TileSelector;
import org.scify.talkandplay.gui.grid.selectors.Selector;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.GameCollection;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.utils.ResourceManager;
import org.scify.talkandplay.utils.ResourceType;

public class BaseGamePanel extends javax.swing.JPanel {

    protected User user;
    protected GridFrame parent;
    protected Game game;
    protected GridBagConstraints c1;
    protected Selector selector;
    protected TileCreator tileCreator;
    protected String gameType;
    protected JPanel topPanel, bottomPanel, topMsgPanel, bottomMsgPanel;
    protected List<GameImage> randomImages;
    protected ArrayList<JPanel> panelList;
    protected String previousGame;
    protected ResourceManager rm;

    public BaseGamePanel(User user, GridFrame parent, String gameType, Game game, String previousGame) {
        this.user = user;
        this.parent = parent;
        this.gameType = gameType;
        this.game = game;
        this.previousGame = previousGame;
        this.rm = ResourceManager.getInstance();

        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            this.selector = new MouseSelector(panelList, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else if (user.getConfiguration().getNavigationSensor() != null) {
            this.selector = new ManualTileSelector(user, panelList, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else {
            this.selector = new TileSelector(panelList, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
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
        bottomPanel.setBackground(Color.white);
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

        if (game == null)
            game = user.getGameModule().getRandomGame(gameType, previousGame);

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
     * Get the win sound for the game
     *
     * @return
     */
    protected String getWinSound() {
        File soundFile = rm.getSound("sounds/games/winSound.mp3", ResourceType.BUNDLE);
        if (game.getWinSound() != null) {
            soundFile = rm.getSound(game.getWinSound().getPath(), game.getWinSound().getResourceType());
        } else {
            for (GameCollection gameCollection : user.getGameModule().getGameTypes()) {
                if (gameType.equals(gameCollection.getGameType()) && gameCollection.getWinSound() != null) {
                    soundFile = rm.getSound(game.getWinSound().getPath(), game.getWinSound().getResourceType());
                }
            }
        }
        return soundFile.getAbsolutePath();
    }

    /**
     * Get the error sound
     *
     * @return
     */
    protected String getErrorSound() {
        File soundFile = rm.getSound("sounds/games/errorSound.mp3", ResourceType.BUNDLE);
        for (GameCollection gameCollection : user.getGameModule().getGameTypes()) {
            if (gameType.equals(gameCollection.getGameType()) && gameCollection.getErrorSound() != null) {
                soundFile = rm.getSound(gameCollection.getErrorSound().getPath(), gameCollection.getErrorSound().getResourceType());
            }
        }
        return soundFile.getAbsolutePath();
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
