package kit.codefight.display;

import java.util.Scanner;

/**
 * This class handles the user interaction via command line as input and output.
 * @author ukgyh
 */
public final class InputOutputCommandLine implements InputOutputHandler {
    private static final String ERROR_PREFIX = "Error, ";
    private final Scanner scanner;

    /**
     * creates a new InputOutputCommandLine object with a new Scanner object.
     */
    public InputOutputCommandLine() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    @Override
    public  void displayError(String message) {
        System.err.println(ERROR_PREFIX + message);
    }

    @Override
    public void displayStandard(String message) {
        System.out.println(message);
    }

    @Override
    public boolean hasNewInput() {
        return scanner.hasNext();
    }

    @Override
    public void closeInteraction() {
        scanner.close();
    }
}
