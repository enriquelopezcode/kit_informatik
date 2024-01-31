package edu.kit.orlog.exceptions;

/**
 * This exception is thrown when a game element could not be created.
 * @author ukgyh
 */
public class GameElementCreationException extends Exception {
    private final String missingElementIdentifier;

    /**
     * Constructs a new GameElementCreationException.
     *
     * @param missingElementIdentifier the identifier of the missing element
     */
    public GameElementCreationException(String missingElementIdentifier) {
        this.missingElementIdentifier = missingElementIdentifier;
    }

    /**
     * Returns the identifier of the missing element.
     *
     * @return the identifier of the missing element
     */
    public String getMissingElementIdentifier() {
        return this.missingElementIdentifier;
    }
}
