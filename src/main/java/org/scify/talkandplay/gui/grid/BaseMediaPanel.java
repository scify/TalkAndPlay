package org.scify.talkandplay.gui.grid;

import org.scify.talkandplay.gui.grid.timers.TimerManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.scify.talkandplay.gui.grid.entertainment.EntertainmentPanel;
import org.scify.talkandplay.gui.grid.entertainment.FilesPanel;
import org.scify.talkandplay.gui.grid.entertainment.MediaPlayerPanel;
import org.scify.talkandplay.gui.grid.timers.ButtonTimerManager;
import org.scify.talkandplay.gui.grid.timers.MouseTimerManager;
import org.scify.talkandplay.gui.grid.timers.TileTimerManager;
import org.scify.talkandplay.gui.helpers.UIConstants;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.models.sensors.KeyboardSensor;
import org.scify.talkandplay.models.sensors.MouseSensor;
import org.scify.talkandplay.models.sensors.Sensor;
import org.scify.talkandplay.services.SensorService;

public class BaseMediaPanel extends BasePanel {

    protected FilesPanel filesPanel;
    protected ArrayList<JPanel> controlsList;
    protected TimerManager timer;
    protected MediaPlayerPanel mediaPlayerPanel;
    protected ArrayList<File> files;
    protected GridBagConstraints c;
    protected String currentFile;

    protected SensorService sensorService;

    public BaseMediaPanel(User user, GridFrame parent, File[] files) {
        super(user, parent);

        this.sensorService = new SensorService(user);
        this.controlsList = new ArrayList();
        this.mediaPlayerPanel = new MediaPlayerPanel(this);
        this.files = new ArrayList();
        this.currentFile = "";
        this.c = new GridBagConstraints();

        if (user.getConfiguration().getSelectionSensor() instanceof MouseSensor) {
            this.timer = new MouseTimerManager(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        } else {
            this.timer = new ButtonTimerManager(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);
        }

        Collections.addAll(this.files, files);
        prepareFiles();
    }

    public void initLayout() {
        setBorder(new EmptyBorder(0, 20, 20, 20));
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
    }

    public void playFile(String fileName) {
        return;
    }

    public TimerManager getTimer() {
        return timer;
    }

    public List<JPanel> getControlsList() {
        return controlsList;
    }

    public boolean isEmpty() {
        if (files.isEmpty()) {
            return true;
        }
        return false;
    }

    protected void drawEmpty() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel noFiles = new JLabel("Δεν υπάρχουν αρχεία");
        noFiles.setBorder(new EmptyBorder(5, 5, 5, 5));
        noFiles.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        noFiles.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel("Πίσω");
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        label.setFont(new Font(UIConstants.mainFont, Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/org/scify/talkandplay/resources/back-icon.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        icon.setBorder(new EmptyBorder(5, 5, 5, 5));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.decode(UIConstants.grey));
        panel.setPreferredSize(new Dimension(180, 130));
        panel.setMaximumSize(new Dimension(180, 130));
        panel.setMinimumSize(new Dimension(180, 130));
        panel.setBorder((new LineBorder(Color.white, 10)));
        panel.add(icon);
        panel.add(label);

        add(noFiles);
        add(panel);
        controlsList.add(panel);
        timer.setList(controlsList);
        timer.start();

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Sensor sensor = new MouseSensor(evt.getButton(), evt.getClickCount(), "mouse");
                if (sensorService.shouldSelect(sensor)) {
                    goBack();
                }
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Sensor sensor = new KeyboardSensor(evt.getKeyCode(), String.valueOf(evt.getKeyChar()), "keyboard");
                if (sensorService.shouldSelect(sensor)) {
                    goBack();
                }
            }
        });
    }

    public void goBack() {
        timer.cancel();
        parent.clearGrid();
        EntertainmentPanel entPanel = new EntertainmentPanel(user, parent);
    }

    /**
     * Remove the directories and keep only the files
     */
    protected void prepareFiles() {
        Iterator<File> i = files.iterator();
        while (i.hasNext()) {
            File f = i.next();
            if (f.isDirectory()) {
                i.remove();
            }
        }
    }

}
