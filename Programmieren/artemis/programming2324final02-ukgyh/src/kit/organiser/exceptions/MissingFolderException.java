package kit.organiser.exceptions;

/**
 * Exception thrown when a folder that is trying to be accessed does not exist.
 * @author ukgyh
 */
public class MissingFolderException extends Exception {
    /**
     * Creates a new MissingFolderException with the given message.
     * @param message The message of the exception.
     */
    public MissingFolderException(String message) {
        super(message);
    }
}
