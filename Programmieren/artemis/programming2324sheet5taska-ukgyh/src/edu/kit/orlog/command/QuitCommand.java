package edu.kit.orlog.command;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.GamePhase;

/**
 * This command quits a {@link CommandHandler command handler}.
 * @author ukgyh
 */
public final class QuitCommand implements Command {
    private static final boolean REQUIRES_GAME_PHASE = false;
    private static final int NUMBER_OF_ARGUMENTS = 0;

    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     *
     * @param commandHandler the command handler to be quit
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(Game ignored, String[] commandArguments) {
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
    }

    /**
     * Gets the number of arguments required for the print command.
     *
     * @return An integer value representing the number of arguments required.
     */
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
        return null;
    }
}
