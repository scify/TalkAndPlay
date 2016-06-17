package org.scify.talkandplay.models.games;

import java.util.List;

/**
 *
 * @author christina
 */
public class SequenceGame extends Game{

    private int difficulty;
    private List<GameImage> images;

    public SequenceGame() {
    }

    public SequenceGame(String name, String image, boolean enabled, int difficulty) {
        super(name, image, enabled);
        this.difficulty = difficulty;
        this.images = images;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<GameImage> getImages() {
        return images;
    }

    public void setImages(List<GameImage> images) {
        this.images = images;
    }
}
