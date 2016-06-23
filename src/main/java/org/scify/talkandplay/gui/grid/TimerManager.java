package org.scify.talkandplay.gui.grid;

import static com.sun.java.swing.plaf.motif.MotifBorders.FrameBorder.BORDER_SIZE;
import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TimerManager {

    private Timer timer;
    private List<JPanel> panelList;
    private int selected;
    private long nextExecutionTime;
    private long period;

    public TimerManager(List<JPanel> panelList, long nextExecutionTime, long period) {
        this.panelList = panelList;
        this.nextExecutionTime = nextExecutionTime;
        this.period = period;
        this.selected = 0;
    }

    public void start() {
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                panelList.get(selected).requestFocusInWindow();
                if (selected == 0) {
                    panelList.get(panelList.size() - 1).setBorder(null);
                    panelList.get(selected).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selected++;
                } else if (selected == panelList.size() - 1) {
                    panelList.get(selected - 1).setBorder(null);
                    panelList.get(selected).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selected = 0;
                } else if (selected < panelList.size() - 1 && selected > 0) {
                    panelList.get(selected - 1).setBorder(null);
                    panelList.get(selected).setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_SIZE));
                    selected++;
                }
            }
        }, nextExecutionTime, period);
    }

    public void cancel() {
        timer.cancel();
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setList(List<JPanel> panelList) {
        this.panelList = panelList;
    }
}
