package kit.organiser.command;

import kit.organiser.io.InputOutputHandler;
import kit.organiser.model.FileManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class handles the user input and executes the commands.
 *
 * @author Programmieren-Team
 * @author ukgyh
 */
public final class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = " ";
    private static final String COMMAND_NOT_FOUND_FORMAT = "command '%s' not found!";
    private static final String WRONG_ARGUMENTS_COUNT_FORMAT = "wrong number of arguments for command '%s'!";
    private static final String UNEXPECTED_VALUE_FORMAT = "Unexpected value: %s";
    private static final String RUN_COMMAND_NAME = "run";
    private static final String CHANGE_COMMAND_NAME = "change";
    private static final String LOAD_COMMAND_NAME = "load";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int START_OF_ARGUMENTS_INDEX = 1;
    private final InputOutputHandler inputOutputHandler;
    private final FileManager fileManager;
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler.
     *
     * @param fileManager the file manager that this instance manages
     * @param inputOutputHandler interface that handles receiving and outputting information to the user
     */
    public CommandHandler(FileManager fileManager, InputOutputHandler inputOutputHandler) {
        this.fileManager = Objects.requireNonNull(fileManager);
        this.inputOutputHandler = inputOutputHandler;
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Starts the interaction with the user.
     */
    public void handleUserInput() {
        this.running = true;

        while (running && inputOutputHandler.hasNewInput()) {
            executeCommand(inputOutputHandler.getInput());
        }
        inputOutputHandler.closeInteraction();
    }

    /**
     * Quits the interaction with the user.
     */
    public void quit() {
        this.running = false;
    }


    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[COMMAND_NAME_INDEX];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, START_OF_ARGUMENTS_INDEX, splittedCommand.length);

        executeCommand(commandName, commandArguments);
    }

    private void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            inputOutputHandler.displayError(COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
        } else if (commands.get(commandName).getNumberOfArguments() != commandArguments.length) {
            inputOutputHandler.displayError(WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));
        }  else {
            CommandResult result = commands.get(commandName).execute(fileManager, commandArguments);
            String output = result.getMessage();

            if (output != null) {
                switch (result.getType()) {
                    case SUCCESS -> inputOutputHandler.displayStandard(output);
                    case FAILURE -> inputOutputHandler.displayError(output);
                    default -> throw new IllegalStateException(UNEXPECTED_VALUE_FORMAT.formatted(result.getType()));
                }
            }
        }
    }

    private void initCommands() {
        this.addCommand(RUN_COMMAND_NAME, new RunCommand());
        this.addCommand(LOAD_COMMAND_NAME, new LoadCommand());
        this.addCommand(CHANGE_COMMAND_NAME, new ChangeCommand());
        this.addCommand(QUIT_COMMAND_NAME, new QuitCommand(this));
    }

    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }
}
