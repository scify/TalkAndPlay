package org.scify.talkandplay.gui.grid.timers;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ButtonTimerManager extends TimerManager {

    protected int BORDER_SIZE = 5;

    public ButtonTimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        super(panelList, nextExecutionTime, period);
    }

    @Override
    protected void setUnselected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.white, getBorderSize()));
        panelList.get(i).setBackground(Color.decode(defaultBackgroundColor));
    }

    @Override
    protected void setSelected(int i) {
        panelList.get(i).setBorder(BorderFactory.createLineBorder(Color.decode(BORDER_COLOR), getBorderSize()));
        panelList.get(i).setBackground(Color.decode(BACKGROUND_COLOR));
    }

    protected int getBorderSize() {
        return BORDER_SIZE;
    }
}
