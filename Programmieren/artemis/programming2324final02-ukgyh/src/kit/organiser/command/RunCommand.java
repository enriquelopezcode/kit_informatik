package kit.organiser.command;

import kit.organiser.exceptions.MissingFolderException;
import kit.organiser.model.FileManager;

/**
 * Represents the run command that automatically organises a folder into directories.
 * @author ukgyh
 */
final class RunCommand implements Command {
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int ID_INDEX = 0;
    private static final String INVALID_FOLDER_ID_FORMAT = "invalid folder id %s";

    @Override
    public CommandResult execute(FileManager fileManager, String[] commandArguments) {
        String idString = commandArguments[ID_INDEX];

        int folderId;
        try {
            folderId = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_FOLDER_ID_FORMAT.formatted(idString));
        }

        //contains information about the organisation process and final file structure
        String orgInfo;
        try {
            orgInfo = fileManager.runOrganiser(folderId);
        } catch (MissingFolderException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        return new CommandResult(CommandResultType.SUCCESS, orgInfo);
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
