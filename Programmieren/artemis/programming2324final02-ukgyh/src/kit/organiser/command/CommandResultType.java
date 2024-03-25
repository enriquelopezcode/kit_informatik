package kit.organiser.command;

/**
 * This enum represents the types that a result of a kit.organiser.command can be.
 *
 * @author Programmieren-Team
 */
public enum CommandResultType {

    /**
     * The kit.organiser.command was executed successfully.
     */
    SUCCESS,
    /**
     * An error occured during processing the kit.organiser.command.
     */
    FAILURE
}
