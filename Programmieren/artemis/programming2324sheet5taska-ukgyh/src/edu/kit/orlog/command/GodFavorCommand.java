package edu.kit.orlog.command;

import edu.kit.orlog.exceptions.GameElementCreationException;
import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;
import edu.kit.orlog.model.gameelements.godfavors.GodFavor;

import static edu.kit.orlog.command.CommandResultType.FAILURE;

/**
 * The {@code GodFavorCommand} class represents a command to invoke a god favor
 * during the God Favor phase of the game. This command allows players to
 * choose specific god favors that they can activate in the evaluation phase, subject to the game's rules.
 * @author ukgyh
 */
public class GodFavorCommand implements Command {

    private static final boolean REQUIRES_GAME_PHASE = true;
    /**
     * The game phase the command requires.
     */
    private static final GamePhase GAME_PHASE = GamePhase.GODFAVORPHASE;
    private static final int MAXIMUM_GODFAVOR_LEVEL = 3;
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int LEVEL_POSITION = 1;
    private static final int GODFAVOR_IDENTIFIER_POSITION = 0;
    private static final String VALID_INTEGER_FORMAT = "level value must be an integer";
    private static final String LEVEL_BOUNDS_FORMAT = "level value must be between 1 and %d";
    private static final String GODFAVOR_NOT_POSSESED_FORMAT = "Current player does not possess GodFavor %s ";
    private static final String GODFAVOR_IDENTIFIER_NOT_FOUND_FORMAT = "GodFavor %s does not exist";
    private static final String NEXT_PLAYER_MESSAGE = "OK, %s's turn";
    private static final String EVALUATION_PHASE_MESSAGE = "OK, all players are now ready to evaluate!";
    /**
     * Executes the god favor action in the game. It checks the validity of the god favor
     * and the level provided in the arguments, and applies the god favor if applicable.
     *
     * @param game The game context in which this command is executed.
     * @param arguments The arguments for the command. Expects two arguments:
     *                  the god favor identifier and the level of the god favor.
     * @return A {@code CommandResult} indicating the outcome of the execution,
     *         such as successful application of a god favor or failure due to invalid inputs.
     */
    @Override
    public CommandResult execute(Game game, String[] arguments) {
        int level;

        try {
            level = Integer.parseInt(arguments[LEVEL_POSITION]);
        } catch (NumberFormatException e) {
            return new CommandResult(FAILURE, VALID_INTEGER_FORMAT);
        }

        if (level < 1 || level > MAXIMUM_GODFAVOR_LEVEL) {
            return new CommandResult(FAILURE, LEVEL_BOUNDS_FORMAT);
        }

        String godFavorIdentifier = arguments[GODFAVOR_IDENTIFIER_POSITION];

        if (!game.getGodFavors(game.getCurrentPlayerID()).contains(godFavorIdentifier)) {
            return new CommandResult(FAILURE, GODFAVOR_NOT_POSSESED_FORMAT.formatted(godFavorIdentifier));
        }
        GodFavor godFavor;

        try {
            godFavor = game.createGodFavor(godFavorIdentifier, game, level, game.getCurrentPlayerID(), game.getNextPlayerID());
        } catch (GameElementCreationException e) {
            return new CommandResult(FAILURE, GODFAVOR_IDENTIFIER_NOT_FOUND_FORMAT.formatted(e.getMissingElementIdentifier()));
        }
        game.addToCurrentGodFavors(godFavor);
        game.progressPhase(false);
        if (game.getCurrentGamePhase() == GamePhase.EVALUATIONPHASE) {
            return new CommandResult(CommandResultType.SUCCESS, EVALUATION_PHASE_MESSAGE);
        } else {
            return new CommandResult(CommandResultType.SUCCESS, NEXT_PLAYER_MESSAGE.formatted(game.getCurrentPlayerName()));
        }
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
        return GAME_PHASE;
    }

}
