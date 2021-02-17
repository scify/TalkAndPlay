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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.tiles.TileAction;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.Game;
import org.scify.talkandplay.models.games.GameImage;
import org.scify.talkandplay.models.games.SimilarityGame;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import org.scify.talkandplay.utils.ImageResource;

public class SimilarityGamePanel extends BaseGamePanel {

    private boolean endGame = false;
    private ImageResource correctImage;
    private SensorService sensorService;
    protected Message message;

    public SimilarityGamePanel(User user, GridFrame parent, String previousGame) {
        super(user, parent, "similarityGame", null, previousGame);
        this.previousGame = previousGame;
        this.sensorService = new SensorService(user);
        message = Message.getInstance();
        initComponents();
        initCustomComponents();
    }

    public SimilarityGamePanel(User user, GridFrame parent, Game game) {
        super(user, parent, "similarityGame", game, "");
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

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setTopMessage(rm.getTextOfXMLTag("pressTheButtonOnTheSimilar"));
        setBottomMessage("");
        Random randomGenerator = new Random();

        int i = randomGenerator.nextInt(game.getEnabledImages().size());
        correctImage = game.getEnabledImages().get(i).getImage();
        bottomPanel.add(createGameItem(game.getEnabledImages().get(i)));

        List<GameImage> tmpImages = new ArrayList(game.getEnabledImages());
        while (!tmpImages.isEmpty()) {
            i = randomGenerator.nextInt(tmpImages.size());
            randomImages.add(tmpImages.get(i));
            tmpImages.remove(i);
        }

        for (GameImage image : randomImages) {
            JPanel panel = createGameItem(image);
            topPanel.add(panel, c1);
            panelList.add(panel);
            c1.gridx++;
        }

        c1.gridx = 0;
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
                    act(image.getImage().getPath());
                }
            }
        });
        panel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");

                if (sensorService.shouldSelect(sensor)) {
                    act(image.getImage().getPath());
                }
            }
        });

        return panel;
    }

    private void act(String image) {

        if (image.equals(correctImage.getPath())) {
            congratulate();
        } else {
            setBottomMessage(message.getRandomMistakeMessage());
            selector.cancel();
            tileCreator.playAudio(getErrorSound(), new TileAction() {
                @Override
                public void act() {
                    return;
                }

                @Override
                public void audioFinished() {
                    selector.setList(panelList);
                    selector.start();
                }
            });
        }

    }

    private void congratulate() {
        setBottomMessage("");
        setTopMessage("");

        final ControlsPanel controls = new ControlsPanel(user, this);
        
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
        
        bottomPanel.removeAll();
        topPanel.revalidate();
        topPanel.repaint();

        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();
    }

    public void newGame() {
        tileCreator.freePlayerResources();
        SimilarityGamePanel gamePanel = new SimilarityGamePanel(user, parent, game.getName());
        parent.clearGrid();
        parent.addGrid(gamePanel);
    }

    public void playAgain() {
        tileCreator.freePlayerResources();
        SimilarityGamePanel gamePanel = new SimilarityGamePanel(user, parent, (SimilarityGame) game);
        parent.clearGrid();
        parent.addGrid(gamePanel);

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
