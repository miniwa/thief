package se.miniwa.thief.game;

public enum Direction {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West");

    private String text;

    Direction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
