package kit.codefight.model.ai;

import kit.codefight.exceptions.AiCreationException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;

import kit.codefight.model.instructions.Instruction;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;


/**
 * Handles the state of the AI entities in the game, including their instructions and memory.
 * @author ukgyh
 */
public final class AiStateHandler {
    private static final String DUPLICATE_NAME_EXCEPTION = "name already used by another AI";
    private static final String AI_NOT_REGISTERED_EXCEPTION = "AI %s is not registered";
    private static final String AI_NOT_ACTIVE_EXCEPTION = "AI %s is not in the current game";
    private static final String DUPLICATES_SYMBOL = "#";
    private static final String AI_OVERLOAD_ERROR = "number of active AI must can't be bigger than %d";
    private static final String MEMORY_OUT_OF_BOUNDS_EXCEPTION = "index %d is out of bounds for memory size %d";
    private static final String TOO_MANY_INSTRUCTIONS_ERROR = "too many starting instructions for memory size";
    private static final int MINIMUM_MEMORY_INDEX = 0;
    private static final int EMPTY_ARRAY_SIZE = 0;
    private static final float MINIMUM_AI_AMOUNT = 2;
    private final int maxNumberAi;
    private final int memorySize;
    private final Map<String, Ai> registeredAi;
    private final Map<String, Ai> activeAi;
    private final Map<String, Ai> stoppedAi;
    private final List<String> orderedNameList;


    /**
     * Constructs a new AiStateHandler.
     *
     * @param maxNumberAi the maximum number of AIs allowed in one game
     * @param memorySize  the size of the memory
     */
    public AiStateHandler(int maxNumberAi, int memorySize) {
        this.maxNumberAi = maxNumberAi;
        this.memorySize = memorySize;
        this.registeredAi = new HashMap<>();
        this.activeAi = new LinkedHashMap<>();
        this.stoppedAi = new HashMap<>();
        this.orderedNameList = new ArrayList<>();
    }

    /**
     * Adds a new AI to the game.
     *
     * @param aiName              the name of the AI
     * @param startingInstructions the instructions with which the AI starts into the game
     * @throws AiCreationException if the AI name is already used by another AI
     */
    public void addAi(String aiName, List<Instruction> startingInstructions) throws AiCreationException {
        if (checkForRegisteredAi(aiName)) {
            throw new AiCreationException(DUPLICATE_NAME_EXCEPTION);
        }
        if (startingInstructions.size() > Math.ceil(memorySize / MINIMUM_AI_AMOUNT)) {
            throw new AiCreationException(TOO_MANY_INSTRUCTIONS_ERROR);
        }
        registeredAi.put(aiName, new Ai(startingInstructions));
    }

    /**
     * Removes an AI from the game.
     *
     * @param aiName the name of the AI to remove
     * @throws MissingAiException if the AI is not registered
     */
    public void removeAi(String aiName) throws MissingAiException {
        if (!checkForRegisteredAi(aiName)) {
            throw new MissingAiException(AI_NOT_REGISTERED_EXCEPTION.formatted(aiName));
        }
        registeredAi.remove(aiName);
    }

    /**
     * Resets the active nad stopped AI.
     */
    public void reset() {
        this.activeAi.clear();
        this.stoppedAi.clear();
        this.orderedNameList.clear();
    }

    /**
     * Activates a list of AIs for participation in the game.
     *
     * @param aiNames The names of the AIs to activate.
     * @throws MissingAiException If any AI is not registered or the number of AIs exceeds the maximum allowed.
     */
    public void activateAi(String[] aiNames) throws MissingAiException {

        if (aiNames.length > maxNumberAi) {
            throw new MissingAiException(AI_OVERLOAD_ERROR.formatted(maxNumberAi));
        }

        HashMap<String, Integer> aiNameCounts = new HashMap<>();
        for (String aiName : aiNames) {

            //updating the name count for each AI, incrementing when same name is found
            aiNameCounts.put(aiName, aiNameCounts.getOrDefault(aiName, 0) + 1);
        }

        HashMap<String, Integer> aiNameIndex = new HashMap<>();
        for (String aiName : aiNames) {
            if (!registeredAi.containsKey(aiName)) {
                this.reset(); // clear active and stopped AI before throwing an exception
                throw new MissingAiException(AI_NOT_REGISTERED_EXCEPTION.formatted(aiName));
            }

            int index = aiNameIndex.getOrDefault(aiName, 0);
            aiNameIndex.put(aiName, index + 1); // Update the index for this AI name

            String newName = aiName;
            if (aiNameCounts.get(aiName) > 1) { // If it's a duplicate, append the index, starting from 0
                newName += DUPLICATES_SYMBOL + index;
            }

            activeAi.put(newName, new Ai(registeredAi.get(aiName), newName));
            orderedNameList.add(newName);
        }
    }

    /**
     * Retrieves the names of all currently active AIs.
     * @return A list of active AI names.
     */
    public List<String> getActiveAiNames() {
        //order is maintained through LinkedHashMap
        return List.of(activeAi.keySet().toArray(new String[EMPTY_ARRAY_SIZE]));
    }

