package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;

/**
 * The {@code TurnCommand} class represents a command used to pass the turn to the
 * next player during the God Favor phase of the game. This command is typically
 * used when a player opts not to choose a god favor.
 * @author ukgyh
 */
public class TurnCommand implements Command {
    private static final String NEXT_PLAYER_MESSAGE = "OK, %s's turn";
    private static final String EVALUATION_READY_MESSAGE = "OK, all players are now ready to evaluate!";

    private static final int NUMBER_OF_ARGUMENTS = 0;

    private static final boolean REQUIRES_GAME_PHASE = true;

    private static final GamePhase GAME_PHASE = GamePhase.GODFAVORPHASE;

    /**
     * Executes the turn passing action in the game. It progresses the game phase
     * and determines whether to proceed to the evaluation phase or to continue
     * with the next player's turn.
     *
     * @param game The game context in which this command is executed.
     * @param arguments The arguments for the command. This command expects no arguments.
     * @return A {@code CommandResult} indicating the outcome of the execution.
     *
     */
    @Override
    public CommandResult execute(Game game, String[] arguments) {
        game.progressPhase(false);
        if (game.getCurrentGamePhase() == GamePhase.EVALUATIONPHASE) {
            return new CommandResult(CommandResultType.SUCCESS, EVALUATION_READY_MESSAGE);
        } else {
            return new CommandResult(CommandResultType.SUCCESS, NEXT_PLAYER_MESSAGE.formatted(game.getCurrentPlayerName()));
        }
    }

    @Override
    public boolean requiresGamePhase() {
        return REQUIRES_GAME_PHASE;
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    @Override
    public GamePhase getGamePhase() {
        return GAME_PHASE;
    }

}
