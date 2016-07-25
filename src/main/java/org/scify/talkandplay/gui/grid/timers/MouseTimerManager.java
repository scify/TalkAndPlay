package org.scify.talkandplay.gui.grid.timers;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.grid.tiles.TilePanel;

public class MouseTimerManager extends TimerManager {

    protected int BORDER_SIZE = 5;

    public MouseTimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    public void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, getBorderSize()));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));
    }

    @Override
    public void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), getBorderSize()));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));
    }

    @Override
    public void start() {
        return;
    }

    @Override
    public void cancel() {
        return;
    }

    public int getBorderSize() {
        return BORDER_SIZE;
    }

}
