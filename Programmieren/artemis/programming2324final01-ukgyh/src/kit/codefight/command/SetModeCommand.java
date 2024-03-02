package kit.codefight.command;

import kit.codefight.exceptions.InitializationModeChangeException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

import java.util.Set;
import java.util.StringJoiner;

/**
 * command class that changes the initialization mode of the memory in the game.
 * @author ukgyh
 */
final class SetModeCommand implements Command {
    private static final Set<Integer> ARGUMENT_AMOUNT = Set.of(1, 2);
    private static final int WITHOUT_SEED_ARGUMENT_AMOUNT = 1;
    private static final int WITH_SEED_ARGUMENT_AMOUNT = 2;
    private static final int NAME_INDEX = 0;
    private static final int SEED_INDEX = 1;
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.INITIALIZATION;
    private static final String INFO_TEXT = "changes the initialization mode of the memory. "
            + "Available: RANDOM_INIT_MODE (seed required), STOP_INIT_MODE (no seed allowed). Format is: set-mode [mode] [seed]";
    private static final String INVALID_ARGUMENTS_ERROR = "number of arguments invalid";
    private static final String SEED_INVALID_ERROR = "seed must be a valid integer";
    private static final String NO_SEED_SUCCESS_MESSAGE = "Changed init mode from %s to %s";
    private static final String SEED_SUCCESS_MESSAGE = "Changed init mode from %s to %s %d";
    private static final String SEPARATOR_SYMBOL = " ";


    @Override
    public CommandResult execute(GameEngine gameEngine, String[] commandArguments) {
        String newModeName = commandArguments[NAME_INDEX];
        String[] oldModeInfo = gameEngine.getCurrentModeInfo();

        StringJoiner stringJoiner = new StringJoiner(SEPARATOR_SYMBOL);

        for (String s : oldModeInfo) {
            stringJoiner.add(s);
        }

        switch (commandArguments.length) {
            case WITHOUT_SEED_ARGUMENT_AMOUNT:
                return setModeWithoutSeed(gameEngine, newModeName, oldModeInfo, stringJoiner);

            case WITH_SEED_ARGUMENT_AMOUNT:
                int seed;
                try {
                    seed = Integer.parseInt(commandArguments[SEED_INDEX]);
                } catch (NumberFormatException e) {
                    return new CommandResult(CommandResultType.FAILURE, SEED_INVALID_ERROR);
                }
                return setModeWithSeed(gameEngine, newModeName, oldModeInfo, seed, stringJoiner);

            default:
                return new CommandResult(CommandResultType.FAILURE, INVALID_ARGUMENTS_ERROR);
        }
    }

    private CommandResult setModeWithoutSeed(GameEngine gameEngine, String newModeName, String[] oldModeInfo, StringJoiner joiner) {
        if (oldModeInfo[NAME_INDEX].equals(newModeName) && oldModeInfo.length == WITHOUT_SEED_ARGUMENT_AMOUNT) {
            return new CommandResult(CommandResultType.SUCCESS, null);
        }

        try {
            gameEngine.setInitializationMode(newModeName, null);
        } catch (InitializationModeChangeException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }
        String successMessageWithoutSeed = NO_SEED_SUCCESS_MESSAGE.formatted(joiner.toString(), newModeName);
        return new CommandResult(CommandResultType.SUCCESS, successMessageWithoutSeed);
    }

    private CommandResult setModeWithSeed(GameEngine gameEngine, String newModeName, String[] oldModeInfo, int seed, StringJoiner joiner) {
        if (oldModeInfo[NAME_INDEX].equals(newModeName) && oldModeInfo.length == WITH_SEED_ARGUMENT_AMOUNT) {
            int oldSeed;
            try {
                oldSeed = Integer.parseInt(oldModeInfo[SEED_INDEX]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, SEED_INVALID_ERROR);
            }

            if (oldSeed == seed) {
                return new CommandResult(CommandResultType.SUCCESS, null);
            }
        }

        try {
            gameEngine.setInitializationMode(newModeName, seed);
        } catch (InitializationModeChangeException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }
        String successMessageWithSeed = SEED_SUCCESS_MESSAGE.formatted(joiner.toString(), newModeName, seed);
        return new CommandResult(CommandResultType.SUCCESS, successMessageWithSeed);
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
