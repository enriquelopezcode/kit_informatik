package kit.codefight.model.memory;

import kit.codefight.exceptions.GameExecutionException;
import kit.codefight.exceptions.MemoryOutOfBoundsException;
import kit.codefight.exceptions.MissingAiException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.instructions.Instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles the display of the memory and AI information for the user interface.
 * @author ukgyh
 */
public final class MemoryDisplayHandler {
    private static final String MEMORY_OUT_OF_BOUNDS_ERROR = "Memory out of bounds";
    private static final String AI_NOT_ACTIVE_ERROR = "AI %s is not in the current game";
    private static final String RUNNING_FORMAT = "%s (RUNNING@%d)";
    private static final String STOPPED_FORMAT = "%s (STOPPED@%d)";
    private static final String NEXT_INSTRUCTION_FORMAT = "Next Command: %s|%d|%d @%d";
    private static final String RUNNING_AI_FORMAT = "Running AIs: ";
    private static final String STOPPED_AI_FORMAT = "Stopped AIs: ";
    private static final String STANDARD_SEPARATION_SYMBOL = " ";
    private static final String GAME_INFO_SEPARATION_SYMBOL = ", ";
    private static final String NUMBER_INSTRUCTION_SEPARATION_SYMBOL = ":";
    private static final String ENTRY_SEPARATION_SYMBOL = " | ";
    private static final String PADDING_SYMBOL_1 = "%";
    private static final String PADDING_SYMBOL_2 = "s";
    private static final int UNUSED_INSTRUCTION_SYMBOL_INDEX = 0;
    private static final int MEMORY_SEGMENT_SYMBOL_INDEX = 1;
    private static final int NEXT_INSTRUCTION_SYMBOL_INDEX = 2;
    private static final int LATER_INSTRUCTION_SYMBOL_INDEX = 3;
    private static final int MEMORY_SEGMENT_LENGTH = 10;
    private static final int MINIMUM_INDEX = 0;
    private static final int UNREACHABLE_INDEX = -1;
    private static final int ARG_A_DATA_INDEX = 0;
    private static final int ARG_B_DATA_INDEX = 1;
    private static final int INSTRUCTION_NAME_DATA_INDEX = 2;
    private static final int INSTRUCTION_POSITION_DATA_INDEX = 3;
    private static final int INSTRUCTION_SYMBOL_DATA_INDEX = 4;
    private static final int STOP_INSTRUCTION_EXTRA = 1;
    private final MemoryStateHandler memoryStateHandler;
    private final AiStateHandler aiStateHandler;
    private String unusedInstructionSymbol;
    private String memorySegmentSymbol;
    private String nextInstructionSymbol;
    private String laterInstructionSymbol;
    private List<String> aiSymbols;

    /**
     * Represents the result of the AI information retrieval.
     * @param aiNames The names of the AI entities.
     * @param nextInstruction The index of the next instruction to be executed.
     * @param laterInstructionPointers The indices of the next instructions of other AIs.
     */
    public record AiInfoResult(List<String> aiNames, int nextInstruction, Set<Integer> laterInstructionPointers) { }

    /**
     * Constructs a new MemoryDisplayHandler with the specified memory and AI state handlers.
     * @param memoryStateHandler The handler responsible for managing memory states.
     * @param aiStateHandler The handler responsible for managing AI states.
     */
    public MemoryDisplayHandler(MemoryStateHandler memoryStateHandler, AiStateHandler aiStateHandler) {
        this.memoryStateHandler = memoryStateHandler;
        this.aiStateHandler = aiStateHandler;
    }

    /**
     * Initializes the memory display handler.
     * @param gameSymbols the symbols used in the memory display
     * @param aiSymbols the AI specific symbols used in the memory display
     */
    public void initialize(String[] gameSymbols, List<String> aiSymbols) {
        unusedInstructionSymbol = gameSymbols[UNUSED_INSTRUCTION_SYMBOL_INDEX];
        memorySegmentSymbol = gameSymbols[MEMORY_SEGMENT_SYMBOL_INDEX];
        nextInstructionSymbol = gameSymbols[NEXT_INSTRUCTION_SYMBOL_INDEX];
        laterInstructionSymbol = gameSymbols[LATER_INSTRUCTION_SYMBOL_INDEX];
        this.aiSymbols = new ArrayList<>(aiSymbols);
    }

