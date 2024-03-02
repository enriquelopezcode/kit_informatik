package kit.codefight.command;

import kit.codefight.display.InputOutputHandler;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

import java.util.Objects;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

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
    private static final String HELP_COMMAND_NAME = "help";
    private static final String ADD_AI_COMMAND_NAME = "add-ai";
    private static final String REMOVE_AI_COMMAND_NAME = "remove-ai";
    private static final String END_GAME_COMMAND_NAME = "end-game";
    private static final String SHOW_AI_COMMAND_NAME = "show-ai";
    private static final String NEXT_COMMAND_NAME = "next";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final String SET_MODE_COMMAND_NAME = "set-init-mode";
    private static final String SHOW_MEMORY_COMMAND_NAME = "show-memory";
    private static final String START_GAME_COMMAND_NAME = "start-game";
    private static final String INVALID_RESULT_TYPE_FORMAT = "Unexpected value: %s";
    private static final String WRONG_GAME_PHASE_ERROR = "command not available in the current game phase";
    private static final int EMPTY_ARRAY_SIZE = 0;
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int START_OF_ARGUMENTS_INDEX = 1;
    private final InputOutputHandler inputOutputHandler;
    private final GameEngine gameEngine;
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler.
     *
     * @param gameEngine the CodeFight game that this instance manages
     * @param inputOutputHandler interface that handles receiving and outputting information to the user
     */
    public CommandHandler(GameEngine gameEngine, InputOutputHandler inputOutputHandler) {
        this.gameEngine = Objects.requireNonNull(gameEngine);
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

    /**
     * provides the name and information about available commands.
     * @return a list of string arrays including command information
     *                  where there is an entry for the command name and an entry for the command information
     */
    public List<String[]> getAvailableCommandInfo() {
        List<String[]> availableCommandNames = new ArrayList<>();
        GamePhase currentPhase = gameEngine.getCurrentGamePhase();
        for (String commandName : commands.keySet().toArray(new String[EMPTY_ARRAY_SIZE])) {
            Command command = commands.get(commandName);
            boolean commandAvailable = !command.requiresGamePhase() || command.getRequiredGamePhase() == currentPhase;

            if (commandAvailable) {
                availableCommandNames.add(new String[]{commandName, command.getInfoText()});
            }
        }

        return availableCommandNames;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[COMMAND_NAME_INDEX];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, START_OF_ARGUMENTS_INDEX, splittedCommand.length);

        executeCommand(commandName, commandArguments);
    }

    private void executeCommand(String commandName, String[] commandArguments) {
        // Check if the command exists
        if (!commands.containsKey(commandName)) {
            inputOutputHandler.displayError(COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
            return;
        }

        Command command = commands.get(commandName);

        // Check for correct number of arguments
        if (!command.isValidArgumentAmount(commandArguments.length)) {
            inputOutputHandler.displayError(WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));
            return;
        }

        // Check if the command can be executed in the current game phase
        if (gameEngine.getCurrentGamePhase() != command.getRequiredGamePhase() && command.getRequiredGamePhase() != GamePhase.NONE) {
            inputOutputHandler.displayError(WRONG_GAME_PHASE_ERROR);
            return;
        }

        // Execute the command
        CommandResult result = command.execute(gameEngine, commandArguments);
        String output = result.getMessage();

        // Display the output based on the result type
        if (output != null) {
            switch (result.getType()) {
                case SUCCESS -> inputOutputHandler.displayStandard(output);
                case FAILURE -> inputOutputHandler.displayError(output);
                default -> throw new IllegalStateException(INVALID_RESULT_TYPE_FORMAT.formatted(result.getType()));
            }
        }
    }

    private void initCommands() {
        this.addCommand(ADD_AI_COMMAND_NAME, new AddAiCommand());
        this.addCommand(REMOVE_AI_COMMAND_NAME, new RemoveAiCommand());
        this.addCommand(HELP_COMMAND_NAME, new HelpCommand(this));
        this.addCommand(END_GAME_COMMAND_NAME, new EndGameCommand());
        this.addCommand(SHOW_AI_COMMAND_NAME, new ShowAiCommand());
        this.addCommand(NEXT_COMMAND_NAME, new NextCommand());
        this.addCommand(SET_MODE_COMMAND_NAME, new SetModeCommand());
        this.addCommand(START_GAME_COMMAND_NAME, new StartGameCommand(gameEngine.getMaxNumberAi()));
        this.addCommand(QUIT_COMMAND_NAME, new QuitCommand(this));
        this.addCommand(SHOW_MEMORY_COMMAND_NAME, new ShowMemoryCommand());
    }

    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }
}
