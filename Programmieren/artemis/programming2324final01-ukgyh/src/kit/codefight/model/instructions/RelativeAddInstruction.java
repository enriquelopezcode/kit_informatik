package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

/**
 * represents the Relative Add instruction.
 * @author ukgyh
 */
final class RelativeAddInstruction extends Instruction {
    private static final String STRING_REPRESENTATION = "ADD_R";
    private static final boolean IS_AI_BOMB = false;
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Relative Add Instruction.
     *
     * @param aiStateHandler the AiStateHandler to be used
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    RelativeAddInstruction(AiStateHandler aiStateHandler, MemoryStateHandler memoryStateHandler, int argA, int argB, String owner) {
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
        int target = currentAiPosition + getArgumentB();
        try {
            Instruction copiedInstruction = memoryStateHandler.getInstructionAtIndex(target);
            int targetArgB = copiedInstruction.getArgumentB();
            copiedInstruction.setArgumentB(getArgumentA() + targetArgB);

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

        Instruction copy = new RelativeAddInstruction(this.aiStateHandler, this.memoryStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }
}