    /**
     * Returns a string representation of the memory display.
     * @param counter index of AI that is next to execute in the activeAi List.
     * @return A string representation of the memory display.
     * @throws GameExecutionException If there is an error constructing the memory display.
     */
    public List<String> getMemoryDisplay(int counter) throws GameExecutionException {
        AiInfoResult aiInfo = getAiInfo(counter);
        int nextInstruction = aiInfo.nextInstruction();
        Set<Integer> laterAiInstructionPointers = aiInfo.laterInstructionPointers();
        //list of all AI names, active or stopped
        List<String> aiNames = aiInfo.aiNames;

        int memorySize = memoryStateHandler.getMemorySize();
        List<String> memoryDisplay = new ArrayList<>();

        for (int i = 0; i < memorySize; i++) {
            if (i == nextInstruction) {
                memoryDisplay.add(nextInstructionSymbol);
                continue;
            } else if (laterAiInstructionPointers.contains(i)) {
                memoryDisplay.add(laterInstructionSymbol);
                continue;
            }

            Instruction instruction;
            try {
                instruction = memoryStateHandler.getInstructionAtIndex(i);
            } catch (MemoryOutOfBoundsException e) {
                throw new GameExecutionException(e.getMessage(), e);
            }
            if (instruction.isAIBomb() && instruction.getLastEditorName() != null) {
                String bombOwner = instruction.getLastEditorName();
                int bombSymbolIndex = aiNames.indexOf(bombOwner) * 2 + 1;
                memoryDisplay.add(aiSymbols.get(bombSymbolIndex));

            } else if (instruction.getLastEditorName() != null) {
                String instructionOwner = instruction.getLastEditorName();
                int ownerIndex = Math.max(aiNames.indexOf(instructionOwner) * 2, MINIMUM_INDEX);
                memoryDisplay.add(aiSymbols.get(ownerIndex));

            } else {
                memoryDisplay.add(unusedInstructionSymbol);
            }
        }
        return memoryDisplay;
    }

