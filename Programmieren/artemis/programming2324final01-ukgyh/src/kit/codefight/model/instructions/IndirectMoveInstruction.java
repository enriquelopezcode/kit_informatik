package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

/**
 * Represents the Conditional Skip instruction.
 * @author ukgyh
 */
final class IndirectMoveInstruction extends Instruction {
    private static final String STRING_REPRESENTATION = "MOV_I";
    private static final boolean IS_AI_BOMB = false;
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private final MemoryStateHandler memoryStateHandler;
    private final AiStateHandler aiStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Indirect Move Instruction.
     *
     * @param aiStateHandler the AiStateHandler to be used
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    IndirectMoveInstruction(AiStateHandler aiStateHandler, MemoryStateHandler memoryStateHandler, int argA, int argB, String owner) {
        super(owner, argA, argB, STRING_REPRESENTATION);
        this.aiStateHandler = aiStateHandler;
        this.memoryStateHandler = memoryStateHandler;
    }

    @Override
    public boolean isAIBomb() {
        return isAiBomb;
    }

    @Override
    public void setBombStatus() {
        this.isAiBomb = IS_AI_BOMB;
    }

    @Override
    public boolean isValidFirstInstruction() {
        return VALID_STARTING_INSTRUCTION;
    }

    @Override
    public void execute(String executorName) throws InstructionExecutionException {
        int currentAiPosition;
        try {
            currentAiPosition = aiStateHandler.getAiPointer(executorName);
        } catch (MissingAiException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }
        int source = currentAiPosition + getArgumentA();
        int intermediate = currentAiPosition + getArgumentB();
        try {
            // Determine the target position and copy the instruction to the new location
            int intermediateArgB = memoryStateHandler.getInstructionAtIndex(intermediate).getArgumentB();
            int target = memoryStateHandler.calculateCircularIndex(intermediate) + intermediateArgB;
            Instruction copiedInstruction = memoryStateHandler.getInstructionAtIndex(source);

            copiedInstruction.setLastEditor(executorName);
            copiedInstruction.setBombStatus();

            memoryStateHandler.putInstructionAtIndex(target, copiedInstruction);
        } catch (MemoryOutOfBoundsException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }

        try {
            aiStateHandler.incrementAiCounter(executorName);
        } catch (MissingAiException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }

    }

    @Override
    public Instruction copy() {
        int argA = this.getArgumentA();
        int argB = this.getArgumentB();
        String lastEditorName = this.getLastEditorName();

        Instruction copy = new IndirectMoveInstruction(this.aiStateHandler, this.memoryStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }
}
