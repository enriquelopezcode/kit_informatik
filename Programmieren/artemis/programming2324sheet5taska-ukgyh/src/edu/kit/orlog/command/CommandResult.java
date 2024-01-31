package edu.kit.orlog.command;

/**
 * This class represents the result of a command.
 * It contains a message and a type.
 * The type is used to determine whether the command was successful or not.
 * @author ukgyh
 */
public class CommandResult {
    private final CommandResultType type;
    private final String message;

    /**
     * Constructs a new CommandResult.
     *
     * @param type          the type of the result
     * @param resultMessage the message of the result. May be {@code null} to indicate that there is no message.
     */
    public CommandResult(CommandResultType type, String resultMessage) {
        this.message = resultMessage;
        this.type = type;
    }

    /**
     * Returns the message of the result. Might be {@code null} indicating that there was no result message.
     *
     * @return the message of the result.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the type of the result.
     *
     * @return the type of the result.
     */
    public CommandResultType getType() {
        return type;
    }
}


