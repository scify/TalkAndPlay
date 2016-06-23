package org.scify.talkandplay.gui.grid.games;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.scify.talkandplay.gui.grid.GridFrame;
import org.scify.talkandplay.gui.helpers.GuiHelper;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.games.GameType;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class GamesPanel extends javax.swing.JPanel {

    private User user;
    private GridFrame parent;
    private List<JPanel> panelList;
    private AudioMediaPlayerComponent audioPlayer;
    private Timer timer;
    private int selectedImage;
    private String clickedImage;

    private GuiHelper guiHelper;
    private SensorService sensorService;

    protected final int BORDER_SIZE = 5;
    protected final int IMAGE_PADDING = 10;

    public GamesPanel(User user, GridFrame parent) {
        this.user = user;
        this.parent = parent;
        this.audioPlayer = new AudioMediaPlayerComponent();
        this.guiHelper = new GuiHelper();
        this.sensorService = new SensorService(user);
        initComponents();
        initAudioPlayer();
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

        imagesPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout imagesPanelLayout = new javax.swing.GroupLayout(imagesPanel);
        imagesPanel.setLayout(imagesPanelLayout);
        imagesPanelLayout.setHorizontalGroup(
            imagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 535, Short.MAX_VALUE)
        );
        imagesPanelLayout.setVerticalGroup(
            imagesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void initAudioPlayer() {
        audioPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                audioPlayer.getMediaPlayer().mute(false);
                audioPlayer.getMediaPlayer().setVolume(100);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                if ("stimulusReactionGame".equals(clickedImage)) {
                    showStimulusReactionGame();
                } else if ("sequenceGame".equals(clickedImage)) {
                    showSequenceGame();
                } else if ("similarityGame".equals(clickedImage)) {
                     showSimilarityGame();
                }
            }
        });

        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioPlayer.getMediaPlayer().stop();
                audioPlayer.getMediaPlayer().stop();
                e.getWindow().dispose();
            }
        });
    }

    private void initCustomComponents() {
        selectedImage = 0;
        imagesPanel.removeAll();
        imagesPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        imagesPanel.setLayout(new GridLayout(1, 3, IMAGE_PADDING, IMAGE_PADDING));
        panelList = new ArrayList<>();

        for (GameType gameType : user.getGameModule().getGameTypes()) {
            createGameItem(gameType);
        }
        createBackItem();

        setTimer();

        imagesPanel.revalidate();
        imagesPanel.repaint();
        parent.add(imagesPanel);
        parent.revalidate();
        parent.repaint();
    }

    private JPanel createGameItem(final GameType gameType) {
        JPanel panel = guiHelper.createImagePanel(gameType.getImage(), gameType.getName());
        panelList.add(panel);
        imagesPanel.add(panel);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    timer.cancel();
                    clickedImage = gameType.getType();
                    audioPlayer.getMediaPlayer().playMedia(user.getEntertainmentModule().getMusicModule().getSound());
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), evt.getKeyChar(), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    timer.cancel();
                    clickedImage = gameType.getType();
                    audioPlayer.getMediaPlayer().playMedia(user.getEntertainmentModule().getMusicModule().getSound());
                }
            }
        });

        return panel;
    }

    private JPanel createBackItem() {
        JPanel panel = guiHelper.createResourceImagePanel((new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/back-icon.png"))), "Πίσω");
        panelList.add(panel);
        imagesPanel.add(panel);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    timer.cancel();
                  //  parent.repaintMenu(imagesPanel);
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), evt.getKeyChar(), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    timer.cancel();
                  //  parent.repaintMenu(imagesPanel);
                }
            }
        });

        return panel;
    }

    private void setTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                panelList.get(selectedImage).requestFocusInWindow();
                if (selectedImage == 0) {
                    panelList.get(panelList.size() - 1).setBorder(null);
                    panelList.get(selectedImage).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selectedImage++;
                } else if (selectedImage == panelList.size() - 1) {
                    panelList.get(selectedImage - 1).setBorder(null);
                    panelList.get(selectedImage).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selectedImage = 0;
                } else if (selectedImage < panelList.size() - 1 && selectedImage > 0) {
                    panelList.get(selectedImage - 1).setBorder(null);
                    panelList.get(selectedImage).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selectedImage++;
                }
            }
        }, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
    }

    private void showStimulusReactionGame() {
        timer.cancel();
        parent.remove(imagesPanel);
        StimulusReactionGamePanel gamePanel = new StimulusReactionGamePanel(user, parent);
    }

    private void showSequenceGame() {
        timer.cancel();
        parent.remove(imagesPanel);
        SequenceGamePanel gamePanel = new SequenceGamePanel(user, parent);
    }
    
    private void showSimilarityGame() {
        timer.cancel();
        parent.remove(imagesPanel);
        SimilarityGamePanel gamePanel = new SimilarityGamePanel(user, parent);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel imagesPanel;
    // End of variables declaration//GEN-END:variables
}
