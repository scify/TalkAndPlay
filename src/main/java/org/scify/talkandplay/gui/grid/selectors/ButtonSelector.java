package org.scify.talkandplay.gui.grid.selectors;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Selector for the grey buttons found at the media player and the games
 *
 * @author christina
 */
public class ButtonSelector extends Selector {

    protected int BORDER_SIZE = 5;

    public ButtonSelector(List<JPanel> panelList, long nextExecutionTime, long period) {
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

    public int getBorderSize() {
        return BORDER_SIZE;
    }
}
