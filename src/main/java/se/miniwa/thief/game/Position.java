package se.miniwa.thief.game;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Position {
    public static Position create(int x, int y) {
        return new AutoValue_Position(x, y);
    }

    public int distanceTo(Position destination) {
        return Math.abs(destination.getX() - getX()) + Math.abs(destination.getX() - getY());
    }

    public Direction directionTo(Position destination) {
        int diffX = destination.getX() - getX();
        int diffY = destination.getY() - getY();
        if(diffX > 0) {
            return Direction.EAST;
        } else if (diffX < 0) {
            return Direction.WEST;
        }

        if(diffY > 0) {
            return Direction.NORTH;
        } else if(diffY < 0) {
            return Direction.SOUTH;
        }

        throw new IllegalArgumentException("Cannot calculate direction to self.");
    }

    public abstract int getX();
    public abstract int getY();
}
