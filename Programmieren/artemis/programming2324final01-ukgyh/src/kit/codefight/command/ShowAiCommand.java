package kit.codefight.command;

import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * command that displays the status of an AI in the game.
 * @author ukgyh
 */
final class ShowAiCommand implements Command {
    private static final int ARGUMENT_AMOUNT = 1;
    private static final int AI_NAME_INDEX = 0;
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.RUNNING;
    private static final String INFO_TEXT = "shows the status of an AI. Format is show-ai [name]";

    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        String aiName = commandArguments[AI_NAME_INDEX];

        String aiDisplay;
        try {
            aiDisplay = gameEngine.getAiDisplay(aiName);
        } catch (MissingAiException | GameExecutionException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }
        return new CommandResult(CommandResultType.SUCCESS, aiDisplay);
    }
    @Override
    public boolean isValidArgumentAmount(int argumentAmount) {
        return argumentAmount == ARGUMENT_AMOUNT;
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
