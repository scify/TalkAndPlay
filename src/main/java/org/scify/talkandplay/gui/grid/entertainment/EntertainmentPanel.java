package org.scify.talkandplay.gui.grid.entertainment;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.scify.talkandplay.gui.grid.BaseGridPanel;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.grid.TileAction;
import org.scify.talkandplay.models.User;

public class EntertainmentPanel extends BaseGridPanel {

    public EntertainmentPanel(User user, GridFrame parent) {
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
            .addGap(0, 414, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initCustomComponents() {
        removeAll();
        setBorder(new EmptyBorder(0, 10, 10, 10));
        initLayout(0, user.getConfiguration().getDefaultGridColumn());

        panelList = new ArrayList<>();

        JPanel musicPanel = createMusicItem();
        JPanel videoPanel = createVideoItem();
        JPanel backPanel = createBackItem();

        add(musicPanel);
        add(videoPanel);
        add(backPanel);
        panelList.add(musicPanel);
        panelList.add(videoPanel);
        panelList.add(backPanel);
        
        fillWithEmpties();        

        timer.setList(panelList);
        timer.start();

        revalidate();
        repaint();
        parent.clearGrid();
        parent.addGrid(this);
        parent.revalidate();
        parent.repaint();
    }

    private JPanel createMusicItem() {
        JPanel panel = tileCreator.create(user.getEntertainmentModule().getMusicModule().getName(),
                user.getEntertainmentModule().getMusicModule().getImage(),
                user.getEntertainmentModule().getMusicModule().getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        timer.cancel();
                    }

                    @Override
                    public void audioFinished() {
                        showMusic();
                    }
                });

        return panel;
    }

    private JPanel createVideoItem() {
        JPanel panel = tileCreator.create(user.getEntertainmentModule().getVideoModule().getName(),
                user.getEntertainmentModule().getVideoModule().getImage(),
                user.getEntertainmentModule().getVideoModule().getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        timer.cancel();
                    }

                    @Override
                    public void audioFinished() {
                        showVideo();
                    }
                });

        return panel;
    }

    private JPanel createBackItem() {
        JPanel panel = tileCreator.create("Πίσω",
                getClass().getResource("/org/scify/talkandplay/resources/back-icon.png").getFile(),
                user.getEntertainmentModule().getVideoModule().getSound(),
                new TileAction() {
                    @Override
                    public void act() {
                        timer.cancel();
                        showMainMenu();
                    }

                    @Override
                    public void audioFinished() {
                        return;
                    }
                });

        return panel;
    }

    private void showMusic() {
        if (user.getEntertainmentModule().getMusicModule().getFolderPath() != null
                && !user.getEntertainmentModule().getMusicModule().getFolderPath().isEmpty()
                && (new File(user.getEntertainmentModule().getMusicModule().getFolderPath())).exists()) {
            MusicPanel musicPanel = new MusicPanel(user, parent);
            parent.clearGrid();
            parent.addGrid(musicPanel);
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Ο φάκελος Μουσική δεν έχει οριστεί σωστά.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
            timer.start();
        }
    }

    private void showVideo() {
        if (user.getEntertainmentModule().getVideoModule().getFolderPath() != null
                && !user.getEntertainmentModule().getVideoModule().getFolderPath().isEmpty()
                && (new File(user.getEntertainmentModule().getVideoModule().getFolderPath())).exists()) {
            VideoPanel videoPanel = new VideoPanel(user, parent);
            parent.clearGrid();
            parent.addGrid(videoPanel);
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Ο φάκελος Βίντεο δεν έχει οριστεί σωστά.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
            timer.start();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
