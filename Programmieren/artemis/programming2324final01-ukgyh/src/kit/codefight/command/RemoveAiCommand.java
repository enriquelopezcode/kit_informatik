package kit.codefight.command;

import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * command that removes a registered AI from the game
 * @author ukgyh
 */
final class RemoveAiCommand implements Command {
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.INITIALIZATION;
    private static final int ARGUMENT_AMOUNT = 1;
    private static final int AI_NAME_INDEX = 0;
    private static final String INFO_TEXT = "removes AI with the specified name from the game. Format is remove-ai [name]";

    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        String aiName = commandArguments[AI_NAME_INDEX];
        try {
            gameEngine.removeAi(aiName);
        } catch (MissingAiException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }
        return new CommandResult(CommandResultType.SUCCESS, aiName);
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
