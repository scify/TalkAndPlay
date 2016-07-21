package org.scify.talkandplay.models.games;

/**
 *
 * @author christina
 */
public class SequenceGame extends Game{

    private int difficulty;

    public SequenceGame() {
    }

    public SequenceGame(String name, boolean enabled, int difficulty) {
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
