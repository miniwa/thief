package se.miniwa.thief.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.miniwa.thief.api.path.NavMesh;
import se.miniwa.thief.api.path.Path;
import se.miniwa.thief.game.Portal;
import se.miniwa.thief.game.Position;

import java.util.HashSet;

public class PathsTest {
    @Test
    public void testPathFindBetweenAxisAlignedPositionsReturnsAllPositionsInPath() {
        Position first = Position.create(0, 0);
        Position second = Position.create(1, 0);
        Position third = Position.create(2, 0);
        Position last = Position.create(3, 0);

        Portal portal = Portal.create(Position.create(9, 9), Position.create(7, 7));
        NavMesh navMesh = NavMesh.create(10, 10, portal, new HashSet<>());
        Path path = Paths.find(first, last, navMesh);

        Assertions.assertNotNull(path);
        Assertions.assertEquals(4, path.getPositions().size());
        Assertions.assertEquals(second, path.getNextPositionFrom(first));
        Assertions.assertEquals(third, path.getNextPositionFrom(second));
        Assertions.assertEquals(last, path.getNextPositionFrom(third));
        Assertions.assertEquals(last, path.getNextPositionFrom(last));
        Assertions.assertEquals(last, path.getDestination());
    }

    public void testPathFindBetweenPortalsReturnsAllPositionsInPath() {
        Position first = Position.create(0, 0);
        Position second = Position.create(1, 0);
        Position third = Position.create(2, 0);
        Position fourth = Position.create(2, 4);
        Position fifth = Position.create(3, 4);
        Position last = Position.create(4, 4);

        Portal portal = Portal.create(Position.create(2, 0), Position.create(2, 4));
        NavMesh navMesh = NavMesh.create(10, 10, portal, new HashSet<>());
        Path path = Paths.find(first, last, navMesh);

        Assertions.assertNotNull(path);
        Assertions.assertEquals(6, path.getPositions().size());
        Assertions.assertEquals(second, path.getNextPositionFrom(first));
        Assertions.assertEquals(third, path.getNextPositionFrom(second));
        Assertions.assertEquals(fourth, path.getNextPositionFrom(third));
        Assertions.assertEquals(fifth, path.getNextPositionFrom(fourth));
        Assertions.assertEquals(last, path.getNextPositionFrom(fifth));
        Assertions.assertEquals(last, path.getNextPositionFrom(last));
        Assertions.assertEquals(last, path.getDestination());
    }

    public void testPathFindFindsShortestPathWhenStartingPositionIsPortalExit() {
        Position first = Position.create(0, 0);
        Position second = Position.create(1, 0);
        Position last = Position.create(2, 0);

        Portal portal = Portal.create(first, Position.create(8, 8));
        NavMesh navMesh = NavMesh.create(10, 10, portal, new HashSet<>());
        Path path = Paths.find(first, last, navMesh);

        Assertions.assertNotNull(path);
        Assertions.assertEquals(3, path.getPositions().size());
        Assertions.assertEquals(second, path.getNextPositionFrom(first));
        Assertions.assertEquals(last, path.getNextPositionFrom(second));
        Assertions.assertEquals(last, path.getNextPositionFrom(last));
        Assertions.assertEquals(last, path.getDestination());
    }
}