    /**
     * Returns a string representation of the memory display with a segment of instructions.
     * @param counter index of AI that is next to execute in the activeAi List.
     * @param startOfSegment the start index of the segment
     * @return A string representation of the memory display.
     * @throws MemoryOutOfBoundsException If the start index is out of bounds.
     * @throws GameExecutionException If there is an error constructing the memory display.
     */
    public List<String> getMemoryDisplay(int counter, int startOfSegment) throws MemoryOutOfBoundsException, GameExecutionException {
        validateSegmentStart(startOfSegment);

        List<String> standardMemoryDisplay = getMemoryDisplay(counter);
        StringBuilder instructionStringBuilder = new StringBuilder();

        int instructionAmount = Math.min(MEMORY_SEGMENT_LENGTH, standardMemoryDisplay.size());

        List<List<String>> instructionData = gatherInstructionData(startOfSegment, instructionAmount, standardMemoryDisplay);

        List<String> instructionName = instructionData.get(INSTRUCTION_NAME_DATA_INDEX);
        List<String> instructionArgA = instructionData.get(ARG_A_DATA_INDEX);
        List<String> instructionArgB = instructionData.get(ARG_B_DATA_INDEX);
        List<String> instructionSymbols = instructionData.get(INSTRUCTION_SYMBOL_DATA_INDEX);
        List<String> instructionIndex = instructionData.get(INSTRUCTION_POSITION_DATA_INDEX);

        // Find maximum lengths
        int maxArgALength = findMaxStringLength(instructionData.get(ARG_A_DATA_INDEX));
        int maxArgBLength = findMaxStringLength(instructionData.get(ARG_B_DATA_INDEX));
        int maxNameLength = findMaxStringLength(instructionData.get(INSTRUCTION_NAME_DATA_INDEX));
        int maxIndexLength = findMaxStringLength(instructionData.get(INSTRUCTION_POSITION_DATA_INDEX));

        String nameFormat = PADDING_SYMBOL_1 + maxNameLength + PADDING_SYMBOL_2;
        String argAFormat = PADDING_SYMBOL_1 + maxArgALength + PADDING_SYMBOL_2;
        String argBFormat = PADDING_SYMBOL_1 + maxArgBLength + PADDING_SYMBOL_2;
        String indexFormat = PADDING_SYMBOL_1 + maxIndexLength + PADDING_SYMBOL_2;

        for (int i = 0; i < instructionAmount; i++) {
            instructionStringBuilder.append(instructionSymbols.get(i)).append(STANDARD_SEPARATION_SYMBOL);

            //adding left padding based on maximum lengths
            String index = String.format(indexFormat, instructionIndex.get(i));
            instructionStringBuilder.append(index);
            instructionStringBuilder.append(NUMBER_INSTRUCTION_SEPARATION_SYMBOL);
            instructionStringBuilder.append(STANDARD_SEPARATION_SYMBOL);

            String name = String.format(nameFormat, instructionName.get(i));
            instructionStringBuilder.append(name).append(ENTRY_SEPARATION_SYMBOL);

            String argA = String.format(argAFormat, instructionArgA.get(i));
            instructionStringBuilder.append(argA).append(ENTRY_SEPARATION_SYMBOL);

            String argB = String.format(argBFormat, instructionArgB.get(i));
            instructionStringBuilder.append(argB);

            if (i != instructionAmount - 1) {
                instructionStringBuilder.append(System.lineSeparator());
            }
        }
        List<String> finalMemoryDisplay = insertSegmentSymbols(standardMemoryDisplay, instructionAmount, startOfSegment);
        finalMemoryDisplay.add(System.lineSeparator());
        finalMemoryDisplay.add(instructionStringBuilder.toString());
        return finalMemoryDisplay;
    }

    private AiInfoResult getAiInfo(int counter) throws GameExecutionException {
        //default if active AI empty
        int nextInstruction = UNREACHABLE_INDEX;

        Set<Integer> laterAiInstructionPointers = new HashSet<>();
        List<String> activeAi = aiStateHandler.getActiveAiNames();
        List<String> orderedAiNames = aiStateHandler.getOrderedAiNames();

        for (int i = 0; i < activeAi.size(); i++) {
            int aiPointer;
            try {
                aiPointer = aiStateHandler.getAiPointer(activeAi.get(i));
            } catch (MissingAiException e) {
                throw new GameExecutionException(e.getMessage(), e);
            }

            if (i == counter) {
                nextInstruction = aiPointer;
            } else {
                laterAiInstructionPointers.add(aiPointer);
            }
        }
        return new AiInfoResult(orderedAiNames, nextInstruction, laterAiInstructionPointers);
    }

    /**
     * Returns the display information of the status of a given AI.
     * @param aiName the name of the AI
     * @return the display information of the given AI
     * @throws MissingAiException if the AI is not registered
     * @throws GameExecutionException if the AI display cannot be created
     */
    public String getAiDisplay(String aiName) throws MissingAiException, GameExecutionException {
        //if AI is not in the game, an exception is thrown here
        int counter = aiStateHandler.getAiCounter(aiName);

        if (aiStateHandler.checkForActiveAi(aiName)) {
            String aiInfo = RUNNING_FORMAT.formatted(aiName, counter);

            int aiPointer = aiStateHandler.getAiPointer(aiName);

            Instruction nextInstruction;
            try {
                nextInstruction = memoryStateHandler.getInstructionAtIndex(aiPointer);
            } catch (MemoryOutOfBoundsException e) {
                throw new GameExecutionException(e.getMessage(), e);
            }
            String instructionName = nextInstruction.getName();
            int argumentA = nextInstruction.getArgumentA();
            int argumentB = nextInstruction.getArgumentB();

            String instructionInfo = NEXT_INSTRUCTION_FORMAT.formatted(instructionName, argumentA, argumentB, aiPointer);
            return aiInfo + System.lineSeparator() + instructionInfo;

        } else if (aiStateHandler.checkForStoppedAi(aiName)) {
            //since stop instruction should be included and AI is stopped we need to add to the counter
            return STOPPED_FORMAT.formatted(aiName, counter + STOP_INSTRUCTION_EXTRA);
        }
        throw new MissingAiException(AI_NOT_ACTIVE_ERROR);
    }

