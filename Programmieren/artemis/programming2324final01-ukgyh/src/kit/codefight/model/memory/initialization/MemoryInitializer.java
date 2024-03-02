package kit.codefight.model.memory.initialization;

import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.exceptions.InitializationModeChangeException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MemoryOverloadException;
import kit.codefight.exceptions.MissingAiException;

import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.instructions.Instruction;
import kit.codefight.model.instructions.InstructionFactory;
import kit.codefight.model.memory.MemoryStateHandler;

import java.util.List;
/**
 * This class is responsible for initializing the memory with the starting instructions of the AIs.
 * It also has the responsibility of setting the mode of initialization for the memory.
 * @author ukgyh
 */
public final class MemoryInitializer {
    private static final String MEMORY_OVERLOAD_ERROR = "too many starting instructions for memory size";
    private static final String MEMORY_FILL_ERROR = "there was an error when trying to fill up memory";
    private static final String NO_OWNER = null;
    private final InitializationModeFactory initializationModeFactory;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private MemoryInitializationMode initializationMode;

    /**
     * Constructs new MemoryInitializer.
     * @param memoryStateHandler the handler for the memory state
     * @param aiStateHandler the handler for the AI state
     * @param instructionFactory the factory for creating instructions
     */
    public MemoryInitializer(MemoryStateHandler memoryStateHandler, AiStateHandler aiStateHandler, InstructionFactory instructionFactory) {
        this.memoryStateHandler = memoryStateHandler;
        this.aiStateHandler = aiStateHandler;
        this.initializationModeFactory = new InitializationModeFactory(instructionFactory);
        this.initializationMode = new StopInitializationMode(instructionFactory);
    }

    /**
     * Initializes the memory with the starting instructions of the AIs.
     * @throws MissingAiException if there are no active AIs
     * @throws MemoryOverloadException if there are too many starting instructions for the memory size
     * @throws InstructionCreationException if there is an error when creating the instructions
     */
    public void initializeMemory() throws MissingAiException, MemoryOverloadException, InstructionCreationException {
        List<String> aiNames = aiStateHandler.getActiveAiNames();
        int memorySize = memoryStateHandler.getMemorySize();
        memoryStateHandler.createMemory(memorySize);

        float numberOfAi = aiNames.size();
        float distanceBetweenAi = memorySize / numberOfAi;

        try {
            fillMemory();
        } catch (MemoryOutOfBoundsException e) {
            resetInitialization();
            throw new MemoryOverloadException(MEMORY_FILL_ERROR, e);
        }

        for (int i = 0; i < numberOfAi; i++)  {

            List<Instruction> startingInstructions = aiStateHandler.getActiveStartingInstructions(aiNames.get(i));
            boolean isLastAi = (i == aiNames.size() - 1);
            checkValidInstructionAmount(distanceBetweenAi, startingInstructions.size(), isLastAi);

            int aiIndex = (int) Math.floor(i * distanceBetweenAi);
            //writing the instructions in memory
            for (int j = 0; j < startingInstructions.size(); j++) {
                try {
                    memoryStateHandler.putInstructionAtIndex(aiIndex + j, startingInstructions.get(j));
                } catch (MemoryOutOfBoundsException e) {
                    resetInitialization();
                    throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR, e);
                }
            }
            //set AI pointer to be at first legal starting instruction
            Instruction initialInstruction;
            try {
                initialInstruction = memoryStateHandler.getInstructionAtIndex(aiIndex);
            } catch (MemoryOutOfBoundsException e) {
                resetInitialization();
                throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR, e);
            }
            while (!initialInstruction.isValidFirstInstruction()) {
                aiIndex++;
                try {
                    initialInstruction = memoryStateHandler.getInstructionAtIndex(aiIndex);
                } catch (MemoryOutOfBoundsException e) {
                    resetInitialization();
                    throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR, e);
                }
            }
            try {
                aiStateHandler.initializeAiPointer(aiNames.get(i), aiIndex);
            } catch (MemoryOutOfBoundsException e) {
                resetInitialization();
                throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR, e);
            }
        }

    }

    /**
     * Changes the mode of initialization for the memory.
     * @param newMode the new mode of initialization
     * @param seed the seed for the new mode of initialization
     * @throws InitializationModeChangeException if there is an error when changing the mode of initialization
     */
    public void setInitializationMode(String newMode, Integer seed) throws InitializationModeChangeException {
        this.initializationMode = initializationModeFactory.createInitializationMode(newMode, seed);

    }

    /**
     * Gets the information about the current mode of initialization.
     * @return name and if existent seed of the current mode of initialization
     */
    public String[] getInitializationModeInfo() {
        return this.initializationMode.getModeInfo();
    }

    private void fillMemory() throws MemoryOutOfBoundsException, InstructionCreationException {
        for (int i = 0; i < memoryStateHandler.getMemorySize(); i++) {
            if (memoryStateHandler.isMemoryCellEmpty(i)) {
                Instruction newInstruction = initializationMode.produceInstruction(NO_OWNER);
                memoryStateHandler.putInstructionAtIndex(i, newInstruction);
            }
        }
    }

    private void resetInitialization() {
        memoryStateHandler.reset();
        aiStateHandler.reset();
    }

    private void checkValidInstructionAmount(float distance, int instructionAmount, boolean isLast) throws MemoryOverloadException {
        if (!isLast) {
            //calculating if AI instructions would overlap
            if (instructionAmount  > Math.floor(distance)) {
                resetInitialization();
                throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR);
            }
        } else {
            //the last AI could have an extra instruction if memory size is uneven
            if (instructionAmount  > Math.ceil(distance)) {
                resetInitialization();
                throw new MemoryOverloadException(MEMORY_OVERLOAD_ERROR);
            }
        }
    }
}
