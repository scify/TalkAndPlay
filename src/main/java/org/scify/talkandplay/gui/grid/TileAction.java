package org.scify.talkandplay.gui.grid;

public abstract class TileAction {

    public boolean mute() {
        return false;
    }

    public abstract void act();

    public abstract void audioFinished();
}
