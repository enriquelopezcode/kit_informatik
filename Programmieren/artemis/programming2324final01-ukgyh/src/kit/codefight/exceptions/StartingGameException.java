package kit.codefight.exceptions;

/**
 * this exception is thrown when there is a problem with starting the game.
 * @author ukgyh
 */
public class StartingGameException extends Exception {

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the throwable that caused exception to occur
     */
    public StartingGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
