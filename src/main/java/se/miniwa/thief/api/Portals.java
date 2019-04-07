package se.miniwa.thief.api;

import se.miniwa.thief.game.Board;
import se.miniwa.thief.game.Portal;

public final class Portals {
    public static Portal getPortal() {
        Board board = StateHolder.getBoard();
        if(board == null) {
            return null;
        }

        return board.getPortal();
    }
}
