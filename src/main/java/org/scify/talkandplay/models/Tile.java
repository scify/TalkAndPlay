package org.scify.talkandplay.models;

import javax.swing.JPanel;

public class Tile {

    private JPanel panel;
    private boolean hasManualListener;

    public Tile() {

    }

    public Tile(JPanel panel, boolean hasManualListener) {
        this.panel = panel;
        this.hasManualListener = hasManualListener;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public boolean hasManualListener() {
        return hasManualListener;
    }

    public void setHasManualListener(boolean hasManualListener) {
        this.hasManualListener = hasManualListener;
    }

}
