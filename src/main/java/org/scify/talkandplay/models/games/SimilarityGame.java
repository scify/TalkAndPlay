package org.scify.talkandplay.models.games;

/**
 *
 * @author christina
 */
public class SimilarityGame extends Game {

    private int difficulty;

    public SimilarityGame() {
    }

    public SimilarityGame(String name, boolean enabled, int difficulty) {
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
