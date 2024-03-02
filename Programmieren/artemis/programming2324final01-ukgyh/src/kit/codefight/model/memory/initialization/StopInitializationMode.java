package kit.codefight.model.memory.initialization;

import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.model.instructions.Instruction;
import kit.codefight.model.instructions.InstructionFactory;

/**
 * This class represents the stop initialization mode which fills the memory with stop instructions.
 * @author ukgyh
 */
public final class StopInitializationMode implements MemoryInitializationMode {
    private static final String MODE_NAME = "INIT_MODE_STOP";
    private static final int HASH_CODE_INT = 0;
    private static final int INFORMATION_AMOUNT = 1;
    private static final int INFORMATION_NAME_INDEX = 0;
    private static final int STOP_ID = 0;
    private static final int STOP_ARG_A = 0;
    private static final int STOP_ARG_B = 0;
    private final InstructionFactory instructionFactory;
    StopInitializationMode(InstructionFactory instructionFactory) {
        this.instructionFactory = instructionFactory;
    }

    @Override
    public String[] getModeInfo() {
        String[] info = new String[INFORMATION_AMOUNT];
        info[INFORMATION_NAME_INDEX] = MODE_NAME;
        return info;
    }

    @Override
    public Instruction produceInstruction(String owner) throws InstructionCreationException {
        return instructionFactory.createInstructionByID(STOP_ID, STOP_ARG_A, STOP_ARG_B, owner);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StopInitializationMode;
    }

    @Override
    public int hashCode() {
        return HASH_CODE_INT;
    }
}
