package kit.codefight.command;

import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

import java.util.List;
import java.util.Set;

/**
 * command class that executes the steps of AIs in the game.
 * @author ukgyh
 */
final class NextCommand implements Command {
    private static final Set<Integer> ARGUMENT_AMOUNT = Set.of(0, 1);
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.RUNNING;
    private static final int STEPS_INDEX = 0;
    private static final int DEFAULT_STEPS_AMOUNT = 1;
    private static final int AI_NAME_INDEX = 0;
    private static final int AI_STEPS_INDEX = 1;
    private static final int WITHOUT_STEPS_ARGUMENT_AMOUNT = 0;
    private static final int WITH_STEPS_ARGUMENT_AMOUNT = 1;
    private static final String INFO_TEXT = "executes the next steps of the game. Optional argument: amount of steps to execute."
            + " Format is next [amount of steps]";
    private static final String STEPS_INVALID_ERROR = "step amount must be a valid integer";
    private static final String INVALID_ARGUMENT_AMOUNT_ERROR = "Invalid amount of arguments";
    private static final String STOPPED_AI_FORMAT = "%s executed %s steps until stopping.";



    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        List<String[]> stoppedAiInfo;
        switch (commandArguments.length) {
            case WITHOUT_STEPS_ARGUMENT_AMOUNT:
                try {
                    stoppedAiInfo = gameEngine.doNextSteps(DEFAULT_STEPS_AMOUNT);
                } catch (GameExecutionException e) {
                    return new CommandResult(CommandResultType.FAILURE, e.getMessage());
                }
                break;
            case WITH_STEPS_ARGUMENT_AMOUNT:
                int steps;
                try {
                    steps = Integer.parseInt(commandArguments[STEPS_INDEX]);
                } catch (NumberFormatException e) {
                    return new CommandResult(CommandResultType.FAILURE, STEPS_INVALID_ERROR);
                }
                try {
                    stoppedAiInfo = gameEngine.doNextSteps(steps);
                } catch (GameExecutionException e) {
                    return new CommandResult(CommandResultType.FAILURE, e.getMessage());
                }
                break;

            default:
                return new CommandResult(CommandResultType.FAILURE, INVALID_ARGUMENT_AMOUNT_ERROR);
        }

        if (!stoppedAiInfo.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String[] aiInfo : stoppedAiInfo) {
                stringBuilder.append(STOPPED_AI_FORMAT.formatted(aiInfo[AI_NAME_INDEX], aiInfo[AI_STEPS_INDEX]));
                if (stoppedAiInfo.indexOf(aiInfo) != stoppedAiInfo.size() - 1) {
                    stringBuilder.append(System.lineSeparator());
                }
            }
            return new CommandResult(CommandResultType.SUCCESS, stringBuilder.toString());
        }
        return new CommandResult(CommandResultType.SUCCESS, null);

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
