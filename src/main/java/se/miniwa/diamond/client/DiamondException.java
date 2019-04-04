package se.miniwa.diamond.client;

public class DiamondException extends Exception {
    public DiamondException() {
    }

    public DiamondException(String message) {
        super(message);
    }

    public DiamondException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiamondException(Throwable cause) {
        super(cause);
    }

    public DiamondException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
