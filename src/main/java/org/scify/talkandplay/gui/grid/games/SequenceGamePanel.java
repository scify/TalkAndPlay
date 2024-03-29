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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.tiles.TileAction;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.SequenceGame;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.FirebaseRestAPI;

public class SequenceGamePanel extends BaseGamePanel {

    private SensorService sensorService;
    protected Message message;
    private int correctImages;
    protected long startTime;
    protected int mistakes;

    public SequenceGamePanel(User user, GridFrame parent, String previousGame) {
        super(user, parent, "sequenceGame", null, previousGame);
        this.correctImages = 1;
        this.sensorService = new SensorService(user);
        message = Message.getInstance();
        initComponents();
        initCustomComponents();
    }

    public SequenceGamePanel(User user, GridFrame parent, SequenceGame game) {
        super(user, parent, "sequenceGame", game, "");
        this.correctImages = 1;
        this.sensorService = new SensorService(user);
        message = Message.getInstance();
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
                        .addGap(0, 518, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 235, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {
        startTime = new Date().getTime();
        mistakes = 0;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBottomMessage(rm.getTextOfXMLTag("sequenceGameInfo2"));
        Random randomGenerator = new Random();
        //draw the images in a random order
        List<GameImage> tmpImages = new ArrayList(game.getEnabledImages());
        int i;
        while (!tmpImages.isEmpty()) {
            i = randomGenerator.nextInt(tmpImages.size());
            randomImages.add(tmpImages.get(i));
            tmpImages.remove(i);
        }

        redrawGamesPanel();

        c1.gridx = 0;
        c1.gridy = 0;
        for (int j = 0; j < game.getEnabledImages().size(); j++) {
            bottomPanel.add(tileCreator.createEmpty(), c1);
            c1.gridx++;
        }

        topPanel.revalidate();
        topPanel.repaint();
        bottomPanel.revalidate();
        bottomPanel.repaint();
        revalidate();
        repaint();
        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();

        selector.setList(panelList);
        selector.start();
    }

    private JPanel createGameItem(final GameImage image) {
        final JPanel panel = tileCreator.create("",
                image.getImage(),
                null);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    act(image.getOrder());
                }
            }
        });
        panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    long currentTime = new Date().getTime();
                    long seconds = (currentTime - startTime) / 1000;
                    FirebaseRestAPI firebaseRestAPI = FirebaseRestAPI.getInstance();
                    if (firebaseRestAPI != null)
                        firebaseRestAPI.postGameSelection(game.getName(), gameType, seconds, -1);
                    exit();
                } else {
                    Sensor sensor = new KeyboardSensor(keyCode, String.valueOf(evt.getKeyChar()), "keyboard");
                    if (sensorService.shouldSelect(sensor)) {
                        act(image.getOrder());
                    }
                }
            }
        });

        return panel;
    }

    private void act(int order) {
        if (correctImages == randomImages.size()) {
            selector.cancel();
            correctImages++;
            congratulate();
            redrawControlsPanel();

            bottomPanel.revalidate();
            bottomPanel.repaint();
            topPanel.revalidate();
            topPanel.repaint();
        } else if (correctImages == order) {
            setBottomMessage(message.getRandomCongratsMessage());
            selector.cancel();
            correctImages++;
            redrawGamesPanel();
            redrawControlsPanel();

            bottomPanel.revalidate();
            bottomPanel.repaint();
            topPanel.revalidate();
            topPanel.repaint();

            selector.setList(panelList);
            selector.start();
        } else {
            mistakes ++;
            setBottomMessage(message.getRandomMistakeMessage());
            selector.cancel();
            tileCreator.playAudio(getErrorSound(), new TileAction() {
                @Override
                public void act() {
                    return;
                }

                @Override
                public void audioFinished() {
                    selector.start();
                }
            });
        }
    }

    private void redrawGamesPanel() {
        c1.gridx = 0;
        topPanel.removeAll();

        panelList.clear();
        int i = 0;
        for (GameImage image : randomImages) {
            if (image.getOrder() < correctImages) {
                topPanel.add(tileCreator.createEmpty(), c1);
            } else {
                JPanel panel = createGameItem(randomImages.get(i));
                topPanel.add(panel, c1);
                panelList.add(panel);
            }
            i++;
            c1.gridx++;
        }
    }

    private void redrawControlsPanel() {
        c1.gridx = 0;
        bottomPanel.removeAll();
        GameImage img;

        for (int i = 0; i < randomImages.size(); i++) {
            img = null;
            for (GameImage image : randomImages) {
                if (image.getOrder() == i + 1 && image.getOrder() < correctImages) {
                    img = image;
                    break;
                }
            }

            if (img != null) {
                bottomPanel.add(createGameItem(img), c1);
            } else {
                bottomPanel.add(tileCreator.createEmpty(), c1);
            }
            c1.gridx++;
        }
    }

    private void congratulate() {
        long currentTime = new Date().getTime();
        long seconds = (currentTime - startTime) / 1000;
        setBottomMessage("");
        setTopMessage("<html>" +
                rm.getTextOfXMLTag("playingTime") + ": " + seconds +
                "<br>" +
                rm.getTextOfXMLTag("numberOfMistakes") + ": " + mistakes +
                "</html>");
        final ControlsPanel controls = new ControlsPanel(user, this);
        FirebaseRestAPI firebaseRestAPI = FirebaseRestAPI.getInstance();
        if (firebaseRestAPI != null)
            firebaseRestAPI.postGameSelection(game.getName(), gameType, seconds, mistakes);
        bottomMsgPanel.setVisible(false);
        tileCreator.playAudio(getWinSound(), new TileAction() {
            @Override
            public void act() {
            }

            @Override
            public void audioFinished() {
                controls.showControls();
                controls.getSelector().setList(controls.getControls());
                controls.getSelector().start();
            }
        });

        topPanel.removeAll();
        topPanel.add(controls);
        topPanel.revalidate();
        topPanel.repaint();

        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();
    }

    public void newGame() {
        tileCreator.freePlayerResources();
        SequenceGamePanel gamesPanel = new SequenceGamePanel(user, parent, game.getName());
        parent.clearGrid();
        parent.addGrid(gamesPanel);
    }

    public void playAgain() {
        tileCreator.freePlayerResources();
        SequenceGamePanel gamesPanel = new SequenceGamePanel(user, parent, (SequenceGame) game);
        parent.clearGrid();
        parent.addGrid(gamesPanel);

    }

    public void exit() {
        tileCreator.freePlayerResources();
        GamesPanel gamesPanel = new GamesPanel(user, parent);
        parent.clearGrid();
        parent.addGrid(gamesPanel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
