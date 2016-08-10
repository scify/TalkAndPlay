package org.scify.talkandplay.gui.grid;

import java.awt.Color;
import org.scify.talkandplay.gui.grid.selectors.Selector;
import org.scify.talkandplay.models.User;

public class BasePanel extends javax.swing.JPanel {

    protected GridFrame parent;
    protected User user;
    protected Selector selector;

    public BasePanel(User user, GridFrame parent) {
        this.user = user;
        this.parent = parent;
        setBackground(Color.white);
    }

    protected void showMainMenu() {
        GridPanel gridPanel = new GridPanel(user, parent);
        parent.clearGrid();
        parent.addGrid(gridPanel);
        parent.revalidate();
        parent.repaint();

        selector.setList(gridPanel.getPanelList());
        selector.start();
    }

}
