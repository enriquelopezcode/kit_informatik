package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;

/**
 * The {@code EvaluateCommand} class represents a command to evaluate the current state
 * of the game. This command is specific to the evaluation phase of the game and
 * performs the necessary actions to progress the game state.
 * @author ukgyh
 */
public class EvaluateCommand implements Command {
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase GAME_PHASE = GamePhase.EVALUATIONPHASE;
    private static final String DRAW_MESSAGE = "draw";
    private static final String WIN_FORMAT = "%s wins!";
    private static final String NEXT_PLAYER_MESSAGE = "OK, %s's turn";

    private static final int NUMBER_OF_ARGUMENTS = 0;


    /**
     * Executes the evaluate action in the game. It performs game state evaluation,
     * progresses the game phase, and determines the outcome of the game (e.g., win, lose, draw).
     *
     * @param game The game context in which this command is executed.
     * @param arguments The arguments for the command. This command expects no arguments.
     * @return A {@code CommandResult} indicating the outcome of the execution,
     *         such as game progression, player win, or draw.
     */
    public CommandResult execute(Game game, String[] arguments) {
        game.evaluate();

        //prints out the player stats
        System.out.println(game);

        int currentPlayerHealth = game.getHealth(game.getCurrentPlayerID());
        int nextPlayerHealth = game.getHealth(game.getNextPlayerID());


        if (currentPlayerHealth <= 0 && nextPlayerHealth <= 0) {
            game.progressPhase(true);
            return new CommandResult(CommandResultType.SUCCESS, DRAW_MESSAGE);

        } else if (currentPlayerHealth <= 0) {
            game.progressPhase(true);
            return new CommandResult(CommandResultType.SUCCESS, WIN_FORMAT.formatted(game.getNextPlayerName()));

        } else if (nextPlayerHealth <= 0) {
            game.progressPhase(true);
            return new CommandResult(CommandResultType.SUCCESS, WIN_FORMAT.formatted(game.getCurrentPlayerName()));

        }
        game.progressPhase(false);
        return new CommandResult(CommandResultType.SUCCESS, NEXT_PLAYER_MESSAGE.formatted(game.getCurrentPlayerName()));
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
