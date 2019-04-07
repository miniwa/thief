package se.miniwa.thief.api.path;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import se.miniwa.thief.game.Portal;
import se.miniwa.thief.game.Position;
import se.miniwa.thief.game.Positionable;

import java.util.Set;

@AutoValue
public abstract class NavMesh {
    public static NavMesh create(int width, int height, Portal portal, Set<Position> blockedPositions) {
        return new AutoValue_NavMesh(width, height, portal, ImmutableSet.copyOf(blockedPositions));
    }

    public boolean isPositionBlocked(Positionable positionable) {
        return getBlockedPositions().contains(positionable.getPosition());
    }

    public ImmutableList<Position> getNeighbors(Positionable positionable) {
        Position pos = positionable.getPosition();
        Position firstExit = getPortal().getFirstExit();
        Position secondExit = getPortal().getSecondExit();

        if(pos.equals(firstExit)) {
            return getNeighborsInternal(secondExit);
        } else if(pos.equals(secondExit)) {
            return getNeighborsInternal(firstExit);
        } else {
            return getNeighborsInternal(pos);
        }
    }

    public int estimateDistanceBetween(Positionable from, Positionable to) {
        Portal portal = getPortal();
        Position firstExit = portal.getFirstExit();
        Position secondExit = portal.getSecondExit();

        int regularDistance = from.distanceTo(to);
        int firstPortalDistance = from.distanceTo(firstExit) + secondExit.distanceTo(to);
        int secondPortalDistance = from.distanceTo(secondExit) + firstExit.distanceTo(to);
        int smallestPortalDistance = Math.min(firstPortalDistance, secondPortalDistance);
        return Math.min(regularDistance, smallestPortalDistance);
    }

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract Portal getPortal();
    public abstract ImmutableSet<Position> getBlockedPositions();

    private ImmutableList<Position> getNeighborsInternal(Position pos) {
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
}
