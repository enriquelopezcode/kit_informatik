package kit.organiser.exceptions;

/**
 * Exception thrown when a folder cannot be created.
 * @author ukgyh
 */
public class FolderCreationException extends Exception {
    /**
     * Creates a new FolderCreationException with the given message.
     * @param message The message of the exception.
     */
    public FolderCreationException(String message) {
        super(message);
    }
}
