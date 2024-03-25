package kit.organiser.io;

/**
 * interface for handling input and output and interaction with the user.
 * @author ukgyh
 */
public interface InputOutputHandler {
    /**
     * displays a standard message to the user.
     * @param message the message to be displayed
     */
    void displayStandard(String message);
    /**
     * displays an error message to the user.
     * @param message the message to be displayed
     */
    void displayError(String message);
    /**
     * checks if there is new input from the user.
     * @return true if there is new input, false otherwise
     */
    boolean hasNewInput();
    /**
     * returns the input from the user.
     * @return the input from the user
     */
    String getInput();
    /**
     * closes the interaction with the user.
     */
    void closeInteraction();
}
