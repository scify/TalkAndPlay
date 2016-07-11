package org.scify.talkandplay.gui.grid;

import java.util.ArrayList;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.entertainment.FilesPanel;
import org.scify.talkandplay.gui.grid.entertainment.MediaPlayerPanel;
import org.scify.talkandplay.models.User;
import org.scify.talkandplay.services.SensorService;

public class BaseMediaPanel extends BasePanel {

    protected FilesPanel filesPanel;
    protected ArrayList<JPanel> controlsList;
    protected TimerManager timer;
    protected MediaPlayerPanel mediaPlayerPanel;

    protected SensorService sensorService;

    public BaseMediaPanel(User user, GridFrame parent) {
        super(user, parent);

        this.sensorService = new SensorService(user);
        this.controlsList = new ArrayList();
        this.mediaPlayerPanel = new MediaPlayerPanel(this, parent);
        this.timer = new TimerManager(null, user.getConfiguration().getRotationSpeed() * 1000, user.getConfiguration().getRotationSpeed() * 1000);

    }

}
