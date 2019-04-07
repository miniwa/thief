package se.miniwa.thief.api;

import se.miniwa.thief.game.Board;
import se.miniwa.thief.game.Player;

public final class Players {
    public static PositionableQuery<Player> getPlayers() {
        Board board = StateHolder.getBoard();
        if(board == null) {
            return null;
        }

        PositionableQuery<Player> query = new PositionableQuery<>();
        query.addAll(board.getPlayers());
        return query;
    }

    public static PositionableQuery<Player> getOpponentsFor(Player player) {
        PositionableQuery<Player> query = getPlayers();
        if(query == null) {
            return null;
        }

        query.remove(player);
        return query;
    }
}
