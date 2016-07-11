package org.scify.talkandplay.gui.grid;

import java.awt.Color;
import org.scify.talkandplay.models.User;

public class BasePanel extends javax.swing.JPanel {

    protected GridFrame parent;
    protected User user;

    public BasePanel(User user, GridFrame parent) {
        this.user = user;
        this.parent = parent;
        setBackground(Color.white);
    }

    protected void showMainMenu() {
        parent.clearGrid();
        parent.addGrid(new GridPanel(user, parent));
        parent.revalidate();
        parent.repaint();
    }

}
