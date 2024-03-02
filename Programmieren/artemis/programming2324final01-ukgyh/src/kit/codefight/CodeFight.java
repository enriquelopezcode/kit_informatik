package kit.codefight;

import kit.codefight.command.CommandHandler;
import kit.codefight.display.InputOutputCommandLine;
import kit.codefight.display.InputOutputHandler;
import kit.codefight.exceptions.ArgumentInvalidException;
import kit.codefight.model.GameEngine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * entry point for the program that parses starting arguments and starts the user interaction.
 * @author ukgyh
 */

public final class CodeFight {
    private static final String FORBIDDEN_SYMBOL_CHARACTER = " ";
    private static final String FORBIDDEN_SYMBOL_ERROR = "Symbols can't contain the character '%s'";
    private static final String INSTANTIATION_ERROR = "utility class cannot be instantiated";
    private static final String INVALID_ARGUMENT_AMOUNT_ERROR = "invalid amount of starting arguments";
    private static final String MEMORY_SIZE_INVALID_INTEGER_ERROR = "memory size must be a valid integer";
    private static final String MEMORY_SIZE_BOUND_ERROR = "memory size must be between %d and %d";
    private static final String SYMBOLS_NOT_UNIQUE_ERROR = "all symbols must be unique";
    private static final String STARTING_MESSAGE = "Welcome to CodeFight 2024. Enter 'help' for more details.";
    private static final int MINIMUM_ARGUMENT_AMOUNT = 8;
    private static final int GAME_SYMBOLS_STARTING_INDEX = 1;
    private static final int GAME_SYMBOLS_FINAL_INDEX = 4;
    private static final int AI_SYMBOLS_STARTING_INDEX = 5;
    private static final int MEMORY_SIZE_INDEX = 0;
    private static final int MINIMUM_MEMORY_SIZE = 7;
    private static final int MAXIMUM_MEMORY_SIZE = 1337;



    private CodeFight() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * entry point for the program that parses starting arguments and begins user interaction.
     * @param args the starting arguments specified by the user
     */
    public static void main(String[] args) {
        boolean validGame = true;
        InputOutputHandler inputOutputHandler = new InputOutputCommandLine();

        GameEngine gameEngine = null;

        try {
            preParsingCheck(args);
            gameEngine = parseArguments(args, (args.length - AI_SYMBOLS_STARTING_INDEX) / 2);

        } catch (ArgumentInvalidException e) {
            inputOutputHandler.displayError(e.getMessage());
            validGame = false;
        }
        if (validGame) {
            inputOutputHandler.displayStandard(STARTING_MESSAGE);
            CommandHandler commandHandler = new CommandHandler(gameEngine, inputOutputHandler);
            commandHandler.handleUserInput();
        }
    }

    private static GameEngine parseArguments(String[] args, int maxNumberAi) throws ArgumentInvalidException {
        int memorySize = parseMemorySize(args[MEMORY_SIZE_INDEX]);

        GameEngine gameEngine = new GameEngine(memorySize, maxNumberAi);

        String[] gameSymbols = Arrays.copyOfRange(args, GAME_SYMBOLS_STARTING_INDEX, GAME_SYMBOLS_FINAL_INDEX + 1);
        List<String> aiSymbols = List.of(Arrays.copyOfRange(args, AI_SYMBOLS_STARTING_INDEX, args.length));
        gameEngine.initializeMemoryDisplay(gameSymbols, aiSymbols);

        return gameEngine;

    }

    private static int parseMemorySize(String memorySizeString) throws ArgumentInvalidException {
        int memorySize;
        try {
            memorySize = Integer.parseInt(memorySizeString);

        } catch (NumberFormatException e) {
            throw new ArgumentInvalidException(MEMORY_SIZE_INVALID_INTEGER_ERROR);
        }

        if (memorySize < MINIMUM_MEMORY_SIZE || memorySize > MAXIMUM_MEMORY_SIZE) {
            throw new ArgumentInvalidException(MEMORY_SIZE_BOUND_ERROR.formatted(MINIMUM_MEMORY_SIZE, MAXIMUM_MEMORY_SIZE));
        }

        return memorySize;

    }

    private static void preParsingCheck(String[] args) throws ArgumentInvalidException {

        //correct amount of arguments is always uneven
        if (args.length < MINIMUM_ARGUMENT_AMOUNT || args.length % 2 == 0) {
            throw new ArgumentInvalidException(INVALID_ARGUMENT_AMOUNT_ERROR);
        }

        String[] symbols = Arrays.copyOfRange(args, GAME_SYMBOLS_STARTING_INDEX, args.length);
        Set<String> symbolSet = new HashSet<>(Arrays.asList(symbols));

        //checking for duplicates
        if (symbols.length != symbolSet.size()) {
            throw new ArgumentInvalidException(SYMBOLS_NOT_UNIQUE_ERROR);
        }

        for (String symbol : symbols) {
            if (symbol.contains(FORBIDDEN_SYMBOL_CHARACTER)) {
                throw new ArgumentInvalidException(FORBIDDEN_SYMBOL_ERROR.formatted(FORBIDDEN_SYMBOL_CHARACTER));
            }
        }

    }
}
