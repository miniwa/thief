package se.miniwa.thief.bot;

import se.miniwa.thief.game.Board;

public final class StateHolder {
    private static Board board;

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board newBoard) {
        board = newBoard;
    }
}
