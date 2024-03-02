package kit.codefight.exceptions;

/**
 * This exception is thrown when there is a problem creating an Instruction object.
 * @author ukgyh
 */
public class InstructionCreationException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public InstructionCreationException(String message) {
        super(message);
    }
}
