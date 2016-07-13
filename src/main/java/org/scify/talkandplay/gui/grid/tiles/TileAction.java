package org.scify.talkandplay.gui.grid.tiles;

public abstract class TileAction {

    public boolean mute() {
        return false;
    }

    public abstract void act();

    public abstract void audioFinished();
}
