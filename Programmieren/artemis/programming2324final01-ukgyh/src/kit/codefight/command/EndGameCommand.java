package kit.codefight.command;

import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * This command ends a game and displays the game info.
 * @author ukgyh
 */
final class EndGameCommand  implements Command {
    private static final int ARGUMENT_AMOUNT = 0;
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.RUNNING;
    private static final String INFO_TEXT = "ends the game and displays information about the AIs. No arguments are required.";


    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        String gameInfo = gameEngine.getGameInfo();
        gameEngine.endGame();
        return new CommandResult(CommandResultType.SUCCESS, gameInfo);
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
