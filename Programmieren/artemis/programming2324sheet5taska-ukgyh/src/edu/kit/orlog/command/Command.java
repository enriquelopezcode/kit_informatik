package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;

/**
 * The {@code Command} interface defines the structure of command objects
 * used in a game. Each command encapsulates the necessary logic to execute
 * a specific action or operation within the game context.
 * @author ukgyh
 */
public interface Command {
    /**
     * Executes the command within the context of the given game.
     *
     * @param game The game context in which the command is executed. This
     *             object provides access to the game's state and functionality.
     * @param arguments An array of strings representing the arguments
     *                  passed to the command. These arguments provide
     *                  additional information or parameters required for
     *                  command execution.
     * @return A {@code CommandResult} object representing the outcome of the
     *         command execution. This can include success or failure status,
     *         along with any relevant messages or data.
     */
    CommandResult execute(Game game, String[] arguments);

    /**
     * Retrieves the number of arguments expected by this command.
     *
     * @return An integer representing the number of arguments this command
     *         expects.
     */
    int getNumberOfArguments();

    /**
     * Determines whether the execution of this command requires the game
     * to be in a specific phase.
     *
     * @return {@code true} if the command requires the game to be in a
     *         specific phase for execution; {@code false} otherwise.
     */
    boolean requiresGamePhase();

    /**
     * Gets the game phase during which this command can be executed.
     * This method is typically called if {@code requiresGamePhase()} returns
     * {@code true}.
     *
     * @return The {@code GamePhase} during which this command is valid, or
     *         null if the command does not require a specific game phase.
     */
    GamePhase getGamePhase();
}
