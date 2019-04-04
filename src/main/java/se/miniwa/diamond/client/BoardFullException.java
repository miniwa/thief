package se.miniwa.diamond.client;

public class BoardFullException extends DiamondException {
    public BoardFullException() {
    }

    public BoardFullException(String message) {
        super(message);
    }

    public BoardFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardFullException(Throwable cause) {
        super(cause);
    }

    public BoardFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
