package org.scify.talkandplay.gui.grid.timers;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.tiles.ImagePanel;
import org.scify.talkandplay.gui.grid.tiles.TilePanel;

public class TileTimerManager extends TimerManager {

    public TileTimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    protected void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));

        TilePanel tilePanel = (TilePanel) panelList.get(i).getComponent(0);
        tilePanel.setBackground(Color.white);
        ((ImagePanel) tilePanel.getComponent(1)).setBackground(Color.white);
    }

    @Override
    protected void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), BORDER_SIZE));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));

        TilePanel tilePanel = (TilePanel) panelList.get(i).getComponent(0);
        tilePanel.setBackground(Color.decode(BACKGROUND_COLOR));
        ((ImagePanel) tilePanel.getComponent(1)).setBackground(Color.decode(BACKGROUND_COLOR));

    }
}
