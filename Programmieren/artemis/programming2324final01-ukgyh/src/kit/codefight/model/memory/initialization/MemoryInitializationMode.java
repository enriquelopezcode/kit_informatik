package kit.codefight.model.memory.initialization;

import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.model.instructions.Instruction;

/**
 * This interface is used to define the methods for the different memory initialization modes.
 * @author ukgyh
 */
public interface MemoryInitializationMode {
    /**
     * Produces an instruction for the given owner.
     * @param owner the owner of the instruction
     * @return the produced instruction
     * @throws InstructionCreationException if the instruction creation fails
     */
    Instruction produceInstruction(String owner) throws InstructionCreationException;

    /**
     * Returns the information about the mode.
     * @return information about the mode
     */
    String[] getModeInfo();

    /**
     * Checks if the given object is equal to this mode.
     * @param obj the object to be checked
     * @return true if the given object is equal to this mode, false otherwise
     */
    boolean equals(Object obj);

    /**
     * Returns the hash code of this mode.
     * @return the hash code
     */
    int hashCode();
}
