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
final class ConditionalSkipInstruction extends Instruction {
    private static final String STRING_REPRESENTATION = "CMP";
    private static final boolean IS_AI_BOMB = false;
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private static final int SKIP_INCREMENT = 1;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Conditional Skip Instruction.
     *
     * @param aiHandler the AiStateHandler to be used
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    ConditionalSkipInstruction(AiStateHandler aiHandler, MemoryStateHandler memoryStateHandler, int argA, int argB, String owner) {
        super(owner, argA, argB, STRING_REPRESENTATION);
        this.aiStateHandler = aiHandler;
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
        int firstPosition = currentAiPosition + getArgumentA();
        int secondPosition = currentAiPosition + getArgumentB();

        int firstArgA;
        int secondArgB;
        try {
            firstArgA = memoryStateHandler.getInstructionAtIndex(firstPosition).getArgumentA();
            secondArgB = memoryStateHandler.getInstructionAtIndex(secondPosition).getArgumentB();
        } catch (MemoryOutOfBoundsException e) {
            throw new InstructionExecutionException(e.getMessage(), e);
        }
        if (firstArgA != secondArgB) {
            int newPosition = currentAiPosition + SKIP_INCREMENT;
            aiStateHandler.setAiPointer(executorName, newPosition);
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

        Instruction copy = new ConditionalSkipInstruction(this.aiStateHandler, this.memoryStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }

}
