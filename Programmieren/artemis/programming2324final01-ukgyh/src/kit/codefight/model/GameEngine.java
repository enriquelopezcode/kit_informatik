package kit.codefight.model;

import kit.codefight.exceptions.AiCreationException;
import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.exceptions.InitializationModeChangeException;
import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MemoryOverloadException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.exceptions.StartingGameException;

import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.instructions.Instruction;
import kit.codefight.model.instructions.InstructionFactory;
import kit.codefight.model.memory.MemoryDisplayHandler;
import kit.codefight.model.memory.MemoryStateHandler;
import kit.codefight.model.memory.initialization.MemoryInitializer;

import java.util.List;

/**
 * handles the interaction between the game model and user interface.
 * @author ukgyh
 */
public final class GameEngine {
    private static final String MEMORY_DISPLAY_JOINER = "";
    private final MemoryDisplayHandler memoryDisplayHandler;
    private final MemoryInitializer memoryInitializer;
    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private final GameScheduler gameScheduler;
    private final InstructionFactory instructionFactory;
    private final int maxNumberAi;
    private GamePhase currentGamePhase;

    /**
     * Constructs a new GameEngine.
     *
     * @param memorySize the size of the memory
     * @param maxNumberAi the maximum number of AIs allowed in one game
     */
    public GameEngine(int memorySize, int maxNumberAi) {
        this.maxNumberAi = maxNumberAi;
        this.memoryStateHandler = new MemoryStateHandler(memorySize);
        this.aiStateHandler = new AiStateHandler(maxNumberAi, memorySize);

        this.instructionFactory = new InstructionFactory(memoryStateHandler, aiStateHandler);
        this.memoryInitializer = new MemoryInitializer(memoryStateHandler, aiStateHandler, instructionFactory);
        this.gameScheduler = new GameScheduler(memoryStateHandler, aiStateHandler);
        this.memoryDisplayHandler = new MemoryDisplayHandler(memoryStateHandler, aiStateHandler);
        this.currentGamePhase = GamePhase.INITIALIZATION;
    }

    /**
     * Changes the initialization mode of the memory.
     *
     * @param newMode the new mode
     * @param seed the seed for the new mode, null if not needed
     * @throws InitializationModeChangeException if the mode change is not possible
     */
    public void setInitializationMode(String newMode, Integer seed) throws InitializationModeChangeException {
        memoryInitializer.setInitializationMode(newMode, seed);
    }

    /**
     * Initializes the memory display handler.
     *
     * @param gameSymbols the symbols used in the memory display
     * @param aiSymbols the AI specific symbols used in the memory display
     */
    public void initializeMemoryDisplay(String[] gameSymbols, List<String> aiSymbols) {
        this.memoryDisplayHandler.initialize(gameSymbols, aiSymbols);
    }

    /**
     * Adds a new AI to the game.
     *
     * @param name the name of the AI
     * @param startingInstructions the starting instructions of the AI
     * @throws AiCreationException if the AI cannot be created
     */
    public void addAi(String name, List<Instruction> startingInstructions) throws AiCreationException {
        aiStateHandler.addAi(name, startingInstructions);
    }

    /**
     * Removes an AI from the game.
     *
     * @param aiName the name of the AI
     * @throws MissingAiException if the AI is not registered
     */
    public void removeAi(String aiName) throws MissingAiException {
        aiStateHandler.removeAi(aiName);
    }

    /**
     * Starts the game with the specified AIs.
     *
     * @param aiNames the names of the AIs in the game
     * @throws StartingGameException if the game cannot be started
     */
    public void startGame(String[] aiNames) throws StartingGameException {
        try {
            aiStateHandler.activateAi(aiNames);
        } catch (MissingAiException e) {
            throw new StartingGameException(e.getMessage(), e);
        }

        try {
            memoryInitializer.initializeMemory();
        } catch (MemoryOverloadException | MissingAiException | InstructionCreationException e) {
            throw new StartingGameException(e.getMessage(), e);
        }

        gameScheduler.loadActiveAi();
        this.currentGamePhase = GamePhase.RUNNING;
    }

    /**
     * Executes the next steps of AIs in the game.
     *
     * @param steps the number of steps to execute
     * @return information about AIs that stopped during the execution
     * @throws GameExecutionException if the game cannot be executed
     */
    public List<String[]> doNextSteps(int steps) throws GameExecutionException {
        return gameScheduler.doNextSteps(steps);
    }

    /**
     * Returns the current game phase.
     * @return the current game phase
     */
    public GamePhase getCurrentGamePhase() {
        return this.currentGamePhase;
    }

    /**
     * Returns information about the current initialization mode.
     * @return array with mode name and, if it exists, the mode seed
     */
    public String[] getCurrentModeInfo() {
        return memoryInitializer.getInitializationModeInfo();
    }

    /**
     * Creates an instruction by the given string.
     *
     * @param instr the name of the instruction
     * @param argA the first argument of the instruction
     * @param argB the second argument of the instruction
     * @param owner the owner / editor of the instruction
     * @return the created instruction
     * @throws InstructionCreationException if the instruction cannot be created
     */
    public Instruction createInstructionsByString(String instr, int argA, int argB, String owner) throws InstructionCreationException {
        return instructionFactory.createInstructionByString(instr, argA, argB, owner);
    }

    /**
     * Returns the maximum number of AIs allowed in one game.
     * @return the maximum number of AIs
     */
    public int getMaxNumberAi() {
        return  maxNumberAi;
    }

    /**
     * Returns the current memory display.
     * @return the current memory display
     * @throws GameExecutionException if the memory display cannot be created
     */
    public String getMemoryDisplay() throws GameExecutionException {
        int counter = gameScheduler.getCounter();
        return String.join(MEMORY_DISPLAY_JOINER, memoryDisplayHandler.getMemoryDisplay(counter));
    }

    /**
     * Returns the memory display starting from the given segment.
     * @param startOfSegment the start of the segment
     * @return the memory display starting from the given segment
     * @throws MemoryOutOfBoundsException if the segment is out of bounds
     * @throws GameExecutionException if the memory display cannot be created
     */
    public String getMemoryDisplay(int startOfSegment) throws MemoryOutOfBoundsException, GameExecutionException {
        int counter = gameScheduler.getCounter();
        return String.join(MEMORY_DISPLAY_JOINER, memoryDisplayHandler.getMemoryDisplay(counter, startOfSegment));
    }

    /**
     * Returns the display information of the status of a given AI.
     * @param aiName the name of the AI
     * @return the display information of the given AI
     * @throws MissingAiException if the AI is not registered
     * @throws GameExecutionException if the AI display cannot be created
     */
    public String getAiDisplay(String aiName) throws MissingAiException, GameExecutionException {
        return memoryDisplayHandler.getAiDisplay(aiName);
    }

    /**
     * Returns the display information of the game and all AIs.
     * @return the display information of the game
     */
    public String getGameInfo() {
        return memoryDisplayHandler.getGameInfo();
    }

    /**
     * Ends the current game and resets the game engine.
     */
    public void endGame() {
        memoryStateHandler.reset();
        aiStateHandler.reset();
        gameScheduler.reset();
        this.currentGamePhase = GamePhase.INITIALIZATION;
    }
}
