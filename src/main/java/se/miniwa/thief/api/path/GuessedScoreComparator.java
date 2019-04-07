package se.miniwa.thief.api.path;

import se.miniwa.thief.game.Position;

import java.util.Comparator;
import java.util.Map;

public class GuessedScoreComparator implements Comparator<Position> {
    private Map<Position, Integer> guessedScores;

    public GuessedScoreComparator(Map<Position, Integer> guessedScores) {
        this.guessedScores = guessedScores;
    }

    @Override
    public int compare(Position left, Position right) {
        int leftScore = guessedScores.get(left);
        int rightScore = guessedScores.get(right);
        return leftScore - rightScore;
    }
}
