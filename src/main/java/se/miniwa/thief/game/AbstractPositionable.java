package se.miniwa.thief.game;

public abstract class AbstractPositionable implements Positionable {
    @Override
    public int distanceTo(Positionable destination) {
        Position pos = getPosition();
        Position destPos = destination.getPosition();
        return Math.abs(destPos.getX() - pos.getX()) + Math.abs(destPos.getY() - pos.getY());
    }

    @Override
    public Direction directionTo(Positionable destination) {
        Position pos = getPosition();
        Position destPos = destination.getPosition();
        int diffX = destPos.getX() - pos.getX();
        int diffY = destPos.getY() - pos.getY();
        if(diffX > 0) {
            return Direction.EAST;
        } else if (diffX < 0) {
            return Direction.WEST;
        }

        if(diffY > 0) {
            return Direction.SOUTH;
        } else if(diffY < 0) {
            return Direction.NORTH;
        }

        throw new IllegalArgumentException("Cannot calculate direction to self.");
    }
}
