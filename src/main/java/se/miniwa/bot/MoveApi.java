package se.miniwa.bot;

import se.miniwa.diamond.Board;
import se.miniwa.diamond.BoardBot;
import se.miniwa.diamond.Position;
import se.miniwa.diamond.client.DiamondClient;
import se.miniwa.diamond.client.DiamondException;
import se.miniwa.diamond.client.InvalidBotException;

import java.io.IOException;
import java.time.Instant;

public class MoveApi {
    private StateHolder state;
    private DiamondClient client;
    private BotApi botApi;
    private String token;

    public boolean botAllowedToMove(BoardBot bot) {
        Instant now = Instant.now();
        Instant nextMoveAvailable = bot.getNextMoveAvailableAt();
        return now.isAfter(nextMoveAvailable);
    }

    public boolean allowedToMove() {
        return botAllowedToMove(botApi.getPlayer());
    }

    public boolean move(Direction direction) {
        try {
            Board board = client.moveBot(state.getBoard().getId(), token, direction.getText());
            state.setBoard(board);
            return true;
        } catch(InvalidBotException ex) {
            return false;
        } catch(IOException|DiamondException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Direction getDirectionTo(Position position, Position destination) {
        int x = position.getX();
        int y = position.getY();
        int destX = destination.getX();
        int destY = destination.getY();

        if(x < destX) {
            return Direction.EAST;
        }

        if(x > destX) {
            return Direction.WEST;
        }

        if(y < destY) {
            return Direction.NORTH;
        }

        if(y > destY) {
            return Direction.SOUTH;
        }
        throw new IllegalArgumentException("Position and destination cannot be equal.");
    }

    public enum Direction {
        NORTH("North"),
        EAST("East"),
        SOUTH("South"),
        WEST("West");

        private String text;
        Direction(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
