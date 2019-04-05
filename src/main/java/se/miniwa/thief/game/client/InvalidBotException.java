package se.miniwa.thief.game.client;

public class InvalidBotException extends DiamondException {
    public InvalidBotException() {
    }

    public InvalidBotException(String message) {
        super(message);
    }

    public InvalidBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBotException(Throwable cause) {
        super(cause);
    }

    public InvalidBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
