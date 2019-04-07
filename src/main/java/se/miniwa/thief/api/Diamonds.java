package se.miniwa.thief.api;

import com.google.common.collect.ImmutableList;
import se.miniwa.thief.game.Board;
import se.miniwa.thief.game.Diamond;

public final class Diamonds {
    public static PositionableQuery<Diamond> getDiamonds() {
        Board board = StateHolder.getBoard();
        if(board == null) {
            return null;
        }

        PositionableQuery<Diamond> query = new PositionableQuery<>();
        query.addAll(board.getDiamonds());
        return query;
    }
}
