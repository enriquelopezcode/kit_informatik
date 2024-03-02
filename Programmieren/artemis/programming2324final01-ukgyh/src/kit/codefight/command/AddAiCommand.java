package kit.codefight.command;

import kit.codefight.exceptions.AiCreationException;
import kit.codefight.exceptions.ArgumentInvalidException;
import kit.codefight.exceptions.InstructionCreationException;
import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;
import kit.codefight.model.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

/**
 * command that registers an AI into the game
 * @author ukgyh
 */
final class AddAiCommand implements Command {
    private static final int ARGUMENT_AMOUNT = 2;
    private static final int AI_NAME_INDEX = 0;
    private static final int AI_ARGUMENTS_INDEX = 1;
    private static final int INSTRUCTION_NAME_OFFSET = 0;
    private static final int INSTRUCTION_ARGUMENT_A_OFFSET = 1;
    private static final int INSTRUCTION_ARGUMENT_B_OFFSET = 2;
    private static final int INSTRUCTION_ARGUMENT_AMOUNT = 3;
    private static final String INFO_TEXT = "registers a new AI into the application."
            + " The format is: add-ai [name] [instructions]. Instructions are separated by commas and each instruction has two arguments.";
    private static final String FORBIDDEN_SYMBOL = " ";
    private static final String FORBIDDEN_SYMBOL_ERROR = "name can't contain '%s'";
    private static final String INSTRUCTION_AMOUNT_INVALID_ERROR = "each AI instruction must have exactly two arguments";
    private static final String ARGUMENT_INTEGER_INVALID_ERROR = "instruction argument must be a valid integer";
    private static final String NO_VALID_FIRST_INSTRUCTION_ERROR = "AI must contain at least one valid first instruction";
    private static final String INSTRUCTION_SEPARATION_SYMBOL = ",";
    private static final boolean REQUIRES_GAME_PHASE = true;
    private static final GamePhase REQUIRED_GAME_PHASE = GamePhase.INITIALIZATION;


    @Override
    public kit.codefight.command.CommandResult execute(GameEngine gameEngine, String[] commandArguments) {

        String aiName;
        List<Instruction> startingInstructions;

        try {
            aiName = parseName(commandArguments[AI_NAME_INDEX]);
        } catch (ArgumentInvalidException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        try {
            startingInstructions = parseInstructions(gameEngine, aiName, commandArguments[AI_ARGUMENTS_INDEX]);
        } catch (InstructionCreationException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        try {
            gameEngine.addAi(aiName, startingInstructions);
        } catch (AiCreationException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        return new CommandResult(CommandResultType.SUCCESS, aiName);
    }


    private List<Instruction> parseInstructions(GameEngine gameEngine, String owner, String instr) throws InstructionCreationException {
        boolean containsValidFirstInstruction = false;

        List<Instruction> instructionList = new ArrayList<>();
        String[] splitInstructionString = instr.split(INSTRUCTION_SEPARATION_SYMBOL);

        //checks if there are three arguments specified for every instruction
        if (splitInstructionString.length % INSTRUCTION_ARGUMENT_AMOUNT != 0) {
            throw new InstructionCreationException(INSTRUCTION_AMOUNT_INVALID_ERROR);
        }

        //parses the instruction name, argumentA and argumentB for every instruction in the string
        for (int i = 0; i < splitInstructionString.length; i += INSTRUCTION_ARGUMENT_AMOUNT) {
            String instructionName = splitInstructionString[i + INSTRUCTION_NAME_OFFSET];

            int argumentA;
            int argumentB;

            try {
                argumentA = Integer.parseInt(splitInstructionString[i + INSTRUCTION_ARGUMENT_A_OFFSET]);
                argumentB = Integer.parseInt(splitInstructionString[i + INSTRUCTION_ARGUMENT_B_OFFSET]);

            } catch (NumberFormatException e) {
                throw new InstructionCreationException(ARGUMENT_INTEGER_INVALID_ERROR);
            }


            Instruction instruction = gameEngine.createInstructionsByString(instructionName, argumentA, argumentB, owner);
            instructionList.add(instruction);


            if (instruction.isValidFirstInstruction()) {
                containsValidFirstInstruction = true;
            }
        }

        if (containsValidFirstInstruction) {
            return instructionList;
        }

        throw new InstructionCreationException(NO_VALID_FIRST_INSTRUCTION_ERROR);
    }

    private String parseName(String name) throws ArgumentInvalidException {

        if (name.contains(FORBIDDEN_SYMBOL)) {
            throw new ArgumentInvalidException(FORBIDDEN_SYMBOL_ERROR.formatted(FORBIDDEN_SYMBOL));
        }
        return name;
    }


    @Override
    public boolean isValidArgumentAmount(int argumentAmount) {
        return argumentAmount == ARGUMENT_AMOUNT;
    }

    @Override
    public boolean requiresGamePhase() {
        return REQUIRES_GAME_PHASE;
    }


    @Override
    public GamePhase getRequiredGamePhase() {
        return REQUIRED_GAME_PHASE;
    }

    @Override
    public String getInfoText() {
        return INFO_TEXT;
    }
}



