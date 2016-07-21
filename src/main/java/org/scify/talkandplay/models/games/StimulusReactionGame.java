package org.scify.talkandplay.models.games;

public class StimulusReactionGame extends Game {

    private int difficulty;

    public StimulusReactionGame() {
    }

    public StimulusReactionGame(String name, boolean enabled, int difficulty) {
        super(name, enabled);
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

}
