package kit.codefight.exceptions;

/**
 * this exception is thrown when there is a problem during execution of the game process.
 * @author ukgyh
 */
public class GameExecutionException extends Exception {

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the throwable that caused exception to occur
     */
    public GameExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
