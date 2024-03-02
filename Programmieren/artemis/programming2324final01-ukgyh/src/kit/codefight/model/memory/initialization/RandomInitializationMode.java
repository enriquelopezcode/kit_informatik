package kit.codefight.model.memory.initialization;

import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.model.instructions.Instruction;
import kit.codefight.model.instructions.InstructionFactory;

import java.util.Random;

/**
 * represents the Random Initialization mode that fills the memory with random instructions.
 * @author ukgyh
 */
public final class RandomInitializationMode implements MemoryInitializationMode {
    private static final String MODE_NAME = "INIT_MODE_RANDOM";
    private static final int INFORMATION_AMOUNT = 2;
    private static final int INFORMATION_NAME_INDEX = 0;
    private static final int INFORMATION_SEED_INDEX = 1;
    private static final int NUMBER_OF_INSTRUCTIONS = 9;
    private final int seed;
    private final Random numberGenerator;
    private final InstructionFactory instructionFactory;

    /**
     * Constructs new RandomInitializationMode.
     * @param instructionFactory the factory for creating instructions
     * @param seed the seed for the random number generator
     */
    RandomInitializationMode(InstructionFactory instructionFactory, int seed) {
        this.instructionFactory = instructionFactory;
        this.seed = seed;
        this.numberGenerator = new Random(seed);
    }


    @Override
    public Instruction produceInstruction(String owner) throws InstructionCreationException {
        int instructionID = numberGenerator.nextInt(NUMBER_OF_INSTRUCTIONS);
        int argA = numberGenerator.nextInt();
        int argB = numberGenerator.nextInt();
        return instructionFactory.createInstructionByID(instructionID, argA, argB, owner);
    }

    @Override
    public String[] getModeInfo() {
        String[] info = new String[INFORMATION_AMOUNT];
        info[INFORMATION_NAME_INDEX] = MODE_NAME;
        info[INFORMATION_SEED_INDEX] = Integer.toString(seed);
        return info;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RandomInitializationMode randomInitializationMode) {
            return seed == randomInitializationMode.seed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(seed);
    }
}