    /**
     * Retrieves the names of AIs in the order they were activated.
     * @return An ordered list of AI names.
     */
    public List<String> getOrderedAiNames() {
        return new ArrayList<>(orderedNameList);
    }


    /**
     * Gets the starting instructions of an active AI.
     *
     * @param aiName The name of the AI.
     * @return The list of starting instructions for the AI.
     * @throws MissingAiException If the AI is not active.
     */
    public List<Instruction> getActiveStartingInstructions(String aiName) throws MissingAiException {
        if (!checkForActiveAi(aiName)) {
            throw new MissingAiException(AI_NOT_REGISTERED_EXCEPTION.formatted(aiName));
        }
        return activeAi.get(aiName).getInstructions();
    }

    /**
     * Initializes the memory pointer of an active AI.
     *
     * @param aiName     The name of the AI.
     * @param newPointer The new memory pointer position.
     * @throws MissingAiException        If the AI is not active.
     * @throws MemoryOutOfBoundsException If the new pointer is outside the allowed memory range.
     */
    public void initializeAiPointer(String aiName, int newPointer) throws MissingAiException, MemoryOutOfBoundsException {
        if (!checkForActiveAi(aiName)) {
            throw new MissingAiException(AI_NOT_ACTIVE_EXCEPTION.formatted(aiName));
        } else if (newPointer < MINIMUM_MEMORY_INDEX || newPointer > memorySize) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_EXCEPTION.formatted(newPointer, memorySize));
        }

        activeAi.get(aiName).setPointer(newPointer);
    }

    /**
     * Sets the memory pointer of an active AI, adjusting it to remain within memory bounds.
     *
     * @param aiName     The name of the AI.
     * @param newPointer The new memory pointer position.
     */
    public void setAiPointer(String aiName, int newPointer) {
        if (checkForActiveAi(aiName)) {
            int circularIndex = calculateCircularIndex(newPointer);
            activeAi.get(aiName).setPointer(circularIndex);
        }
    }

    /**
     * Retrieves the current memory pointer of an active AI.
     *
     * @param aiName The name of the AI.
     * @return The current memory pointer position.
     * @throws MissingAiException If the AI is not active.
     */
    public int getAiPointer(String aiName) throws MissingAiException {
        if (!checkForActiveAi(aiName)) {
            throw new MissingAiException(AI_NOT_ACTIVE_EXCEPTION.formatted(aiName));
        }
        return activeAi.get(aiName).getPointer();
    }

    /**
     * Retrieves the execution counter of an AI.
     *
     * @param aiName The name of the AI.
     * @return The current value of the AI's execution counter.
     * @throws MissingAiException If the AI is neither active nor stopped.
     */
    public int getAiCounter(String aiName) throws MissingAiException {
        if (checkForActiveAi(aiName)) {
            return activeAi.get(aiName).getCounter();
        } else if (checkForStoppedAi(aiName)) {
            return stoppedAi.get(aiName).getCounter();
        } else {
            throw new MissingAiException(AI_NOT_ACTIVE_EXCEPTION.formatted(aiName));
        }
    }

    /**
     * Stops an active AI, moving it to the stopped state.
     *
     * @param aiName The name of the AI to stop.
     * @throws MissingAiException If the AI is not active.
     */
    public void stopAi(String aiName) throws MissingAiException {
        if (!checkForActiveAi(aiName)) {
            throw new MissingAiException(AI_NOT_ACTIVE_EXCEPTION.formatted(aiName));
        }
        stoppedAi.put(aiName, activeAi.get(aiName));
        activeAi.remove(aiName);
    }

    /**
     * Increments the execution counter of an active AI.
     *
     * @param aiName The name of the AI.
     * @throws MissingAiException If the AI is not active.
     */
    public void incrementAiCounter(String aiName) throws MissingAiException {
        if (!checkForActiveAi(aiName)) {
            throw new MissingAiException(AI_NOT_ACTIVE_EXCEPTION.formatted(aiName));
        }
        activeAi.get(aiName).incrementCounter();
    }

    private int calculateCircularIndex(int index) {
        //realises the circular nature of the memory
        int adjustedIndex = index % memorySize;
        if (adjustedIndex < 0) {
            adjustedIndex += memorySize;
        }
        return adjustedIndex;
    }


    /**
     * Checks if an AI is currently active.
     *
     * @param aiName The name of the AI to check.
     * @return {@code true} if the AI is active, {@code false} otherwise.
     */
    public boolean checkForActiveAi(String aiName) {
        return activeAi.containsKey(aiName);
    }


    /**
     * Checks if an AI has been stopped and is currently not active.
     *
     * @param aiName The name of the AI to check.
     * @return {@code true} if the AI has been stopped, {@code false} otherwise.
     */
    public boolean checkForStoppedAi(String aiName) {
        return stoppedAi.containsKey(aiName);
    }

    private boolean checkForRegisteredAi(String aiName) {
        return registeredAi.containsKey(aiName);
    }
}
