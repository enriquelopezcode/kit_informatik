package kit.codefight.command;

import kit.codefight.exceptions.StartingGameException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * command that starts the game with the current settings
 * @author ukgyh
 */

final class StartGameCommand implements Command {

    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.INITIALIZATION;
    private static final String INFO_TEXT = "starts the game with the current settings. Format is start-game [ai1] [ai2] ...";
    private static final String SUCCESS_MESSAGE = "Game started.";
    private static final int MIN_ARGUMENT_AMOUNT = 2;
    private final int maxNumberAi;

    StartGameCommand(int maxNumberAi) {
        this.maxNumberAi = maxNumberAi;
    }

    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        try {
            gameEngine.startGame(commandArguments);
        } catch (StartingGameException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        return new CommandResult(CommandResultType.SUCCESS, SUCCESS_MESSAGE);
    }


    @Override
    public boolean isValidArgumentAmount(int argumentAmount) {
        return argumentAmount >= MIN_ARGUMENT_AMOUNT && argumentAmount <= maxNumberAi;
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
