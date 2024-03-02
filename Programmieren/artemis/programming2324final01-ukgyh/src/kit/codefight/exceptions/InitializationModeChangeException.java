package kit.codefight.exceptions;

/**
 * This exception is thrown when there is a problem changing the initialization mode of the game.
 * @author ukgyh
 *
 */
public class InitializationModeChangeException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public InitializationModeChangeException(String message) {
        super(message);
    }

}
