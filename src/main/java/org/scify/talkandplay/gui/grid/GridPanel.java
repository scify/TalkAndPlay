package org.scify.talkandplay.gui.grid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.communication.CommunicationPanel;
import org.scify.talkandplay.gui.grid.entertainment.EntertainmentPanel;
import org.scify.talkandplay.gui.grid.games.GamesPanel;
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
        draw();
    }

    /**
     * Draw/display each module and add the necessary listeners
     *
     */
    private void draw() {
        panelList = new ArrayList<>();
        initLayout(1, 3);

        if (user.getCommunicationModule().isEnabled()) {
            JPanel communicationPanel = tileCreator.create(user.getCommunicationModule().getName(),
                    user.getCommunicationModule().getImage(),
                    user.getCommunicationModule().getSound(),
                    new TileAction() {
                        @Override
                        public void act() {
                            timer.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showCommunication();
                        }
                    });
            add(communicationPanel);
            panelList.add(communicationPanel);
        }

        if (user.getEntertainmentModule().isEnabled()) {
            JPanel entertainmentPanel = tileCreator.create(user.getEntertainmentModule().getName(),
                    user.getEntertainmentModule().getImage(),
                    user.getEntertainmentModule().getSound(),
                    new TileAction() {
                        @Override
                        public void act() {
                            timer.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showEntertainment();
                        }
                    });
            add(entertainmentPanel);
            panelList.add(entertainmentPanel);
        }

        if (user.getGameModule().isEnabled()) {
            JPanel gamesPanel = tileCreator.create(user.getGameModule().getName(),
                    user.getGameModule().getImage(),
                    user.getGameModule().getSound(),
                    new TileAction() {
                        @Override
                        public void act() {
                            timer.cancel();
                        }

                        @Override
                        public void audioFinished() {
                            showGames();
                        }
                    });
            add(gamesPanel);
            panelList.add(gamesPanel);
        }

        timer.setList(panelList);
        timer.start();

        revalidate();
        repaint();
    }

    private void showCommunication() {
        timer.cancel();
        try {
            CommunicationPanel communicationPanel = new CommunicationPanel(user, parent);
        } catch (IOException ex) {
            Logger.getLogger(GridPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showEntertainment() {
        timer.cancel();
        EntertainmentPanel entertainmentPanel = new EntertainmentPanel(user, parent);
    }

    private void showGames() {
        timer.cancel();
        GamesPanel gamesPanel = new GamesPanel(user, parent);
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
