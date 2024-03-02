package kit.codefight.model.memory;

import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.model.instructions.Instruction;

/**
 * This class represents the memory of the game.
 * @author ukgyh
 */
public class Memory {
    private static final String MEMORY_OUT_OF_BOUNDS_ERROR = "index %d is out of bounds for memory size %d";
    private static final int MINIMUM_MEMORY_INDEX = 0;
    private final Instruction[] memoryArray;

    /**
     * Constructs new Memory.
     * @param memorySize the amount of memory cells
     */
    Memory(int memorySize) {
        this.memoryArray = new Instruction[memorySize];
    }

    /**
     * Checks if the cell at the given index is empty.
     * @param index the index of the cell
     * @return true if the cell is empty, false otherwise
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     */
    public boolean isCellEmpty(int index) throws MemoryOutOfBoundsException {
        if (!isValidIndex(index)) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_ERROR.formatted(index, memoryArray.length));
        }
        return memoryArray[index] == null;
    }

    /**
     * Checks if the given index is valid.
     * @param index the index to be checked
     * @return true if the index is valid, false otherwise
     */
    private boolean isValidIndex(int index) {
        return index >= MINIMUM_MEMORY_INDEX && index < memoryArray.length;
    }

    /**
     * Puts the given instruction at the given index.
     * @param index the index of the cell
     * @param instruction the instruction to be put
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     */
    public void putInstructionAtIndex(int index, Instruction instruction) throws MemoryOutOfBoundsException {
        if (!isValidIndex(index)) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_ERROR.formatted(index, memoryArray.length));
        }
        memoryArray[index] = instruction;


    }

    /**
     * Executes the instruction at the given index.
     * @param index the index of the cell
     * @param aiName the name of the AI that is executing the instruction
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     * @throws InstructionExecutionException if there is a problem executing the instruction
     */
    public void executeInstruction(int index, String aiName) throws MemoryOutOfBoundsException, InstructionExecutionException {
        if (!isValidIndex(index)) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_ERROR.formatted(index, memoryArray.length));
        }
        memoryArray[index].execute(aiName);
    }

    /**
     * Gets the instruction at the given index.
     * @param index the index of the cell
     * @return the instruction at the given index
     * @throws MemoryOutOfBoundsException if the index is out of bounds
     */
    public Instruction getInstructionAtIndex(int index) throws MemoryOutOfBoundsException {
        if (!isValidIndex(index)) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_ERROR.formatted(index, memoryArray.length));
        }
        return memoryArray[index].copy();

    }
}

