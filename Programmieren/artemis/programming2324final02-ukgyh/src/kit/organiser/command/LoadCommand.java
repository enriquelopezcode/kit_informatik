package kit.organiser.command;

import kit.organiser.exceptions.FolderCreationException;
import kit.organiser.io.TextFileParser;
import kit.organiser.model.Folder;
import kit.organiser.model.FolderFactory;
import kit.organiser.model.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a command to load a document folder from a file.
 * @author ukgyh
 */
final class LoadCommand implements Command {
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int FILE_PATH_INDEX = 0;
    private static final String SUCCESS_MESSAGE_FORMAT = "Loaded %s with id: %d";
    private static final String MISSING_FILE_MESSAGE = "file not found: %s";
    private static final String EMPTY_FILE_MESSAGE = "file is empty";
    private static final String SEPARATOR_SYMBOL = ",";

    @Override
    public CommandResult execute(FileManager fileManager, String[] commandArguments) {
        String fileName = commandArguments[FILE_PATH_INDEX];
        List<String[]> splitDocumentArguments = new ArrayList<>();

        //contains all lines of parsed file
        List<String> documentsArguments;
        try {
            documentsArguments = TextFileParser.parseFile(fileName);
        } catch (IOException e) {
            return new CommandResult(CommandResultType.FAILURE, MISSING_FILE_MESSAGE.formatted(fileName));
        }

        for (String argument : documentsArguments) {
            splitDocumentArguments.add(argument.split(SEPARATOR_SYMBOL));
        }

        if (splitDocumentArguments.isEmpty()) {
            return new CommandResult(CommandResultType.FAILURE, EMPTY_FILE_MESSAGE);
        }

        Folder folder;
        try {
            folder = FolderFactory.createFolder(splitDocumentArguments);
        } catch (FolderCreationException e) {
            return new CommandResult(CommandResultType.FAILURE, e.getMessage());
        }

        //id of folder in the file manager
        int folderId = fileManager.addFolder(folder);

        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(SUCCESS_MESSAGE_FORMAT.formatted(fileName, folderId));

        for (String documentArgument : documentsArguments) {
            joiner.add(documentArgument);
        }

        return new CommandResult(CommandResultType.SUCCESS, joiner.toString());
    }



    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
