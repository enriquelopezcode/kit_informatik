package kit.organiser.exceptions;

/**
 * Exception thrown when a tag cannot be created.
 * @author ukgyh
 */
public class TagCreationException extends Exception {
    /**
     * Creates a new TagCreationException with the given message.
     * @param message The message of the exception.
     */
    public TagCreationException(String message) {
        super(message);
    }
}
