package kit.codefight.model.ai;

import kit.codefight.model.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AI entity in a game, handling its instructions and state.
 * @author ukgyh
 */
public class Ai {
    private final List<Instruction> startingInstructions;
    private int pointer;
    private int counter;

    /**
     * constructs an AI object with the given name, id and starting instructions.
     * @param startingInstructions the instruction with which the AI starts into the game
     */
    public Ai(List<Instruction> startingInstructions) {
        this.startingInstructions = deepCopy(startingInstructions);
        this.counter = 0;
    }

    /**
     * Creates a copy of an existing AI with a new name.
     * @param originalAi The original AI to copy.
     * @param newName The new name for the AI.
     */
    public Ai(Ai originalAi, String newName) {
        this.startingInstructions = renameInstructions(deepCopy(originalAi.startingInstructions), newName);
        this.counter = 0;
    }

    /**
     * provides the current position of the AI in memory.
     * @return position of AI
     */
    public int getPointer() {
        return this.pointer;
    }


    /**
     * changes the position of the AI in memory.
     * @param position the new position of the AI
     */
    public void setPointer(int position) {
        this.pointer = position;
    }


    /**
     * Increments the internal counter of the AI.
     */
    public void incrementCounter() {
        this.counter++;
    }

    /**
     * Returns the value of the AI's counter.
     * @return The counter value.
     */
    public int getCounter() {
        return this.counter;
    }


    /**
     * Provides a deep copy of the AI's starting instructions.
     * @return A deep copy of the starting instructions.
     */
    public List<Instruction> getInstructions() {
        return deepCopy(startingInstructions);
    }

    private List<Instruction> deepCopy(List<Instruction> instructions) {
        List<Instruction> copy = new ArrayList<>();
        for (Instruction instruction : instructions) {
            copy.add(instruction.copy());
        }
        return copy;
    }

    private List<Instruction> renameInstructions(List<Instruction> instructions, String newName) {
        List<Instruction> renamedInstructions = new ArrayList<>();
        for (Instruction instruction : instructions) {
            instruction.setLastEditor(newName);
            renamedInstructions.add(instruction);
        }
        return renamedInstructions;
    }
}
