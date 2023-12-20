package edu.kit.connectgame;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Utility class for handling input and output operations of Connect Game.
 *
 * @author ukgyh
 */
public final class InputOutput {
    private static final String ERROR_TEMPLATE = "ERROR: ";
    private static final String CELL_SEPARATOR = "|";
    private static final Scanner MOVE_READER = new Scanner(System.in);


    /**
     * private constructor to avoid instantiation
     */
    private InputOutput() {

    }

    /**
     * Method that prints out error messages to console.
     * All error messages start with the String: "ERROR: " followed by
     * the error message String parameter
     *
     * @param errorMessage error message to be printed
     */
    public static void printError(String errorMessage) {
        System.out.println(ERROR_TEMPLATE + errorMessage);
    }

    /**
     * Method that prints out a connect board to the console.
     *
     * @param board the board to be printed (two-dimensional String array)
     *              cells of the board in the printed String are seperated by the String: "|"
     */
    public static void printBoard(String[][] board) {

        StringJoiner rowJoiner = new StringJoiner(System.lineSeparator(), "", System.lineSeparator());
        for (String[] row : board) {

            StringJoiner columnJoiner = new StringJoiner(CELL_SEPARATOR, CELL_SEPARATOR, CELL_SEPARATOR);

            for (String cell : row) {
                columnJoiner.add(Objects.requireNonNullElse(cell, " "));
            }

            rowJoiner.add(columnJoiner.toString());
        }

        System.out.print(rowJoiner);

    }

    /**
     * method that prints the Move and Player Number to the console.
     *
     * @param moveNumber the current move number of the Connect Game
     * @param player Player Number whose turn it is
     */
    public static void printMove(int moveNumber, int player) {
        System.out.println("%d. Zug, Spieler %d:".formatted(moveNumber, player));
    }

    /**
     * Method that reads in the next move through the command line.
     *
     * @return string of the player's next move
     */
    public static String readMove() {
        return MOVE_READER.nextLine();
    }

    /**
     * Prints winning message for the given player to the console.
     *
     * @param player number of player that won the game
     */
    public static void printWin(int player) {
        System.out.println("Sieger: Spieler %d".formatted(player));
    }

    /**
     * Print message if game is a draw.
     */
    public static void printDraw() {
        System.out.println("Unentschieden!");
    }

    /**
     * Method that closes the scanner.
     */
    public static void closeScanner() {
        MOVE_READER.close();
    }

}
