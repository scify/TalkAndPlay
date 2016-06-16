package org.scify.talkandplay.models.modules;

import java.util.List;
import org.scify.talkandplay.models.games.SequenceGame;
import org.scify.talkandplay.models.games.SimilarityGame;
import org.scify.talkandplay.models.games.StimulusReactionGame;

public class GameModule extends Module {

    private List<SequenceGame> sequenceGames;
    private List<StimulusReactionGame> stimulusReactionGames;
    private List<SimilarityGame> similarityGames;

    public GameModule() {
    }

    public List<SequenceGame> getSequenceGames() {
        return sequenceGames;
    }

    public void setSequenceGames(List<SequenceGame> sequenceGames) {
        this.sequenceGames = sequenceGames;
    }

    public List<StimulusReactionGame> getStimulusReactionGames() {
        return stimulusReactionGames;
    }

    public void setStimulusReactionGames(List<StimulusReactionGame> stimulusReactionGames) {
        this.stimulusReactionGames = stimulusReactionGames;
    }

    public List<SimilarityGame> getSimilarityGames() {
        return similarityGames;
    }

    public void setSimilarityGames(List<SimilarityGame> similarityGames) {
        this.similarityGames = similarityGames;
    }

}
