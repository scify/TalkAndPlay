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
package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import io.sentry.Sentry;
import org.scify.talkandplay.gui.grid.BaseGridPanel;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.communication.CommunicationPanel;
import org.scify.talkandplay.gui.grid.tiles.TileAction;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.Category;
import org.scify.talkandplay.models.Tile;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameCollection;
import org.scify.talkandplay.models.games.StimulusReactionGame;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.ImageResource;
import org.scify.talkandplay.utils.ResourceType;

public class GamesPanel extends BaseGridPanel {

    private int grid;
    private int stopped = 0;

    public GamesPanel(User user, GridFrame parent) {
        super(user, parent);

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
                        .addGap(0, 535, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {
        UIConstants.getInstance().setRows(2);
        UIConstants.getInstance().setColumns(3);
        initLayout();
        panelList = new ArrayList();
        showGameTypes();
    }

    protected void showGameTypes() {
        for (GameCollection gameCollection : user.getGameModule().getGameTypes()) {
            JPanel gamePanel = createGameItem(gameCollection);
            add(gamePanel, c);
            c.gridx++;
            panelList.add(gamePanel);
        }

        JPanel backPanel = createBackItem(true);
        add(backPanel, c);
        panelList.add(backPanel);

        c.gridy++;
        c.gridx = 0;
        fillWithEmpties();

        revalidate();
        repaint();
        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();

        selector.setList(panelList);
        selector.start();
    }

    private JPanel createGameItem(final GameCollection gameCollection) {

        JPanel panel = tileCreator.create(gameCollection.getName(),
                gameCollection.getImage(),
                gameCollection.getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        selector.cancel();
                    }

                    @Override
                    public void audioFinished() {
                        if ("stimulusReactionGame".equals(gameCollection.getGameType())) {
                            List<Game> games = user.getGameModule().getGames("stimulusReactionGame");
                            drawGames(games, "stimulusReactionGame");
                        } else if ("sequenceGame".equals(gameCollection.getGameType())) {
                            List<Game> games = user.getGameModule().getGames("sequenceGame");
                            drawGames(games, "sequenceGame");
                        } else if ("similarityGame".equals(gameCollection.getGameType())) {
                            List<Game> games = user.getGameModule().getGames("similarityGame");
                            drawGames(games, "similarityGame");
                        }
                    }
                });

        return panel;
    }

    protected void drawGames(List<Game> games, String gamesType) {
        removeAll();
        panelList.clear();
        c.gridx = -1;
        c.gridy = 0;
        setGrid();

        int size = games.size();
        if (size > 0) {
            if (size >= grid) {
                int i;
                for (i = stopped; i < (grid + stopped - 2); i++) {
                    if (i < size) {
                        Game game = games.get(i);
                        JPanel gamePanel = createGameItem(game, gamesType);
                        add(gamePanel, c);
                        setGrid();
                        panelList.add(gamePanel);
                    }
                }
                stopped = i;
                JPanel panel = createMoreItem(games, gamesType);
                add(panel, c);
                setGrid();
                panelList.add(panel);
                tileList.add(new Tile(panel, false));

                if (i >= size) {
                    stopped = 0;
                }

            } else {
                for (Game game : games) {
                    JPanel gamePanel = createGameItem(game, gamesType);
                    add(gamePanel, c);
                    setGrid();
                    panelList.add(gamePanel);
                }
            }
        }

        JPanel backPanel = createBackItem(false);
        add(backPanel, c);
        setGrid();
        panelList.add(backPanel);


        fillWithEmpties();
        revalidate();
        repaint();
        selector.setList(panelList);
        selector.start();
    }

    private void setGrid() {
        int rows = user.getConfiguration().getDefaultGridRow();
        int columns = user.getConfiguration().getDefaultGridColumn();
        grid = rows * columns;
        if (c.gridx == (columns - 1)) {
            c.gridx = 0;
            c.gridy++;
        } else {
            c.gridx++;
        }
    }

