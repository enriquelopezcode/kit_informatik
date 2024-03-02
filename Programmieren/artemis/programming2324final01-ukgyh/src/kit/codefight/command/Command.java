package kit.codefight.command;

import kit.codefight.model.GameEngine;
import kit.codefight.model.GamePhase;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 * @author ukgyh
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param gameEngine            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(GameEngine gameEngine, String[] commandArguments);

    /**
     * checks if the amount of arguments for the command are valid.
     * @param argumentAmount the number to be checked
     * @return true if the given number of arguments for the command is valid, false otherwise
     */
    boolean isValidArgumentAmount(int argumentAmount);

    /**
     * checks if the command can only be executed during a certain phase of the game.
     * @return true if the command requires a game phase to be executed, false otherwise
     */
    boolean requiresGamePhase();

    /**
     * returns the game phase that is required to execute the command.
     * @return the game phase required to run the command
     */
    GamePhase getRequiredGamePhase();

    /**
     * returns a text that documents what the command does when called.
     * @return info text about the command
     */
    String getInfoText();
}
