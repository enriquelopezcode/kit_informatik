package edu.kit.connectgame;

import java.util.Objects;

/**
 * Represents the game board for the Connect Game.
 * This class provides functionality to manage the state of the game board,
 * including initializing the board, adding stones, and checking for wins.
 * The board is represented as a two-dimensional array of strings.
 *
 * @author ukgyh
 */
public class GameBoard {


    private final int rows;
    private final int columns;

    private final String[][] boardState;

    /**
     * Method that constructs a GameBoard with the specified dimensions
     * The GameBoard is initialised as a two-dimensional array with
     * the specified dimensions. It is then filled with empty space Strings.
     *
     * @param rows number of rows the board should have
     * @param columns number of columns the board should have
     */

    GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.boardState = new String[rows][columns];
    }

    /**
     * Method that gets the current GameBoard as a two-dimensional array.
     *
     * @return two-dimensional array of the GameBoard
     */
    public String[][] getBoardState() {
        return boardState;
    }

    /**
     * Method that returns the amount of columns of the GameBoard.
     *
     * @return number of columns
     */
    public int getColumnAmount() {
        return columns;
    }

    /**
     * Method that checks if a cell in the GameBoard is available or full
     *
     * @param cell String int he GameBoard array
     * @return true if the cell is available, false if it is full
     */
    private boolean cellFree(String cell) {
        return cell == null;
    }

    /**
     * Method that returns the first row that is available for a dropped stone.
     * should only be used after it is ensured that there is at least
     * one available cell in the column.
     *
     * @param column column that should be checked for available row
     * @return row number of the first available cell in the column
     */
    private int firstAvailableRow(int column) {
        int firstCell = 0;
        for (int i = rows - 1; i >= 0; i--) {
            if (cellFree(boardState[i][column])) {
                firstCell = i;
                break;
            }
        }
        return firstCell;
    }

    /**
     * Method that checks if there is at least one available cell in a column.
     *
     * @param playerMove the column which should be checked for availability (starting from 1)
     * @return true if available, false if completely full
     */
    public boolean isColumnAvailable(int playerMove) {
        int columnIndex = playerMove - 1;
        // only need to check if top cell is available
        return cellFree(boardState[0][columnIndex]);
    }

    /**
     * Method tht checks if the entire GameBoard is occupied with stones.
     *
     * @return true if entire GameBoard is full, false if not full
     */
    public boolean isFull() {
        for (String cell : boardState[0]) {
            if (cellFree(cell)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that checks for a win in the horizontal direction
     *
     * @param winningAmount Number of stones in a line needed to win
     * @param playerSymbol the playerSymbol for which to check the win
     * @return true if player has a winning streak in the rows, false if not
     */
    private boolean checkHorizontal(int winningAmount, String playerSymbol) {
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i <= columns - winningAmount; i++) {
                boolean winFound = true;

                // check each possible horizontal sequence for winning streak
                for (int k = i; k < i + winningAmount; k++) {
                    if (!(Objects.equals(boardState[j][k], playerSymbol))) {
                        winFound = false;
                        break;
                    }
                }
                if (winFound) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that checks for a win in the vertical direction
     *
     * @param winningAmount Number of stones in a line needed to win
     * @param playerSymbol the playerSymbol for which to check the win
     * @return true if player has a winning streak in the columns, false if not
     */
    private boolean checkVertical(int winningAmount, String playerSymbol) {
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j <= rows - winningAmount; j++) {
                boolean winFound = true;

                // check each possible vertical sequence for winning streak
                for (int k = j; k < j + winningAmount; k++) {
                    if (!Objects.equals(boardState[k][i], playerSymbol)) {
                        winFound = false;
                        break;
                    }
                }
                if (winFound) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that checks for a win in the diagonals
     * both diagonals are checked (upper-right to bottom-left, bottom-right to upper-left)
     *
     * @param winningAmount Number of stones in a line needed to win
     * @param playerSymbol the playerSymbol for which to check the win
     * @return true if player has a winning streak in the diagonals, false if not
     */

    private boolean checkDiagonals(int winningAmount, String playerSymbol) {
        // diagonal check (top-left to bottom-right)
        for (int j = 0; j <= rows - winningAmount; j++) {
            for (int i = 0; i <= columns - winningAmount; i++) {
                boolean winFound = true;

                // check each possible downward diagonal sequence for winning streak
                for (int k = 0; k < winningAmount; k++) {
                    if (!Objects.equals(boardState[j + k][i + k], playerSymbol)) {
                        winFound = false;
                        break;
                    }
                }
                if (winFound) {
                    return true;
                }
            }
        }
        // diagonal check (top-right to bottom-left)
        for (int j = 0; j <= rows - winningAmount; j++) {
            for (int i = winningAmount - 1; i < columns; i++) {
                boolean winFound = true;

                // check each possible upward diagonal sequence for winning streak
                for (int k = 0; k < winningAmount; k++) {
                    if (!Objects.equals(boardState[j + k][i - k], playerSymbol)) {
                        winFound = false;
                        break;
                    }
                }
                if (winFound) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Method that checks if a player has won the Game.
     *
     * @param winningAmount Number of stones in a line needed to win
     * @param playerSymbol the playerSymbol for which to check the win
     * @return true if player has won, false if not
     */
    public boolean hasWon(int winningAmount, String playerSymbol) {
        boolean horizontalWin = checkHorizontal(winningAmount, playerSymbol);
        boolean verticalWin = checkVertical(winningAmount, playerSymbol);
        boolean diagonalWin = checkDiagonals(winningAmount, playerSymbol);

        return horizontalWin || verticalWin || diagonalWin;
    }

    /**
     * Method that drops a stone into a column and changes the GameBoard accordingly.
     * Should only be used once the availability of the column has been verified
     *
     * @param playerMove the column number in which the stone should be dropped
     * @param playerSymbol the symbol of the player whose move it is
     */
    public void dropStone(int playerMove, String playerSymbol) {
        int column = playerMove - 1;
        boardState[firstAvailableRow(column)][column] = playerSymbol;
    }
}
