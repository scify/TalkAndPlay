package org.scify.talkandplay.gui.grid.timers;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.tiles.ImagePanel;
import org.scify.talkandplay.gui.grid.tiles.TilePanel;

public class TileTimerManager extends TimerManager {

    protected int BORDER_SIZE = 5;

    public TileTimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    protected void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));

        ((TilePanel) panelList.get(i).getComponent(0)).setUnSelected();
    }

    @Override
    protected void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));

        ((TilePanel) panelList.get(i).getComponent(0)).setSelected();
    }
}
