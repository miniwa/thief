package se.miniwa.bot;

import se.miniwa.diamond.Board;

public class StateHolder {
    private Board board;
    private String token;

    public StateHolder(Board board, String token) {
        this.board = board;
        this.token = token;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
