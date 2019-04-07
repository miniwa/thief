package se.miniwa.thief.api.path;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import se.miniwa.thief.game.Position;
import se.miniwa.thief.game.Positionable;

import java.util.Set;

@AutoValue
public abstract class NavMesh {
    public static NavMesh create(int width, int height, Set<Position> blockedPositions) {
        return new AutoValue_NavMesh(width, height, ImmutableSet.copyOf(blockedPositions));
    }

    public boolean isPositionBlocked(Positionable positionable) {
        return getBlockedPositions().contains(positionable.getPosition());
    }

    public ImmutableList<Position> getNeighbors(Positionable positionable) {
        Position pos = positionable.getPosition();
        ImmutableList.Builder<Position> builder = ImmutableList.builder();

        Position left = Position.create(pos.getX() - 1, pos.getY());
        if(left.getX() >= 0) {
            builder.add(left);
        }

        Position right = Position.create(pos.getX() + 1, pos.getY());
        if(right.getX() < getWidth()) {
            builder.add(right);
        }

        Position up = Position.create(pos.getX(), pos.getY() - 1);
        if(up.getY() >= 0) {
            builder.add(up);
        }

        Position down = Position.create(pos.getX(), pos.getY() + 1);
        if(down.getY() < getHeight()) {
            builder.add(down);
        }

        return builder.build();
    }

    public int estimateDistanceBetween(Positionable from, Positionable to) {
        return from.distanceTo(to);
    }

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract ImmutableSet<Position> getBlockedPositions();
}
