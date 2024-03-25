package kit.organiser.command;

import kit.organiser.model.FileManager;

/**
 * This command quits a {@link CommandHandler command handler}.
 *
 * @author Programmieren-Team
 * @author ukgyh
 */
final class QuitCommand implements Command {
    private static final int ARGUMENT_AMOUNT = 0;
    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     * @param commandHandler the command handler to be quit
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(FileManager ignored, String[] commandArguments) {
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
    }


    @Override
    public int getNumberOfArguments() {
        return ARGUMENT_AMOUNT;
    }
}
