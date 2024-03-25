package kit.organiser;

import kit.organiser.command.CommandHandler;
import kit.organiser.io.InputOutputCommandLine;
import kit.organiser.io.InputOutputHandler;
import kit.organiser.model.FileManager;

/**
 * The main class of the Overpowered Organiser. Serves as the entry point for the program.
 * @author ukgyh
 */
public final class OverpoweredOrganiser {
    private static final String INSTANTIATION_ERROR = "Utility class cannot be instantiated";
    private static final String STARTING_MESSAGE = "Use one of the following commands: "
            + "load <path>, run <id>, change <id> <file> <number>, quit";

    private OverpoweredOrganiser() {
        throw new IllegalStateException(INSTANTIATION_ERROR);
    }

    /**
     * The main method of the program. Serves as the entry point for the program.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        InputOutputHandler inputOutputHandler = new InputOutputCommandLine();
        inputOutputHandler.displayStandard(STARTING_MESSAGE);

        FileManager fileManager = new FileManager();
        CommandHandler commandHandler = new CommandHandler(fileManager, inputOutputHandler);
        commandHandler.handleUserInput();
    }
}
