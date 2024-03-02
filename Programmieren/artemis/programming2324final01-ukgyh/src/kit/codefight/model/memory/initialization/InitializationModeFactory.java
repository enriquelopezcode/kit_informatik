package kit.codefight.model.memory.initialization;

import kit.codefight.exceptions.InitializationModeChangeException;
import kit.codefight.model.instructions.InstructionFactory;
/**
 * This class is responsible for creating the initialization mode of the memory.
 * @author ukgyh
 */
public final class InitializationModeFactory {
    private static final String STOP_INIT_NAME = "INIT_MODE_STOP";
    private static final String RANDOM_INIT_MODE = "INIT_MODE_RANDOM";
    private static final String MODE_NOT_FOUND_EXCEPTION = "Invalid mode name";
    private static final String SEED_NOT_ALLOWED_ERROR = "seed is not allowed for %s";
    private static final String SEED_REQUIRED_EXCEPTION = "seed is required for %s";
    private static final String SEED_OUT_OF_BOUNDS = "seed must be between %d and %d";
    private static final int MINIMUM_SEED = -1337;
    private static final int MAXIMUM_SEED = 1337;
    private final InstructionFactory instructionFactory;

    /**
     * Constructs new InitializationModeFactory.
     * @param instructionFactory the factory for creating instructions
     */
    InitializationModeFactory(InstructionFactory instructionFactory) {
        this.instructionFactory = instructionFactory;
    }

    /**
     * Creates a new initialization mode.
     * @param modeName the name of the mode
     * @param seed the seed for the mode, null if not needed
     * @return the new initialization mode
     * @throws InitializationModeChangeException if the mode change is not possible
     */
    public MemoryInitializationMode createInitializationMode(String modeName, Integer seed) throws InitializationModeChangeException {
        return switch (modeName) {

            case STOP_INIT_NAME -> {
                if (seed != null) {
                    throw new InitializationModeChangeException(SEED_NOT_ALLOWED_ERROR.formatted(modeName));
                }
                yield new StopInitializationMode(instructionFactory);
            }
            case RANDOM_INIT_MODE -> {
                if (seed == null) {
                    throw new InitializationModeChangeException(SEED_REQUIRED_EXCEPTION.formatted(modeName));
                } else if (seed < MINIMUM_SEED || seed > MAXIMUM_SEED) {
                    throw new InitializationModeChangeException(SEED_OUT_OF_BOUNDS.formatted(MINIMUM_SEED, MAXIMUM_SEED));
                }
                yield new RandomInitializationMode(instructionFactory, seed);
            }
            default -> throw new InitializationModeChangeException(MODE_NOT_FOUND_EXCEPTION);
        };
    }
}
