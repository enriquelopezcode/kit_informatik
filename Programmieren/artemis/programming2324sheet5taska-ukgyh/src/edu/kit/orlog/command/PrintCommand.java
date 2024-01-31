package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;

import static edu.kit.orlog.command.CommandResultType.SUCCESS;

/**
 * provides functionality to print the current game state.
 *
 * @author ukgyh
 *
 */
public final class PrintCommand implements Command {
    private static final boolean REQUIRES_GAME_PHASE = false;
    private static final int NUMBER_OF_ARGUMENTS = 0;

    /**
     * Executes the print command. This method prints the current state of the game.
     * It checks if the correct number of arguments are provided and returns a success
     * or failure command result accordingly.
     *
     * @param game The instance of the game on which the command is to be executed.
     * @param arguments An array of strings representing the arguments for the command.
     *                  The print command does not require any arguments.
     * @return A {@code CommandResult} instance representing the result of the command execution.
     *         It returns a success result with the game state if no arguments are provided,
     *         otherwise, it returns a failure result with an error message.
     */
    public CommandResult execute(Game game, String[] arguments) {
        return new CommandResult(SUCCESS, game.toString());
    }



    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }



    @Override
    public boolean requiresGamePhase() {
        return REQUIRES_GAME_PHASE;
    }


    @Override
    public GamePhase getGamePhase() {
        return null;
    }
}
