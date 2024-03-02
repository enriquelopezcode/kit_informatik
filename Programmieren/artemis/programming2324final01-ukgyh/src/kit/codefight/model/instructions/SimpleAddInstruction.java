package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;

/**
 * represents the Simple Add instruction.
 * @author ukgyh
 */
final class SimpleAddInstruction extends Instruction {
    private static final String STRING_REPRESENTATION = "ADD";
    private static final boolean IS_AI_BOMB = false;
    private static final boolean VALID_STARTING_INSTRUCTION = true;
    private final AiStateHandler aiStateHandler;
    private boolean isAiBomb = false;

    /**
     * Constructs new Simple Add Instruction.
     *
     * @param aiStateHandler the AiStateHandler to be used
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     */
    SimpleAddInstruction(AiStateHandler aiStateHandler, int argA, int argB, String owner) {
        super(owner, argA, argB, STRING_REPRESENTATION);
        this.aiStateHandler = aiStateHandler;
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

        setArgumentB(getArgumentA() + getArgumentB());
        setLastEditor(executorName);
        setBombStatus();

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

        Instruction copy = new SimpleAddInstruction(this.aiStateHandler, argA, argB, lastEditorName);
        copy.copyBombStatus(this.isAiBomb);
        return copy;
    }

    @Override
    protected void copyBombStatus(boolean isAiBomb) {
        this.isAiBomb = isAiBomb;
    }
}
