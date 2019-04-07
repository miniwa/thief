package se.miniwa.thief.bot;

import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.miniwa.thief.api.Diamonds;
import se.miniwa.thief.api.Paths;
import se.miniwa.thief.api.PositionableQuery;
import se.miniwa.thief.api.path.NavMesh;
import se.miniwa.thief.api.path.Path;
import se.miniwa.thief.game.*;

import java.time.Instant;
import java.util.Comparator;

public class Bot {
    private static final Logger logger = LogManager.getLogger(Bot.class);
    private PlayerBot playerBot;
    private int consecutiveFails = 0;

    public Bot(PlayerBot playerBot) {
        this.playerBot = playerBot;
    }

    public void tick(Instant now) {
        if(!playerBot.hasRegistered()) {
            logger.debug("Registering bot.");
            playerBot.register();
        }

        if(!playerBot.hasJoined()) {
            logger.debug("Joining game.");
            playerBot.join();
        }

        if(!playerBot.canMove()) {
            return;
        }

        Player self = playerBot.getLocalPlayer();
        NavMesh navMesh = Paths.getNavMeshFor(self);

        Path path;
        if(!self.isInventoryFull()) {
            path = null;
            PositionableQuery<Diamond> query = Diamonds.getDiamonds()
                    .where(diamond -> self.canPickup(diamond) && !self.getPosition().equals(diamond.getPosition()));
            query.sort(new DiamondsOverDistanceComparator(self, navMesh).reversed());

            for(Diamond diamond : query) {
                path = Paths.find(self, diamond, navMesh);
                if(path != null) {
                    break;
                }
            }
        } else {
            path = Paths.find(self, self.getBase(), navMesh);
        }

        if(path == null) {
            logger.debug("Could not find path to destination. Forcing game state update.");
            playerBot.updateState();
            return;
        }

        if(self.hasReachedDestinationOf(path)) {
            logger.debug("Player already at destination.");
            return;
        }

        Direction direction = path.getNextDirectionFrom(self);
        if(playerBot.move(direction)) {
            consecutiveFails = 0;
            logger.debug("Moved toward: " + direction);
        } else {
            logger.debug("Could not move towards: " + direction);
            consecutiveFails += 1;
            if(consecutiveFails > 3) {
                logger.debug("Too many fails. Forcing game state update.");
                playerBot.updateState();
                consecutiveFails = 0;
            }
        }
    }

    private class DiamondsOverDistanceComparator implements Comparator<Diamond> {
        private Player player;
        private NavMesh navMesh;

        public DiamondsOverDistanceComparator(Player player, NavMesh navMesh) {
            this.player = player;
            this.navMesh = navMesh;
        }

        @Override
        public int compare(Diamond left, Diamond right) {
            int leftFullDistance = navMesh.estimateDistanceBetween(player, left);
            int rightFullDistance = navMesh.estimateDistanceBetween(player, right);
            double leftDiamondsOverDistance = left.getSize() / (double)leftFullDistance;
            double rightDiamondsOverDistance = right.getSize() / (double)rightFullDistance;

            if(leftDiamondsOverDistance < rightDiamondsOverDistance) {
                return -1;
            } else if (leftDiamondsOverDistance > rightDiamondsOverDistance) {
                return 1;
            } else {
                return rightFullDistance - leftFullDistance;
            }
        }
    }
}
