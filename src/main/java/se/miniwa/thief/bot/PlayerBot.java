package se.miniwa.thief.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.miniwa.thief.api.Players;
import se.miniwa.thief.api.PositionableQuery;
import se.miniwa.thief.api.StateHolder;
import se.miniwa.thief.game.Board;
import se.miniwa.thief.game.Direction;
import se.miniwa.thief.game.Player;
import se.miniwa.thief.game.client.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static se.miniwa.thief.api.Players.getPlayers;

public class PlayerBot {
    private static final Logger logger = LogManager.getLogger(PlayerBot.class);
    private DiamondClient client;
    private String name;
    private String email;
    private String token;
    private Instant nextMoveAvailableAt = Instant.MIN;

    public PlayerBot(DiamondClient client, String name, String email) {
        this.client = client;
        this.name = name;
        this.email = email;
    }

    public PlayerBot(DiamondClient client, String name, String email, String token) {
        this.client = client;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public boolean hasRegistered() {
        return token != null;
    }

    public void register() {
        if(hasRegistered()) {
            throw new IllegalStateException("Bot has already been registered.");
        }

        try {
            token = client.registerBot(name, email);
        } catch(IOException ex) {
            logger.catching(ex);
            throw new BotException("Could not register api.", ex);
        }
    }

    public boolean hasJoined() {
        return getLocalPlayer() != null;
    }

    public void join() {
        if(!hasRegistered()) {
            throw new IllegalStateException("Bot has not been registered yet.");
        }

        if(hasJoined()) {
            throw new IllegalStateException("Bot has already joined.");
        }

        try {
            client.joinBoard(client.getBoards().get(0).getId(), token);
            StateHolder.setBoard(client.getBoards().get(0));
        }
        catch(BoardFullException ex) {
            // Bot most likely already on board, ignore.
            try {
                StateHolder.setBoard(client.getBoards().get(0));
            } catch(IOException exc) {
                logger.warn(exc);
            }
        }
        catch(IOException|DiamondException ex) {
            throw new BotException("Could not join board.", ex);
        }
    }

    public Player getLocalPlayer() {
        PositionableQuery<Player> players = Players.getPlayers();
        if(players == null) {
            return null;
        }

        for(Player player : players) {
            if(player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public boolean canMove() {
        Instant now = Instant.now();
        return now.isAfter(nextMoveAvailableAt);
    }

    public boolean move(Direction direction) {
        if(!hasJoined()) {
            throw new BotException("Bot has not yet joined.");
        }

        try {
            Instant start = Instant.now();
            Board board = client.moveBot(StateHolder.getBoard().getId(), token, direction.getText());
            Instant end = Instant.now();
            Duration latency = Duration.between(start, end);

            StateHolder.setBoard(board);
            nextMoveAvailableAt = end;
            return true;
        } catch(InvalidMoveException|InvalidBotException ex) {
            return false;
        } catch(IOException|DiamondException ex) {
            throw new BotException("Could not make move.", ex);
        }
    }

    public void updateState() {
        try {
            StateHolder.setBoard(client.getBoards().get(0));
        } catch (IOException ex) {
            logger.debug("Error when attempting to update state.", ex);
        }
    }
}
