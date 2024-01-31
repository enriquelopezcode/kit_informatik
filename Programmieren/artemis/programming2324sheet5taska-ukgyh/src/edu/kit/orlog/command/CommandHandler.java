package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * The {@code CommandHandler} class is responsible for managing and executing
 * commands within a game. It handles user input, parses it into commands,
 * and executes them according to the game's rules and state.
 * @author ukgyh
 */
public final class CommandHandler {
    private static final String ROLL_COMMAND_NAME = "roll";
    private static final String GODFAVOR_COMMAND_NAME = "godfavor";
    private static final String TURN_COMMAND_NAME = "turn";
    private static final String EVALUATE_COMMAND_NAME = "evaluate";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final String PRINT_COMMAND_NAME = "print";


    private static final String COMMAND_SEPARATOR_REGEX = " +";
    private static final String ERROR_PREFIX = "Error, ";
    private static final String WRONG_GAME_PHASE_FORMAT = "wrong game phase for this command.";
    private static final String COMMAND_NOT_FOUND_FORMAT = "command '%s' not found!";
    private static final String WRONG_ARGUMENTS_COUNT_FORMAT = "wrong number of arguments for command '%s'!";
    private static final String UNEXPECTED_VALUE_FORMAT = "Unexpected value: %s";
    private final Game game;
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler for a given game.
     *
     * @param game The game instance this handler will manage commands for.
     */
    public CommandHandler(Game game) {
        this.game = Objects.requireNonNull(game);
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Starts handling user input from the standard input.
     * Continues to read and execute commands until the {@code quit} method is called.
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the interaction with the user and the game.
     */
    public void quit() {
        this.running = false;
    }

    /**
     * Executes a command given as a single string, which includes the command name
     * and its arguments.
     *
     * splits the command arguments and the command name  and calls the other executeCommand method
     *
     * @param commandWithArguments The full command string including the command name
     *                             and its arguments separated by spaces.
     */
    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);

        executeCommand(commandName, commandArguments);
    }

    /**
     * Executes a command based on its name and a string array of arguments.
     * It first checks if the command exists, validates the number of arguments,
     * and then executes the command if the game is in the correct phase.
     *
     * @param commandName The name of the command to be executed.
     * @param commandArguments An array of strings representing the arguments for the command.
     */
    private void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            System.err.println(ERROR_PREFIX + COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
        } else if (commands.get(commandName).getNumberOfArguments() != commandArguments.length) {
            System.err.println(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));

        }  else {
            if (commands.get(commandName).requiresGamePhase()) {
                if (commands.get(commandName).getGamePhase() != game.getCurrentGamePhase()) {
                    System.err.println(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT);
                    return;
                }
            }

            CommandResult result = commands.get(commandName).execute(game, commandArguments);
            String output = switch (result.getType()) {
                case SUCCESS -> result.getMessage();
                case FAILURE -> ERROR_PREFIX + result.getMessage();
            };
            if (output != null) {
                switch (result.getType()) {
                    case SUCCESS -> System.out.println(output);
                    case FAILURE -> System.err.println(output);
                    default -> throw new IllegalStateException(UNEXPECTED_VALUE_FORMAT.formatted(result.getType()));
                }
            }
        }
    }

    /**
     * Initializes the set of commands available in this handler.
     * It associates command names with their respective command objects.
     */
    private void initCommands() {
        this.addCommand(ROLL_COMMAND_NAME, new RollCommand());
        this.addCommand(TURN_COMMAND_NAME, new TurnCommand());
        this.addCommand(EVALUATE_COMMAND_NAME, new EvaluateCommand());
        this.addCommand(PRINT_COMMAND_NAME, new PrintCommand());
        this.addCommand(GODFAVOR_COMMAND_NAME, new GodFavorCommand());
        this.addCommand(QUIT_COMMAND_NAME, new QuitCommand(this));
    }

    /**
     * Adds a new command to the handler.
     *
     * @param commandName The name of the command to be added.
     * @param command The command object associated with the command name.
     */
    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }
}
