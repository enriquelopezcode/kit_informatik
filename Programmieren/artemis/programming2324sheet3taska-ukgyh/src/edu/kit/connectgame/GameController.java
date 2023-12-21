package edu.kit.connectgame;

/**
 * Class that handles the Initialisation and progression of the Connect Game.
 * Handles player turns, validates players Moves and handles game logic and the end of a Game.
 * Current maximum amount of players is 6 and standard dimensions of GameBoard are 6 rows and 7 columns.
 * It adjusts the Amount of stones needed for a winning streak according to the amount of players.
 *
 * @author ukgyh
 */

public class GameController {

    /**
     * The maximum number of players allowed in the game.
     */
    public static final int MAX_NUMBER_PLAYERS = 6;

    /**
     * The maximum amount of stones needed for a win for any amount of players
     */
    private static final int MAX_WINNING_STONE_AMOUNT = 4;

    /**
     * Number used to calculate Streak amount needed depending on the player count
     */
    private static final double WIN_CONDITION_DIVIDEND = 9.0;

    private static final int STANDARD_ROWS = 6;
    private static final int STANDARD_COLUMNS = 7;

    private final int numberPlayers;
    private final int winningAmount;
    private final String[] playerSymbols;

    private final GameBoard gameBoard;

    private int moveNumber = 1;

    private int playerTurn = 1;

    /**
     * Constructs the GameController class
     * Assigns values or number of Players and the winning streak amount and initializes new GameBoard
     *
     * @param playerSymbols String with the Symbol string of every participating player
     */
    GameController(String[] playerSymbols) {
        this.numberPlayers = playerSymbols.length;
        this.winningAmount = Math.min(MAX_WINNING_STONE_AMOUNT, (int) Math.ceil(WIN_CONDITION_DIVIDEND / numberPlayers));
        this.playerSymbols = playerSymbols;
        this.gameBoard = new GameBoard(STANDARD_ROWS, STANDARD_COLUMNS);
    }

    /**
     * Checks if the player's inputted move is a valid column number.
     * Checks for a valid Integer first and then checks if the Integer is a valid column number
     *
     * @param playerMove the move the player inputted into the command line
     * @return true if valid column number, false if not
     */

    private boolean validateMove(String playerMove) {
        try {
            Integer.parseInt(playerMove);
        } catch (NumberFormatException e) {
            return false;
        }

        if ((Integer.parseInt(playerMove) > gameBoard.getColumnAmount()) || (Integer.parseInt(playerMove) < 1)) {
            return false;
        }

        return gameBoard.isColumnAvailable(Integer.parseInt(playerMove));
    }

    /**
     * Method that starts a Game and handles the entire game flow until it concludes.
     * prompts the players to input their moves and updates the state of the board and the move and player number
     * Ends the game if a player has won or if it is a draw
     */
    public void startGame() {
        InputOutput.printBoard(gameBoard.getBoardState());
        InputOutput.printMove(1, 1);
        String playerMove = InputOutput.readMove();

        while (!(playerMove.equals("quit"))) {

            if (!validateMove(playerMove)) {
                InputOutput.printError("Column not available");
                InputOutput.printMove(moveNumber, playerTurn);
                playerMove = InputOutput.readMove();
                continue;
            }

            gameBoard.dropStone(Integer.parseInt(playerMove), playerSymbols[playerTurn - 1]);

            if (gameBoard.hasWon(winningAmount, playerSymbols[playerTurn - 1])) {
                InputOutput.printBoard(gameBoard.getBoardState());
                InputOutput.printWin(playerTurn);
                InputOutput.closeScanner();
                break;

            } else if (gameBoard.isFull()) {
                InputOutput.printBoard(gameBoard.getBoardState());
                InputOutput.printDraw();
                InputOutput.closeScanner();
                break;
            }

            if (playerTurn == numberPlayers) {
                playerTurn = 1;

            } else {
                playerTurn++;
            }
            moveNumber++;

            InputOutput.printBoard(gameBoard.getBoardState());
            InputOutput.printMove(moveNumber, playerTurn);
            playerMove = InputOutput.readMove();

        }
        
    InputOutput.closeScanner();

    }


}
