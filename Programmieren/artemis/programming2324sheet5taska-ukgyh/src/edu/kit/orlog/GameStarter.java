package edu.kit.orlog;

import edu.kit.orlog.command.CommandHandler;
import edu.kit.orlog.command.StartingArgumentHandler;
import edu.kit.orlog.exceptions.StartingArgumentInvalidException;
import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.Player;

import java.util.List;

/**
 * This class is the entry point of the program.
 *
 * @author ukgyh
 */
public final class GameStarter {
    private static final String ERROR_PREFIX = "Error, ";
    private static final String INSTANTIATION_ERROR = "utility class cannot be instantiated";


    private GameStarter() {
        throw new UnsupportedOperationException(INSTANTIATION_ERROR);
    }

    /**
     * Starts the program and initializes the game with specified starting conditions.
     * @param args the starting conditions
     */
    public static void main(String[] args) {
        List<Player> players;
        try {
            players = StartingArgumentHandler.parseStartingArguments(args);
        } catch (StartingArgumentInvalidException e) {
            System.err.println(ERROR_PREFIX + e.getMessage());
            return;
        }
        Game game = new Game(players.get(0), players.get(1));
        CommandHandler commandHandler = new CommandHandler(game);
        commandHandler.handleUserInput();

    }
}

