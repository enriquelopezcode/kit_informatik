package edu.kit.connectgame;

import java.util.Arrays;
import java.util.Collections;

/**
 * The {@code Main} class serves as the entry point for the Connect Game application.
 *
 * @author ukgyh
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */

    private Main() {

    }

    /**
     * Main method that initialises the Connect Game.
     * It reads in a String array of player symbols from the command line using the
     * InputOutput Utility Class and validates the String array. If the String array adheres to the
     * specification the method initialises the GameController class,
     * otherwise an error is thrown and the program terminates.
     *
     * @param args the command-line arguments containing the players symbols.
     *      Each symbol should be a unique, single character. The number of symbols should not exceed 6.
     */

    public static void main(String[] args) {
        String errorMessage;

        if (args.length > GameController.MAX_NUMBER_PLAYERS) {
            errorMessage = String.format("exceeded maximum player count of %d", GameController.MAX_NUMBER_PLAYERS);
            InputOutput.printError(errorMessage);
            return;

        }

        for (String symbol : args) {
            if (symbol.length() > 1) {
                errorMessage = "symbols can only be one character";
                InputOutput.printError(errorMessage);
                return;
            }

            //checks if any symbol appears more than once in player symbols
            if (Collections.frequency(Arrays.asList(args), symbol) > 1) {
                errorMessage = "identical player symbols are not allowed";
                InputOutput.printError(errorMessage);
                return;
            }
        }

        // default two player game
        if (args.length == 0) {
            String[] playerSymbols = {"x", "o"};
            GameController game = new GameController(playerSymbols);
            game.startGame();

        } else {
            GameController game = new GameController(args);
            game.startGame();

        }
    }
}
