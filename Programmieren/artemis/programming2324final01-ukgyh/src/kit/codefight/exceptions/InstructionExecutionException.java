package kit.codefight.exceptions;

/**
 * this exception is thrown when there is a problem executing an AI instruction.
 * @author ukgyh
 */
public class InstructionExecutionException extends Exception {

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the throwable that caused exception to occur
     */
    public InstructionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
