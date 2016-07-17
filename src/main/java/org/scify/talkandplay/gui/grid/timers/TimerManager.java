package org.scify.talkandplay.gui.grid.timers;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.helpers.UIConstants;

public class TimerManager {

    protected Timer timer;
    protected List<JPanel> panelList;
    protected int selected;
    protected long nextExecutionTime, period;

    protected static final int BORDER_SIZE = 10;
    protected static final String BORDER_COLOR = UIConstants.blue;
    protected static final String BACKGROUND_COLOR = UIConstants.lightBlue;
    protected String defaultBackgroundColor = "#ffffff";

    public TimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        this.panelList = panelList;
        this.nextExecutionTime = nextExecutionTime;
        this.period = period;
        this.selected = 0;
    }

    public void start() {
        timer = new Timer();
        selected = 0;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                panelList.get(selected).requestFocusInWindow();
                for (int i = 0; i < panelList.size(); i++) {
                    setUnselected(i);
                }
                setSelected(selected);

                if (selected == 0 || (selected < panelList.size() - 1 && selected > 0)) {
                    selected++;
                } else if (selected == panelList.size() - 1) {
                    selected = 0;
                }
            }
        }, nextExecutionTime, period);
    }

    public void unselect() {
        if (selected == 0) {
            panelList.get(panelList.size() - 1).setBorder(BorderFactory.createLineBorder(Color.white, getBorderSize()));
            panelList.get(panelList.size() - 1).setBackground(Color.decode(defaultBackgroundColor));
        } else {
            panelList.get(selected - 1).setBorder(BorderFactory.createLineBorder(Color.white, getBorderSize()));
            panelList.get(selected - 1).setBackground(Color.decode(defaultBackgroundColor));
        }
    }

    protected void setUnselected(int i) {
        return;
    }

    protected void setSelected(int i) {
        return;
    }

    public void cancel() {
        timer.cancel();
    }

    public void setList(List<JPanel> panelList) {
        this.panelList = panelList;
    }

    public void setDefaultBackgroundColor(String color) {
        this.defaultBackgroundColor = color;
    }

    protected int getBorderSize() {
        return BORDER_SIZE;
    }
}
