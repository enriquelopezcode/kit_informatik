package kit.codefight.exceptions;

/**
 * this exception is thrown when starting instructions overlap during memory initialization.
 * @author ukgyh
 */
public class MemoryOverloadException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public MemoryOverloadException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the throwable that caused exception to occur
     */
    public MemoryOverloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
