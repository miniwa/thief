package se.miniwa.thief.api;

import se.miniwa.thief.game.Positionable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PositionableQuery<T extends Positionable> extends ArrayList<T> {
    public T closestTo(Positionable positionable) {
        return orderByDistanceTo(positionable).get(0);
    }

    public PositionableQuery<T> orderByDistanceTo(Positionable positionable) {
        sort(new DistanceComparator(positionable));
        return this;
    }

    public PositionableQuery<T> where(Predicate<T> predicate) {
        return stream().filter(predicate)
                .collect(Collectors.toCollection(PositionableQuery::new));
    }
}
