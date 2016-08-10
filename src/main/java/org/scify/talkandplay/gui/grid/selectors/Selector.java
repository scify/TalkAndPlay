package org.scify.talkandplay.gui.grid.selectors;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.scify.talkandplay.gui.helpers.UIConstants;

/**
 * Base selector
 *
 * @author christina
 */
public class Selector {

    protected Timer timer;
    protected List<JPanel> panelList;
    protected int selected;
    protected long nextExecutionTime, period;

    protected static final int BORDER_SIZE = 10;
    protected static final String BORDER_COLOR = UIConstants.blue;
    protected static final String BACKGROUND_COLOR = UIConstants.lightBlue;
    protected String defaultBackgroundColor = "#ffffff";

    public Selector(List<JPanel> panelList, long nextExecutionTime, long period) {
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
                panelList.get(selected).setFocusable(true);
                panelList.get(selected).grabFocus();

                unselectAll();
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

    public void setUnselected(int i) {
        return;
    }

    public void setSelected(int i) {
        return;
    }

    public void unselectAll() {
        for (int i = 0; i < panelList.size(); i++) {
            setUnselected(i);
        }
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

    public int getBorderSize() {
        return BORDER_SIZE;
    }

    public String getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public static String getBorderColor() {
        return BORDER_COLOR;
    }

    public static String getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    public void addListeners(List<JPanel> panelList) {
    }
}
