package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

/**
 * represents the Jump instruction.
 * @author ukgyh
 */
final class JumpInstruction extends Instruction {
    private static final String STRING_REPRESENTATION = "JMP";
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private static final int INCREMENT_OFFSET = -1;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Jump Instruction.
     *
     * @param aiStateHandler the AiStateHandler to be used
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    JumpInstruction(AiStateHandler aiStateHandler, MemoryStateHandler memoryStateHandler, int argA, int argB, String owner) {
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
        this.isAiBomb = getArgumentA() == 0;
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
        int newPosition = memoryStateHandler.calculateCircularIndex(currentAiPosition + getArgumentA()) + INCREMENT_OFFSET;

        //this ensures that the AI will be at index 0 after the step is executed
        if (newPosition < 0) {
            newPosition = memoryStateHandler.getMemorySize() - 1;
        }
        aiStateHandler.setAiPointer(executorName, newPosition);

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

        Instruction copy = new JumpInstruction(this.aiStateHandler, this.memoryStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }
}
