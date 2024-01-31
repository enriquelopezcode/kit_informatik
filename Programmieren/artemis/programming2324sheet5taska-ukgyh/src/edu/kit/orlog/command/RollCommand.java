package edu.kit.orlog.command;

import edu.kit.orlog.exceptions.GameElementCreationException;
import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;
import edu.kit.orlog.model.gameelements.fightingelements.FightingElement;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.orlog.command.CommandResultType.FAILURE;
import static edu.kit.orlog.command.CommandResultType.SUCCESS;

/**
 * The {@code RollCommand} class represents a command to perform a roll action
 * during the Dice phase of the game. This command is responsible for handling
 * generating fighting elements based on the provided arguments.
 * @author ukgyh
 */
public class RollCommand implements Command {
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase GAME_PHASE = GamePhase.DICEPHASE;
    private static final int NUMBER_OF_ARGUMENTS = 6;
    private static final String MISSING_ELEMENT_MESSAGE = "FightingElement %s does not exist";
    private static final String NEXT_PLAYER_MESSAGE = "OK, %s's turn";

    /**
     * Executes the roll action in the game. It creates fighting elements based on
     * the provided arguments and adds them to the game's current fighting elements.
     *
     * @param game The game context in which this command is executed.
     * @param arguments The arguments for the command, representing identifiers for
     *                  the fighting elements to be created during the roll.
     * @return A {@code CommandResult} indicating the outcome of the execution,
     *         such as successful creation of fighting elements or failure due to invalid inputs.
     */
    @Override
    public CommandResult execute(Game game, String[] arguments) {
        List<FightingElement> fightingElements = new ArrayList<>();
        int currentID = game.getCurrentPlayerID();
        int nextID = game.getNextPlayerID();
        try {
            for (String elementIdentifier : arguments) {
                FightingElement fightingElement = game.createFightingElement(elementIdentifier, game, currentID, nextID);
                fightingElements.add(fightingElement);
            }
        } catch (GameElementCreationException e) {
            return new CommandResult(FAILURE, MISSING_ELEMENT_MESSAGE.formatted(e.getMissingElementIdentifier()));
        }
        game.addToCurrentFightingElements(fightingElements);
        game.progressPhase(false);
        return new CommandResult(SUCCESS, NEXT_PLAYER_MESSAGE.formatted(game.getCurrentPlayerName()));

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