    /**
     * Returns the display information of the game and all AIs.
     * @return the display information of the game
     */
    public String getGameInfo() {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> activeAiNames = aiStateHandler.getActiveAiNames();
        List<String> orderedAiNames = aiStateHandler.getOrderedAiNames();


        if (!activeAiNames.isEmpty()) {
            stringBuilder.append(RUNNING_AI_FORMAT);

            String activeAisJoined = String.join(GAME_INFO_SEPARATION_SYMBOL, activeAiNames);
            stringBuilder.append(activeAisJoined);

            orderedAiNames.removeAll(activeAiNames);
        }

        if (!orderedAiNames.isEmpty()) {

            if (!activeAiNames.isEmpty()) {
                stringBuilder.append(System.lineSeparator());
            }
            stringBuilder.append(STOPPED_AI_FORMAT);

            String stoppedAisJoined = String.join(GAME_INFO_SEPARATION_SYMBOL, orderedAiNames);
            stringBuilder.append(stoppedAisJoined);
        }

        return stringBuilder.toString();
    }

    private int findMaxStringLength(List<String> strings) {
        int maxLength = 0;
        for (String str : strings) {
            if (str.length() > maxLength) {
                maxLength = str.length();
            }
        }
        return maxLength;
    }

    private void validateSegmentStart(int startOfSegment) throws MemoryOutOfBoundsException {
        if (startOfSegment < 0 || startOfSegment > memoryStateHandler.getMemorySize() - 1) {
            throw new MemoryOutOfBoundsException(MEMORY_OUT_OF_BOUNDS_ERROR);
        }
    }

    private List<List<String>> gatherInstructionData(int startSegment, int instrAmount, List<String> memory) throws GameExecutionException {
        List<String> instructionName = new ArrayList<>();
        List<String> instructionArgA = new ArrayList<>();
        List<String> instructionArgB = new ArrayList<>();
        List<String> instructionIndex = new ArrayList<>();
        List<String> instructionSymbols = new ArrayList<>();

        //collecting instruction data
        for (int i = startSegment; i < startSegment + instrAmount; i++) {
            Instruction instruction;
            try {
                instruction = memoryStateHandler.getInstructionAtIndex(i);
            } catch (MemoryOutOfBoundsException e) {
                throw new GameExecutionException(e.getMessage(), e);
            }
            instructionName.add(instruction.getName());
            instructionArgA.add(String.valueOf(instruction.getArgumentA()));
            instructionArgB.add(String.valueOf(instruction.getArgumentB()));

            int circularIndex = memoryStateHandler.calculateCircularIndex(i);
            instructionIndex.add(String.valueOf(circularIndex));
            instructionSymbols.add(memory.get(circularIndex));
        }
        return Arrays.asList(instructionArgA, instructionArgB, instructionName, instructionIndex, instructionSymbols);
    }

    private List<String> insertSegmentSymbols(List<String> memoryDisplay, int instructionAmount, int startOfSegment) {
        memoryDisplay.add(startOfSegment, memorySegmentSymbol);
        int endSegmentIndex = startOfSegment + instructionAmount + 1;

        // Adjust endOfSegmentIndex to be the full memory size if it is at the end of the memory, else it wraps around
        int checkEndSegmentIndex = memoryStateHandler.calculateCircularIndex(endSegmentIndex, memoryDisplay.size());
        endSegmentIndex = (checkEndSegmentIndex == 0) ? memoryDisplay.size() : checkEndSegmentIndex;
        memoryDisplay.add(endSegmentIndex, memorySegmentSymbol);

        return memoryDisplay;
    }
}
