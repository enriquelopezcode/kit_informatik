package kit.codefight.exceptions;

/**
 * this exception is thrown when an index outside available memory size is referenced.
 * @author ukgyh
 */
public class MemoryOutOfBoundsException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */

    public MemoryOutOfBoundsException(String message) {
        super(message);
    }

}
