package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

/**
 * represents the Swap instruction.
 * @author ukgyh
 */

final class SwapInstruction extends Instruction {
    private static final boolean IS_AI_BOMB = false;
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private static final String STRING_REPRESENTATION = "SWAP";
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Swap Instruction.
     *
     * @param aiStateHandler the AiStateHandler to be used
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    SwapInstruction(AiStateHandler aiStateHandler, MemoryStateHandler memoryStateHandler, int argA, int argB, String owner) {
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
        int firstPosition = currentAiPosition + getArgumentA();
        int secondPosition = currentAiPosition + getArgumentB();

        // Check if first and second positions are the same, indicating the same instruction
        if (firstPosition == secondPosition) {
            try {
                Instruction instruction = memoryStateHandler.getInstructionAtIndex(firstPosition);
                // Swap argument A and B within the same instruction
                int temp = instruction.getArgumentA();
                instruction.setArgumentA(instruction.getArgumentB());
                instruction.setArgumentB(temp);

                instruction.setLastEditor(executorName);
                instruction.setBombStatus();

                memoryStateHandler.putInstructionAtIndex(firstPosition, instruction);
            } catch (MemoryOutOfBoundsException e) {
                throw new InstructionExecutionException(e.getMessage(), e);
            }
        } else {
            try {
                Instruction firstInstruction = memoryStateHandler.getInstructionAtIndex(firstPosition);
                Instruction secondInstruction = memoryStateHandler.getInstructionAtIndex(secondPosition);

                int firstArgA = firstInstruction.getArgumentA();
                int secondArgB = secondInstruction.getArgumentB();

                firstInstruction.setArgumentA(secondArgB);
                secondInstruction.setArgumentB(firstArgA);

                firstInstruction.setLastEditor(executorName);
                secondInstruction.setLastEditor(executorName);

                firstInstruction.setBombStatus();
                secondInstruction.setBombStatus();

                memoryStateHandler.putInstructionAtIndex(firstPosition, firstInstruction);
                memoryStateHandler.putInstructionAtIndex(secondPosition, secondInstruction);

            } catch (MemoryOutOfBoundsException e) {
                throw new InstructionExecutionException(e.getMessage(), e);
            }
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

        Instruction copy = new SwapInstruction(this.aiStateHandler, this.memoryStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }
}
