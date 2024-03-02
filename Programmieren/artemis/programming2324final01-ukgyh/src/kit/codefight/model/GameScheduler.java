package kit.codefight.model;

import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.exceptions.InstructionExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for scheduling and executing game steps for AI entities.
 * @author ukgyh
 */
public class GameScheduler {
    private static final String GAME_EXECUTION_ERROR = "there was an error during execution of the game";
    private static final int AI_STEP_AMOUNT = 1;
    private static final int STARTING_COUNTER = 0;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private List<String> activeAi;
    private int counter;


    /**
     * Constructs a GameScheduler instance with specified memory and AI state handlers.
     *
     * @param memoryStateHandler The handler responsible for managing memory states.
     * @param aiStateHandler The handler responsible for managing AI states.
     */
    GameScheduler(MemoryStateHandler memoryStateHandler, AiStateHandler aiStateHandler) {
        this.aiStateHandler  = aiStateHandler;
        this.memoryStateHandler = memoryStateHandler;
        this.counter = STARTING_COUNTER;
    }

    /**
     * Executes the next set of steps for the active AI entities.
     *
     * @param steps The number of steps to execute.
     * @return A list of arrays where each array contains the name and counter of an AI that has stopped executing.
     * @throws GameExecutionException If there is an error executing the game steps.
     */
    public List<String[]> doNextSteps(int steps) throws GameExecutionException {
        List<String[]> stoppedAiInfo = new ArrayList<>();

        for (int i = 0; i < steps; i++) {
            //if no active AI, break the loop
            if (activeAi == null || activeAi.isEmpty()) {
                break;
            }

            String currentAiName = activeAi.get(counter);
            int currentAiPointer;
            try {
                currentAiPointer = aiStateHandler.getAiPointer(currentAiName);
            } catch (MissingAiException e) {
                throw new GameExecutionException(GAME_EXECUTION_ERROR, e);
            }

            try {
                memoryStateHandler.executeInstruction(currentAiPointer, currentAiName);
            } catch (MemoryOutOfBoundsException | InstructionExecutionException e) {
                throw new GameExecutionException(GAME_EXECUTION_ERROR, e);
            }

            updateActiveAi();

            if (!activeAi.contains(currentAiName)) {
                int aiCounter;
                try {
                    aiCounter = aiStateHandler.getAiCounter(currentAiName);
                } catch (MissingAiException e) {
                    throw new GameExecutionException(GAME_EXECUTION_ERROR, e);
                }
                stoppedAiInfo.add(new String[]{currentAiName, String.valueOf(aiCounter)});
                continue;
            }

            int newAiPointer;
            try {
                newAiPointer = aiStateHandler.getAiPointer(currentAiName);
            } catch (MissingAiException e) {
                throw new GameExecutionException(GAME_EXECUTION_ERROR, e);
            }
            aiStateHandler.setAiPointer(currentAiName, newAiPointer + AI_STEP_AMOUNT);
        }
        return stoppedAiInfo;
    }

    /**
     * Loads all names of active AI in the game.
     */
    public void loadActiveAi() {
        this.activeAi = aiStateHandler.getActiveAiNames();
    }

    /**
     * Resets the scheduler to its initial state.
     */
    public void reset() {
        this.activeAi = null;
        this.counter = 0;
    }

    /**
     * updates the active AI list and the current counter.
     */
    private void updateActiveAi() {
        int formerActiveAiLength = this.activeAi.size();
        loadActiveAi();
        //if the current AI stopped
        if (formerActiveAiLength > activeAi.size()) {
            //if it was the last AI in the list increment counter, otherwise it stays the same
            if (counter > activeAi.size() - 1) {
                incrementCounter();
            }
        } else {
            incrementCounter();
        }
    }

    /**
     * Increments the counter used to track the current AI entity. Wraps around if it exceeds the size of the active AI list.
     */
    private void incrementCounter() {
        counter++;
        if (counter > activeAi.size() - 1) {
            counter = 0;
        }
    }

    /**
     * Returns the current counter.
     * the counter is used to track which AI is next in line to execute.
     * @return the counter value
     */
    public int getCounter() {
        return this.counter;
    }
}
