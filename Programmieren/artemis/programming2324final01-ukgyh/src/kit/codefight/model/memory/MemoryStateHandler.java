package kit.codefight.model.memory;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.model.instructions.Instruction;

/**
 * This class is responsible for creating and handling the state of the memory.
 * @author ukgyh
 */
public final class MemoryStateHandler {
    private final int memorySize;
    private Memory memory;

    /**
     * Constructs a new MemoryStateHandler.
     * @param memorySize the size of the memory
     */
    public MemoryStateHandler(int memorySize) {
        this.memorySize = memorySize;
    }

    /**
     * Resets the memory.
     */
    public void reset() {
        this.memory = null;
    }

    /**
     * Creates new memory.
     * @param memorySize the size of the memory
     */
    public void createMemory(int memorySize) {
        this.memory = new Memory(memorySize);
    }

    /**
     * Returns the size of the memory.
     * @return the size of the memory
     */
    public int getMemorySize() {
        return memorySize;
    }

    /**
     * Checks if the cell at the given index is empty.
     * @param index the index of the cell
     * @return true if the cell is empty, false otherwise
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     */
    public boolean isMemoryCellEmpty(int index) throws MemoryOutOfBoundsException {
        return memory.isCellEmpty(index);
    }

    /**
     * Puts the given instruction at the given index.
     * @param index the index of the cell
     * @param instruction the instruction to be put
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     */
    public void putInstructionAtIndex(int index, Instruction instruction) throws MemoryOutOfBoundsException {
        memory.putInstructionAtIndex(calculateCircularIndex(index), instruction);
    }

    /**
     * Returns the instruction at the given index.
     * @param index the index of the cell
     * @return the instruction at the given index
     * @throws MemoryOutOfBoundsException if there was a problem accessing memory correctly
     */
    public Instruction getInstructionAtIndex(int index) throws MemoryOutOfBoundsException {
        return memory.getInstructionAtIndex(calculateCircularIndex(index));
    }

    /**
     * Executes the instruction at the given index.
     * @param index the index of the cell
     * @param aiExecutorName the name of the AI that is executing the instruction
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     * @throws InstructionExecutionException if there is a problem executing the instruction
     */
    public void executeInstruction(int index, String aiExecutorName) throws MemoryOutOfBoundsException, InstructionExecutionException {
        memory.executeInstruction(index, aiExecutorName);
    }

    /**
     * Calculates the circular index of the memory.
     * @param index the index to be calculated
     * @return the calculated index
     */
    public int calculateCircularIndex(int index) {
        //realises circular memory
        int adjustedIndex = index % memorySize;
        if (adjustedIndex < 0) {
            adjustedIndex += memorySize;
        }
        return adjustedIndex;
    }

    /**
     * Calculates the circular index with custom memory size.
     * @param index the index to be calculated
     * @param memorySize the size of the memory
     * @return the calculated index
     */
    public int calculateCircularIndex(int index, int memorySize) {
        //realises circular memory
        int adjustedIndex = index % memorySize;
        if (adjustedIndex < 0) {
            adjustedIndex += memorySize;
        }
        return adjustedIndex;
    }

}
