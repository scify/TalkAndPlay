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
package org.scify.talkandplay.gui.grid.games;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.scify.talkandplay.gui.grid.BaseGridPanel;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.tiles.TileAction;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.GameCollection;
import org.scify.talkandplay.models.games.StimulusReactionGame;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.utils.FirebaseRestAPI;
import org.scify.talkandplay.utils.ResourceType;
import org.scify.talkandplay.utils.SoundResource;

public class StimulusReactionGamePanel extends BaseGridPanel {

    private StimulusReactionGame game;
    private int selected;
    private JPanel gamePanel, controlsPanel;
    private String previousGame;
    protected long startTime;
    protected JPanel topMsgPanel;

    public StimulusReactionGamePanel(User user, GridFrame parent, String previousGame) {
        super(user, parent);
        this.selected = 0;
        this.previousGame = previousGame;
        initComponents();
        initCustomComponents();
    }

    public StimulusReactionGamePanel(User user, GridFrame parent, StimulusReactionGame game) {
        super(user, parent);
        this.selected = 0;
        this.game = game;
        initComponents();        
        initCustomComponents();
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
            .addGap(0, 327, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {

        startTime = new Date().getTime();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        topMsgPanel = new JPanel();
        topMsgPanel.setBackground(Color.white);
        topMsgPanel.setBorder(new MatteBorder(0, 0, 2, 0, Color.decode(UIConstants.grey)));
        topMsgPanel.setVisible(false);
        add(topMsgPanel);


        gamePanel = new JPanel();
        gamePanel.setBackground(Color.white);
        controlsPanel = new JPanel();
        controlsPanel.setBackground(Color.white);
        add(gamePanel);
        add(controlsPanel);

        gamePanel.setLayout(new FlowLayout());

        panelList = new ArrayList();

        if (game == null) {
            game = (StimulusReactionGame)user.getGameModule().getRandomGame("stimulusReactionGame", previousGame);
        }

        if (game == null) {
            //TODO fix
            gamePanel.add(new JLabel("No games"));
        } else {
            List<GameImage> enabledImages = game.getEnabledImages();
            JPanel gameImage = createGameItem(enabledImages.get(0));
            gamePanel.add(gameImage);
            panelList.add(gameImage);

        }

        gamePanel.revalidate();
        gamePanel.repaint();
        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();

        selector.setList(panelList);
        selector.start();
    }

    private JPanel createGameItem(GameImage image) {
        //make the image in stimulus reaction games fill the whole screen
        UIConstants.getInstance().setRows(1);
        UIConstants.getInstance().setColumns(1);
        initLayout();
        
        final StimulusReactionGamePanel currentPanel = this;
        panelList.clear();
                
        JPanel panel = tileCreator.create("",
                image.getImage(),
                image.getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        selector.cancel();
                        selected++;
                        if (selected == game.getEnabledImages().size() - 1) {
                            congratulate(game.getEnabledImages().get(selected));
                        } else {
                            panelList.clear();
                            JPanel panel = createGameItem(game.getEnabledImages().get(selected));
                            panelList.add(panel);
                            gamePanel.removeAll();
                            gamePanel.add(panel, c);
                            gamePanel.revalidate();
                            gamePanel.repaint();
                            parent.clearGrid();
                            parent.addGrid(currentPanel);
                            parent.revalidate();
                            parent.repaint();

                            selector.setList(panelList);
                            selector.start();
                        }
                    }

                    @Override
                    public void audioFinished() {
                    }

                    @Override
                    public boolean mute() {
                        return true;
                    }
                });

        panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    long currentTime = new Date().getTime();
                    long seconds = (currentTime - startTime) / 1000;
                    FirebaseRestAPI.getInstance().postGameSelection(game.getName(), "stimulusReactionGame", seconds, -1);
                    exit();
                }
            }
        });

        panelList.add(panel);
        selector.setList(panelList);
        selector.start();

        return panel;
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

    private void congratulate(GameImage image) {

        long currentTime = new Date().getTime();
        long seconds = (currentTime - startTime) / 1000;
        setTopMessage(rm.getTextOfXMLTag("playingTime") + ": " + seconds);
        FirebaseRestAPI.getInstance().postGameSelection(game.getName(), "stimulusReactionGame", seconds, 0);

        tileCreator.playAudio(getWinSound().getSound().getAbsolutePath());

        JPanel finalImage = tileCreator.create("",
                image.getImage(),
                getWinSound(),
                new TileAction() {
                    @Override
                    public void act() {
                    }

                    @Override
                    public void audioFinished() {
                    }
                });

        gamePanel.removeAll();
        gamePanel.add(finalImage);
        gamePanel.revalidate();
        gamePanel.repaint();

        ControlsPanel controls = new ControlsPanel(user, this);
        controlsPanel.add(controls);
        
        controls.showControls();
        controls.getSelector().setList(controls.getControls());
        controls.getSelector().start();

        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();
    }

    public void setGame(StimulusReactionGame game) {
        this.game = game;
    }

    public void newGame() {
        tileCreator.freePlayerResources();
        StimulusReactionGamePanel gamePanel = new StimulusReactionGamePanel(user, parent, game.getName());
        parent.clearGrid();
        parent.addGrid(gamePanel);
    }

    public void playAgain() {
        tileCreator.freePlayerResources();
        StimulusReactionGamePanel gamePanel = new StimulusReactionGamePanel(user, parent, game);
        parent.clearGrid();
        parent.addGrid(gamePanel);
    }

    public void exit() {
        tileCreator.freePlayerResources();
        GamesPanel gamesPanel = new GamesPanel(user, parent);
        parent.clearGrid();
        parent.addGrid(gamesPanel);
    }

    protected SoundResource getWinSound() {
        SoundResource sound = null;

        if (game.getWinSound() != null) {
            sound = game.getWinSound();
        } else {
            for (GameCollection gameCollection : user.getGameModule().getGameTypes()) {
                if ("stimulusReactionGame".equals(gameCollection.getGameType()) && gameCollection.getWinSound() != null) {
                    sound = gameCollection.getWinSound();
                }
            }
        }
        if (sound == null) {
            sound = new SoundResource("sounds/games/winSound.mp3", ResourceType.BUNDLE);
        }
        return sound;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
