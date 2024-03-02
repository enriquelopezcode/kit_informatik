package kit.codefight.command;

import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

import java.util.Comparator;
import java.util.List;

/**
 * command class that displays information about available commands to the user when executed
 * @author ukgyh
 */
final class HelpCommand implements Command {
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int COMMAND_INFO_INDEX = 1;
    private static final int ARGUMENT_AMOUNT = 0;
    private static final String SEPARATION_SYMBOL = ": ";
    private static final String INFO_TEXT = "provides helpful information about available commands. No arguments are required.";
    private static final boolean REQUIRES_GAME_PHASE = false;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.NONE;
    private final CommandHandler commandHandler;


    HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {

        StringBuilder infoText = new StringBuilder();
        List<String[]> availableCommandInfo = commandHandler.getAvailableCommandInfo();

        //sort alphabetically by command name
        availableCommandInfo.sort(Comparator.comparing(o -> o[COMMAND_NAME_INDEX]));

        for (int i = 0; i < availableCommandInfo.size(); i++) {
            String[] commandInfo = availableCommandInfo.get(i);
            infoText.append(commandInfo[COMMAND_NAME_INDEX])
                    .append(SEPARATION_SYMBOL)
                    .append(commandInfo[COMMAND_INFO_INDEX]);

            if (i < availableCommandInfo.size() - 1) {
                infoText.append(System.lineSeparator());
            }
        }

        return new CommandResult(CommandResultType.SUCCESS, infoText.toString());

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
