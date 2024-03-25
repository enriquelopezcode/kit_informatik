package kit.organiser.command;

import kit.organiser.exceptions.AccessAmountChangeException;
import kit.organiser.model.FileManager;

/**
 * Represents a command to change the access amount of a document within a folder.
 * @author ukgyh
 */
final class ChangeCommand implements Command {
    private static final String SUCCESS_MESSAGE_FORMAT = "Change %d to %d for %s";
    private static final String INVALID_FOLDER_ID_FORMAT = "invalid folder id %s";
    private static final String INVALID_ACCESS_AMOUNT_FORMAT = "invalid access amount: %s";
    private static final int NUMBER_OF_ARGUMENTS = 3;
    private static final int FOLDER_ID_INDEX = 0;
    private static final int DOCUMENT_NAME_INDEX = 1;
    private static final int NEW_ACCESS_AMOUNT_INDEX = 2;

    @Override
    public CommandResult execute(FileManager fileManager, String[] commandArguments) {

        int folderId;
        try {
            folderId = Integer.parseInt(commandArguments[FOLDER_ID_INDEX]);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_FOLDER_ID_FORMAT.formatted(commandArguments[FOLDER_ID_INDEX]));
        }

        String documentName = commandArguments[DOCUMENT_NAME_INDEX];

        String accessAmountString = commandArguments[NEW_ACCESS_AMOUNT_INDEX];
        int newAccessAmount;
        try {
            newAccessAmount = Integer.parseInt(accessAmountString);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ACCESS_AMOUNT_FORMAT.formatted(accessAmountString));
        }

        int oldAccessAmount;
        try {
            oldAccessAmount = fileManager.changeDocumentAccessAmount(folderId, documentName, newAccessAmount);
        } catch (AccessAmountChangeException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }
        String successMessage = SUCCESS_MESSAGE_FORMAT.formatted(oldAccessAmount, newAccessAmount, documentName);
        return new CommandResult(CommandResultType.SUCCESS, successMessage);
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
