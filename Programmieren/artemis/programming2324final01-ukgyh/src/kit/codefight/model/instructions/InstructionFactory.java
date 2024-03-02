package kit.codefight.model.instructions;

import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.model.ai.AiStateHandler;
import kit.codefight.model.memory.MemoryStateHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating instructions based on their ID or name.
 * @author ukgyh
 */
public final class InstructionFactory {
    private static final String STOP_NAME = "STOP";
    private static final int STOP_ID = 0;
    private static final String RELATIVE_MOVE_NAME = "MOV_R";
    private static final int RELATIVE_MOVE_ID = 1;
    private static final String INDIRECT_MOVE_NAME = "MOV_I";
    private static final int INDIRECT_MOVE_ID = 2;
    private static final String SIMPLE_ADD_NAME = "ADD";
    private static final int SIMPLE_ADD_ID = 3;
    private static final String RELATIVE_ADD_NAME = "ADD_R";
    private static final int RELATIVE_ADD_ID = 4;
    private static final String JUMP_NAME = "JMP";
    private static final int JUMP_ID = 5;
    private static final String CONDITIONAL_JUMP_NAME = "JMZ";
    private static final int CONDITIONAL_JUMP_ID = 6;
    private static final String CONDITIONAL_SKIP_NAME = "CMP";
    private static final int CONDITIONAL_SKIP_ID = 7;
    private static final String SWAP_NAME = "SWAP";
    private static final int SWAP_ID = 8;
    private static final String INSTRUCTION_INVALID_ERROR = "instruction %s does not exist";
    private static final String INSTRUCTION_ID_INVALID_ERROR = "instruction id %d does not exist";


    private final AiStateHandler aiStateHandler;
    private final MemoryStateHandler memoryStateHandler;
    private final Map<String, Integer> instructionKeyMap;

    /**
     * Constructs new Instruction Factory.
     * @param memoryStateHandler the MemoryStateHandler to be used
     * @param aiStateHandler the AiStateHandler to be used
     */
    public InstructionFactory(MemoryStateHandler memoryStateHandler, AiStateHandler aiStateHandler) {
        this.memoryStateHandler = memoryStateHandler;
        this.aiStateHandler = aiStateHandler;
        instructionKeyMap = new HashMap<>();
        initInstructionKeyMap();
    }

    /**
     * Creates an instruction based on its ID.
     * @param id the ID of the instruction
     * @param argA the first argument
     * @param argB the second argument
     * @param lastEditorName the name of the last editor
     * @return the created instruction
     * @throws InstructionCreationException if the instruction ID is invalid
     */
    public Instruction createInstructionByID(int id, int argA, int argB, String lastEditorName) throws InstructionCreationException {
        return switch (id) {
            case RELATIVE_MOVE_ID -> new RelativeMoveInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case INDIRECT_MOVE_ID -> new IndirectMoveInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case RELATIVE_ADD_ID -> new RelativeAddInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case SIMPLE_ADD_ID -> new SimpleAddInstruction(aiStateHandler, argA, argB, lastEditorName);
            case JUMP_ID -> new JumpInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case CONDITIONAL_JUMP_ID -> new ConditionalJumpInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case CONDITIONAL_SKIP_ID -> new ConditionalSkipInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case SWAP_ID -> new SwapInstruction(aiStateHandler, memoryStateHandler, argA, argB, lastEditorName);
            case STOP_ID -> new StopInstruction(aiStateHandler, argA, argB, lastEditorName);
            default -> throw new InstructionCreationException(INSTRUCTION_ID_INVALID_ERROR.formatted(id));
        };
    }

    /**
     * Creates an instruction based on its name.
     * @param instruction the name of the instruction
     * @param argA the first argument
     * @param argB the second argument
     * @param owner the owner of the instruction
     * @return the created instruction
     * @throws InstructionCreationException if the instruction name is invalid
     */
    public Instruction createInstructionByString(String instruction, int argA, int argB, String owner) throws InstructionCreationException {

        if (!instructionKeyMap.containsKey(instruction)) {
            throw new InstructionCreationException(INSTRUCTION_INVALID_ERROR.formatted(instruction));
        }

        return createInstructionByID(instructionKeyMap.get(instruction), argA, argB, owner);
    }

    private void initInstructionKeyMap() {
        this.instructionKeyMap.put(STOP_NAME, STOP_ID);
        this.instructionKeyMap.put(RELATIVE_MOVE_NAME, RELATIVE_MOVE_ID);
        this.instructionKeyMap.put(INDIRECT_MOVE_NAME, INDIRECT_MOVE_ID);
        this.instructionKeyMap.put(RELATIVE_ADD_NAME, RELATIVE_ADD_ID);
        this.instructionKeyMap.put(SIMPLE_ADD_NAME, SIMPLE_ADD_ID);
        this.instructionKeyMap.put(JUMP_NAME, JUMP_ID);
        this.instructionKeyMap.put(CONDITIONAL_JUMP_NAME, CONDITIONAL_JUMP_ID);
        this.instructionKeyMap.put(CONDITIONAL_SKIP_NAME, CONDITIONAL_SKIP_ID);
        this.instructionKeyMap.put(SWAP_NAME, SWAP_ID);
    }
}
