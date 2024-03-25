package kit.organiser.command;

import kit.organiser.model.FileManager;

/**
 * This interface represents an executable kit.organiser.command.
 *
 * @author Programmieren-Team
 */
public interface Command {
    /**
     * Executes the kit.organiser.command.
     *
     * @param fileManager       the kit.organiser.model to execute the kit.organiser.command on
     * @param commandArguments the arguments of the kit.organiser.command
     * @return the result of the kit.organiser.command
     */
    CommandResult execute(FileManager fileManager, String[] commandArguments);

    /**
     * Retrieves the number of arguments expected by this command.
     *
     * @return An integer representing the number of arguments this command
     *         expects.
     */
    int getNumberOfArguments();
}
