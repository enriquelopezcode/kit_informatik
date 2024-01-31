package edu.kit.orlog.model.gameelements;

import edu.kit.orlog.model.Game;

/**
 * Abstract class representing a game element.
 * This class serves as a base for different types of game elements such as fighting elements
 * and god favors, which are part of the game mechanics.
 *
 * The class stores information about the players involved (ally and opponent), the active state
 * of the game element, and a reference to the game instance. It provides the basic functionalities
 * common to all game elements, including the ability to check and change the active state and access
 * player information.
 *
 * Classes inheriting from GameElement are required to implement the {@code applyEffect} method,
 * defining the specific effect of the game element.
 * @author ukgyh
 */
public abstract class GameElement {
    protected int allyPlayer;
    protected int opponentPlayer;
    protected boolean active;
    protected Game game;


    /**
     * Constructs a new GameElement instance.
     *
     * @param game The instance of the Game to which this element belongs.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     */
    public GameElement(Game game, int allyPlayer, int opponentPlayer) {
        this.allyPlayer = allyPlayer;
        this.opponentPlayer = opponentPlayer;
        this.game = game;
    }

    /**
     * Abstract method to apply the specific effect of the game element.
     * Implementations will define the actual effect logic.
     */
    public abstract void applyEffect();

    /**
     * Checks whether the game element is active and should apply effects.
     * @return true if the game element is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active state of the game element.
     *
     * @param active The new active state of the element.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the identifier of the ally player.
     *
     * @return The identifier of the ally player.
     */
    public int getAllyPlayer() {
        return allyPlayer;
    }

    /**
     * Gets the identifier of the opponent player.
     *
     * @return The identifier of the opponent player.
     */
    public int getOpponentPlayer() {
        return opponentPlayer;
    }


}
