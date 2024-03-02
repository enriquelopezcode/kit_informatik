package kit.codefight.exceptions;

/**
 * This exception is thrown when input arguments are invalid.
 * @author ukgyh
 */
public class ArgumentInvalidException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public ArgumentInvalidException(String message) {
        super(message);
    }
}
