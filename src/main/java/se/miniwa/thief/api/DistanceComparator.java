package se.miniwa.thief.api;

import se.miniwa.thief.game.Positionable;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Positionable> {
    private Positionable destination;

    public DistanceComparator(Positionable destination) {
        this.destination = destination;
    }

    @Override
    public int compare(Positionable left, Positionable right) {
        int leftDistance = left.getPosition().distanceTo(destination.getPosition());
        int rightDistance = right.getPosition().distanceTo(destination.getPosition());
        return leftDistance - rightDistance;
    }
}
