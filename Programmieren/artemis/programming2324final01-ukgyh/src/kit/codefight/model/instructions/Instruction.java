package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionExecutionException;

/**
 * Represents an abstract instruction that AIs can execute in the game.
 * Instructions are actions that can be performed within the game, each with specific arguments and effects.
 * @author ukgyh
 */
public abstract class Instruction {
    private String lastEditorName;
    private final String stringRepresentation;
    private int argumentA;
    private int argumentB;

    /**
     * Constructs an Instruction with specified arguments and owner information.
     *
     * @param owner The name of the last editor or owner of this instruction.
     * @param argumentA The first argument of the instruction.
     * @param argumentB The second argument of the instruction.
     * @param stringRepresentation A string representation of the instruction.
     */
    protected Instruction(String owner, int argumentA, int argumentB, String stringRepresentation) {
        this.argumentA = argumentA;
        this.argumentB = argumentB;
        this.lastEditorName = owner;
        this.stringRepresentation = stringRepresentation;
    }

    /**
     * Returns the name (string representation) of an instruction.
     * @return A string representing the instruction.
     */
    public String getName() {
        return stringRepresentation;
    }

    /**
     * Determines if this instruction is valid to be the first instruction executed.
     * @return true if it is valid to be the first instruction, false otherwise.
     */
    public abstract boolean isValidFirstInstruction();

    /**
     * Gets the last editor's name of this instruction.
     * @return The name of the last editor.
     */
    public String getLastEditorName() {
        return lastEditorName;
    }

    /**
     * Sets the last editor's name of this instruction.
     * @param lastEditorName The name of the last editor to set.
     */
    public void setLastEditor(String lastEditorName) {
        this.lastEditorName  = lastEditorName;
    }

    /**
     * Executes this instruction and applies the resulting effect.
     *
     * @param executorName The name of the executor performing this instruction.
     * @throws InstructionExecutionException If the execution fails due to any reason.
     */
    public abstract void execute(String executorName) throws InstructionExecutionException;

    /**
     * Gets argument A of this instruction.
     * @return argument A of this instruction.
     */
    public int getArgumentA() {
        return argumentA;
    }

    /**
     * Gets argument B of this instruction.
     * @return argument B of this instruction.
     */
    public int getArgumentB() {
        return argumentB;
    }

    /**
     * Sets the bomb status of this instruction.
     */
    public abstract void setBombStatus();

    /**
     * Determines if this instruction is an AI bomb.
     * @return true if this instruction is an AI bomb, false otherwise.
     */
    public abstract boolean isAIBomb();

    /**
     * Sets argument A of this instruction.
     * @param argumentA The value to set argument A to.
     */
    public void setArgumentA(int argumentA) {
        this.argumentA = argumentA;
    }

    /**
     * Sets argument B of this instruction.
     * @param argumentB The value to set argument B to.
     */
    public void setArgumentB(int argumentB) {
        this.argumentB = argumentB;
    }

    /**
     * Creates a new instance of this instruction, copying relevant fields.
     * @return A new instance of the instruction with the same state.
     */
    public abstract Instruction copy();

    /**
     * sets the bomb status of this instruction when copying the instruction.
     * @param bombStatus The bomb status to copy.
     */
    protected abstract void copyBombStatus(boolean bombStatus);

}
