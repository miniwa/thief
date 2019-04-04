package se.miniwa.diamond;

public class Diamond {
    private int points;
    private Position position;

    public Diamond(int points, Position position) {
        this.points = points;
        this.position = position;
    }

    public int getPoints() {
        return points;
    }

    public Position getPosition() {
        return position;
    }
}
