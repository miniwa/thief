package se.miniwa.thief.game.client;

public class InvalidBoardException extends DiamondException {
    public InvalidBoardException() {
    }

    public InvalidBoardException(String message) {
        super(message);
    }

    public InvalidBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBoardException(Throwable cause) {
        super(cause);
    }

    public InvalidBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
