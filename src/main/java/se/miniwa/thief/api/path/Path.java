package se.miniwa.thief.api.path;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import se.miniwa.thief.game.Direction;
import se.miniwa.thief.game.Position;
import se.miniwa.thief.game.Positionable;

@AutoValue
public abstract class Path {
    public static Path create(ImmutableList<Position> positions) {
        return new AutoValue_Path(positions);
    }

    public Position getDestination() {
        return getPositions().get(getPositions().size() - 1);
    }

    public Position getNextPositionFrom(Positionable positionable) {
        Position pos = positionable.getPosition();
        if(pos.equals(getDestination())) {
            return getDestination();
        }

        int index = getPositions().indexOf(pos);
        if(index == -1) {
            throw new IllegalArgumentException("Path does not visit position.");
        } else {
            return getPositions().get(index + 1);
        }
    }

    public Direction getNextDirectionFrom(Positionable positionable) {
        return positionable.directionTo(getNextPositionFrom(positionable));
    }

    public abstract ImmutableList<Position> getPositions();
}