    private JPanel createGameItem(final Game game, final String gamesType) {

        JPanel panel = tileCreator.create(game.getName(),
                game.getImage(),
                game.getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        selector.cancel();
                        if (gamesType.equals("stimulusReactionGame"))
                            showStimulusReactionGame();
                        else if (gamesType.equals("sequenceGame"))
                            showSequenceGame();
                        else if (gamesType.equals("similarityGame"))
                            showSimilarityGame();
                    }

                    @Override
                    public void audioFinished() {

                    }
                });

        return panel;
    }

    /**
     * Create the JPanel that holds the more button
     *
     * @return
     * @throws IOException
     */
    private JPanel createMoreItem(List<Game> games, final String gamesType) {
        JPanel panel = tileCreator.create(rm.getTextOfXMLTag("more"),
                new ImageResource("more-icon.png", ResourceType.JAR),
                null,
                new TileAction() {
                    @Override
                    public void act() {
                        selector.cancel();
                        drawGames(games, gamesType);
                    }

                    @Override
                    public void audioFinished() {
                        return;
                    }

                    @Override
                    public boolean mute() {
                        return true;
                    }
                });

        return panel;
    }

    private JPanel createBackItem(boolean toMainMenu) {
        JPanel panel = tileCreator.create(rm.getTextOfXMLTag("backButton"),
                new ImageResource("back-icon.png", ResourceType.JAR),
                null,
                new TileAction() {
                    @Override
                    public void act() {
                        if (toMainMenu) {
                            selector.cancel();
                            showMainMenu();
                        } else {
                            selector.cancel();
                            GamesPanel gamesPanel = new GamesPanel(user, parent);
                            parent.clearGrid();
                            parent.addGrid(gamesPanel);
                        }
                    }

                    @Override
                    public void audioFinished() {
                        return;
                    }

                    @Override
                    public boolean mute() {
                        return true;
                    }
                });

        return panel;
    }

    private void showStimulusReactionGame() {
        final SensorService sensorService = new SensorService(user);

        if (hasGames("stimulusReactionGame")) {

            ButtonPanel buttonPanel = new ButtonPanel(rm.getTextOfXMLTag("pressTheButton"), "");

            buttonPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        StimulusReactionGamePanel gamePanel = new StimulusReactionGamePanel(user, parent, "");
                    }
                }
            });
            buttonPanel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        StimulusReactionGamePanel gamePanel = new StimulusReactionGamePanel(user, parent, "");
                    }
                }
            });

            removeAll();
            add(buttonPanel);
            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            buttonPanel.requestFocusInWindow();
            buttonPanel.setFocusable(true);
        } else {
            JPanel panel = noGamePanel();

            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });
            panel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });

            removeAll();
            add(panel);

            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            panel.requestFocusInWindow();
            panel.setFocusable(true);

        }
    }

    private void showSequenceGame() {
        final SensorService sensorService = new SensorService(user);

        if (hasGames("sequenceGame")) {

            ButtonPanel buttonPanel = new ButtonPanel(rm.getTextOfXMLTag("sequenceGameInfo"), rm.getTextOfXMLTag("pressTheButtonToStart"));

            buttonPanel.setFocusable(true);
            buttonPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        SequenceGamePanel gamePanel = new SequenceGamePanel(user, parent, "");
                    }
                }
            });
            buttonPanel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        SequenceGamePanel gamePanel = new SequenceGamePanel(user, parent, "");
                    }
                }
            });

            removeAll();
            add(buttonPanel);
            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            buttonPanel.requestFocusInWindow();
            buttonPanel.setFocusable(true);
        } else {
            JPanel panel = noGamePanel();

            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });
            panel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });

            removeAll();
            add(panel);

            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            panel.requestFocusInWindow();
            panel.setFocusable(true);

        }
    }

    private void showSimilarityGame() {
        final SensorService sensorService = new SensorService(user);

        if (hasGames("similarityGame")) {
            ButtonPanel buttonPanel = new ButtonPanel(rm.getTextOfXMLTag("gameTypeFindTheSimilar") + ".", rm.getTextOfXMLTag("pressTheButtonToStart"));
            buttonPanel.setFocusable(true);
            buttonPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        SimilarityGamePanel gamePanel = new SimilarityGamePanel(user, parent, "");
                    }
                }
            });
            buttonPanel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        SimilarityGamePanel gamePanel = new SimilarityGamePanel(user, parent, "");
                    }
                }
            });

            removeAll();
            add(buttonPanel);

            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            buttonPanel.requestFocusInWindow();
            buttonPanel.setFocusable(true);
        } else {
            JPanel panel = noGamePanel();

            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent me) {
                    Sensor sensor = new MouseSensor(me.getButton(), me.getClickCount(), "mouse");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });
            panel.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ke) {
                    Sensor sensor = new KeyboardSensor(ke.getKeyCode(), String.valueOf(ke.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        GamesPanel gamePanel = new GamesPanel(user, parent);
                    }
                }
            });

            removeAll();
            add(panel);

            revalidate();
            repaint();
            parent.clearGrid();
            parent.addGrid(this);
            parent.revalidate();
            parent.repaint();

            panel.requestFocusInWindow();
            panel.setFocusable(true);

        }
    }

    private JPanel noGamePanel() {
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(rm.getImage("back-icon.png", ResourceType.JAR).getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel fileLabel = new JLabel(rm.getTextOfXMLTag("noGames"));
        fileLabel.setFont(new Font(UIConstants.mainFont, Font.BOLD, 18));
        fileLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setBorder((new LineBorder(Color.white, 5)));

        panel.add(fileLabel);
        panel.add(iconLabel);

        return panel;
    }

    private boolean hasGames(String type) {
        boolean hasGames = true;

        for (GameCollection gameCollection : user.getGameModule().getGameTypes()) {
            if (type.equals(gameCollection.getGameType()) && gameCollection.getEnabledGames().size() == 0) {
                hasGames = false;
                break;
            }
        }
        return hasGames;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
