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
package org.scify.talkandplay.gui.grid;

import io.sentry.Sentry;
import org.scify.talkandplay.gui.grid.tiles.TileAction;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

import org.scify.talkandplay.gui.grid.communication.CommunicationPanel;
import org.scify.talkandplay.gui.grid.entertainment.EntertainmentPanel;
import org.scify.talkandplay.gui.grid.games.GamesPanel;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;

/**
 * The first grid, or the main menu of the applications. Displays the
 * communication, entertainment and game modules.
 *
 * @author christina
 */
public class GridPanel extends BaseGridPanel {

    public GridPanel(User user, GridFrame parent) {
        super(user, parent);
        this.panelList = new ArrayList<>();
        initComponents();
        initCustomComponents();
    }

    /**
     * Draw/display each module and add the necessary listeners
     *
     */
    private void initCustomComponents() {
        UIConstants.getInstance().setRows(2);
        UIConstants.getInstance().setColumns(3);
        initLayout();
        panelList = new ArrayList<>();

        if (user.getCommunicationModule().isEnabled()) {
             JPanel communicationPanel = tileCreator.create(user.getCommunicationModule().getName(),
                    user.getCommunicationModule().getImageResource(),
                    user.getCommunicationModule().getSoundResource(),
                    new TileAction() {
                        @Override
                        public void act() {
                            selector.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showCommunication();
                        }
                    });
            communicationPanel.setBackground(Color.white);
            add(communicationPanel, c);
            panelList.add(communicationPanel);
        }

        if (user.getEntertainmentModule().isEnabled()) {
            JPanel entertainmentPanel = tileCreator.create(user.getEntertainmentModule().getName(),
                    user.getEntertainmentModule().getImageResource(),
                    user.getEntertainmentModule().getSoundResource(),
                    new TileAction() {
                        @Override
                        public void act() {
                            selector.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showEntertainment();
                        }
                    });
            entertainmentPanel.setBackground(Color.white);
            c.gridx++;
            add(entertainmentPanel, c);
            panelList.add(entertainmentPanel);
        }

        if (user.getGameModule().isEnabled()) {
            JPanel gamesPanel = tileCreator.create(user.getGameModule().getName(),
                    user.getGameModule().getImageResource(),
                    user.getGameModule().getSoundResource(),
                    new TileAction() {
                        @Override
                        public void act() {
                            selector.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showGames();
                        }
                    });
            gamesPanel.setBackground(Color.white);
            c.gridx++;
            add(gamesPanel, c);
            panelList.add(gamesPanel);
        }

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

    private void showCommunication() {
        try {
            new CommunicationPanel(user, parent);
        } catch (IOException ex) {
            Logger.getLogger(GridPanel.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.captureMessage(ex.getMessage());
        }
    }

    private void showEntertainment() {
        try {
            new EntertainmentPanel(user, parent);
        } catch (Exception ex) {
            Logger.getLogger(GridPanel.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.captureMessage(ex.getMessage());
        }
    }

    private void showGames() {
        try {
            new GamesPanel(user, parent);
        } catch (Exception ex) {
            Logger.getLogger(GridPanel.class.getName()).log(Level.SEVERE, null, ex);
            Sentry.captureMessage(ex.getMessage());
        }
    }

    public List<JPanel> getPanelList() {
        return panelList;
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
