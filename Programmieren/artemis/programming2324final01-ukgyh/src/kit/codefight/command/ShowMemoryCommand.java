package kit.codefight.command;

import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

import java.util.Set;

/**
 * command class that displays the current state of the memory in the game.
 * @author ukgyh
 */

final class ShowMemoryCommand implements Command {
    private static final Set<Integer> ARGUMENT_AMOUNT = Set.of(0, 1);
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.RUNNING;
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final int NO_SEGMENT_ARGUMENT_LENGTH = 0;
    private static final int WITH_SEGMENT_ARGUMENT_LENGTH = 1;
    private static final int START_OF_SEGMENT_INDEX = 0;
    private static final String INFO_TEXT = "displays the current state of the memory. "
            + "Optional arguments: the start of a segment where additional instruction information is displayed."
            + " Format is show-memory [start of segment]";
    private static final String INVALID_ARGUMENT_AMOUNT_ERROR = "Invalid amount of arguments";
    private static final String INVALID_SEGMENT_START_ERROR = "Segment start must be a valid integer";


    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        switch (commandArguments.length) {
            case NO_SEGMENT_ARGUMENT_LENGTH:
                String memoryDisplay;
                try {
                    memoryDisplay = gameEngine.getMemoryDisplay();
                } catch (GameExecutionException e) {
                    return new CommandResult(CommandResultType.FAILURE, e.getMessage());
                }

                return new CommandResult(CommandResultType.SUCCESS, memoryDisplay);

            case WITH_SEGMENT_ARGUMENT_LENGTH:
                int startOfSegment;
                try {
                    startOfSegment = Integer.parseInt(commandArguments[START_OF_SEGMENT_INDEX]);
                } catch (NumberFormatException e) {
                    return new CommandResult(CommandResultType.FAILURE, INVALID_SEGMENT_START_ERROR);
                }

                String memoryDisplaySegment;
                try {
                    memoryDisplaySegment = gameEngine.getMemoryDisplay(startOfSegment);
                } catch (GameExecutionException | MemoryOutOfBoundsException e) {
                    return new CommandResult(CommandResultType.FAILURE, e.getMessage());
                }

                return new CommandResult(CommandResultType.SUCCESS, memoryDisplaySegment);

            default:
                return new CommandResult(CommandResultType.FAILURE, INVALID_ARGUMENT_AMOUNT_ERROR);
        }
    }

    @Override
    public boolean isValidArgumentAmount(int argumentAmount) {
        return ARGUMENT_AMOUNT.contains(argumentAmount);
    }


    @Override
    public boolean requiresGamePhase() {
        return REQUIRES_GAME_PHASE;
    }


    @Override
    public GamePhase getRequiredGamePhase() {
        return REQUIRED_GAME_PHASE;
    }

    @Override
    public String getInfoText() {
        return INFO_TEXT;
    }
}
