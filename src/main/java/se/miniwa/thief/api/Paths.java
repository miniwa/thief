package se.miniwa.thief.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import javafx.geometry.Pos;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.miniwa.thief.api.path.GuessedScoreComparator;
import se.miniwa.thief.api.path.NavMesh;
import se.miniwa.thief.api.path.Path;
import se.miniwa.thief.game.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public final class Paths {
    private static final Logger logger = LogManager.getLogger(Paths.class);

    public static Path find(Positionable from, Positionable to, NavMesh navMesh) {
        Position fromPos = from.getPosition();
        Position toPos = to.getPosition();
        Map<Position, Position> cameFrom = new HashMap<>();

        logger.debug("Finding path between: " + fromPos + " and " + toPos);
        Instant start = Instant.now();

        if(fromPos.equals(toPos)) {
            logger.debug("Finding path between equal positions.");
            return rebuildPath(toPos, cameFrom, navMesh);
        }

        if(navMesh.isPositionBlocked(to)) {
            logger.debug("To position is blocked.");
            return null;
        }

        Set<Position> evaluated = new HashSet<>();
        Set<Position> open = new HashSet<>();
        open.add(fromPos);

        Map<Position, Integer> realScores = new HashMap<>();
        realScores.put(fromPos, 0);

        Map<Position, Integer> guessedScores = new HashMap<>();
        guessedScores.put(fromPos, fromPos.distanceTo(toPos));

        GuessedScoreComparator comparator = new GuessedScoreComparator(guessedScores);
        while(!open.isEmpty()) {
            Position node = open.stream()
                    .min(comparator)
                    .get();

            // Exit if destination found.
            if(node.equals(toPos)) {
                Path path = rebuildPath(toPos, cameFrom, navMesh);
                Duration time = Duration.between(start, Instant.now());
                logger.debug("Calculated path in: " + time);
                return path;
            }

            open.remove(node);
            evaluated.add(node);

            for(Position neighbor : navMesh.getNeighbors(node)) {
                if(evaluated.contains(neighbor) || navMesh.isPositionBlocked(neighbor)) {
                    continue;
                }

                int realScore = realScores.get(node) + navMesh.estimateDistanceBetween(node, neighbor);
                if(!realScores.containsKey(neighbor) || realScore < realScores.get(neighbor)) {
                    cameFrom.put(neighbor, node);
                    realScores.put(neighbor, realScore);
                    guessedScores.put(neighbor, realScore + navMesh.estimateDistanceBetween(neighbor, to));
                }
                open.add(neighbor);
            }
        }

        Duration time = Duration.between(start, Instant.now());
        logger.debug("Failed pathfind finished in: " + time);

         // No path.
        return null;
    }

    public static NavMesh getNavMeshFor(Player player) {
        Board board = StateHolder.getBoard();
        if(board == null) {
            return null;
        }

        ImmutableSet.Builder<Position> builder = ImmutableSet.builder();
        PositionableQuery<Player> opponents = Players.getOpponentsFor(player);
        for(Player opponent : opponents) {
            builder.add(opponent.getPosition(), opponent.getBase());
        }

        return NavMesh.create(board.getWidth(), board.getHeight(), Portals.getPortal(), builder.build());
    }

    private static Path rebuildPath(Position to, Map<Position, Position> cameFrom, NavMesh navMesh) {
        Portal portal = navMesh.getPortal();
        Position firstExit = portal.getFirstExit();
        Position secondExit = portal.getSecondExit();
        ImmutableList.Builder<Position> builder = ImmutableList.builder();
        builder.add(to);

        Position current = to;
        while(cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            if(current.equals(firstExit)) {
                builder.add(secondExit);
            } else if(current.equals(secondExit)) {
                builder.add(firstExit);
            }
            builder.add(current);
        }
        return Path.create(ImmutableList.copyOf(Lists.reverse(builder.build())));
    }
}
