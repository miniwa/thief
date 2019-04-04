package se.miniwa.diamond;

import java.time.Instant;

public class BoardBot {
    private String name;
    private String id;
    private Position position;
    private Position base;
    private int diamondCount;
    private int score;
    private Instant nextMoveAvailableAt;
    private Instant roundOverAt;

    public BoardBot(String name, String id, Position position, Position base, int diamondCount, int score,
                    Instant nextMoveAvailableAt, Instant roundOverAt) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.base = base;
        this.diamondCount = diamondCount;
        this.score = score;
        this.nextMoveAvailableAt = nextMoveAvailableAt;
        this.roundOverAt = roundOverAt;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public Position getBase() {
        return base;
    }

    public int getDiamondCount() {
        return diamondCount;
    }

    public int getScore() {
        return score;
    }

    public Instant getNextMoveAvailableAt() {
        return nextMoveAvailableAt;
    }

    public Instant getRoundOverAt() {
        return roundOverAt;
    }
}
