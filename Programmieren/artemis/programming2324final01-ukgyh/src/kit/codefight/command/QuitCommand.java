package kit.codefight.command;

import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * This command quits a {@link CommandHandler command handler}.
 *
 * @author Programmieren-Team
 * @author ukgyh
 */
final class QuitCommand implements Command {

    private static final boolean REQUIRES_GAME_PHASE = false;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.NONE;
    private static final int ARGUMENT_AMOUNT = 0;
    private static final String INFO_TEXT = "quits the application. No arguments are required.";
    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     * @param commandHandler the command handler to be quit
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(GameEngine ignored, String[] commandArguments) {
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
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
