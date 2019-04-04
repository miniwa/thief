package se.miniwa.diamond;

import java.time.Duration;
import java.util.List;

public class Board {
    private String id;
    private int width;
    private int height;
    private Duration delayBetweenMoves;

    private List<Diamond> diamonds;
    private List<BoardBot> bots;

    public Board(String id, int width, int height, Duration delayBetweenMoves, List<Diamond> diamonds,
                 List<BoardBot> bots) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.delayBetweenMoves = delayBetweenMoves;
        this.diamonds = diamonds;
        this.bots = bots;
    }

    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Duration getDelayBetweenMoves() {
        return delayBetweenMoves;
    }

    public List<Diamond> getDiamonds() {
        return diamonds;
    }

    public List<BoardBot> getBots() {
        return bots;
    }
}
