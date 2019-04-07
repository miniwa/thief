package se.miniwa.thief.game;

public interface Positionable {
    Position getPosition();
    int distanceTo(Positionable destination);
    Direction directionTo(Positionable destination);
}
