package edu.kit.orlog.model.gameelements;

import edu.kit.orlog.model.Game;


/**
 * Class representing the parameters required for creating game elements.
 * This class serves as a container for the necessary information to instantiate game elements, such as
 * fighting elements and god favors. It holds the identifiers for the ally player and opponent player,
 * as well as a reference to the game instance.
 *
 * This class simplifies the process of passing multiple parameters to the constructors of game elements
 * and ensures that all necessary information is available when creating new instances of game elements.
 * The class provides getters for each of the parameters, allowing easy access to the ally player ID,
 * opponent player ID, and the associated game object.
 *
 * Classes such as FightingElementParams and GodFavorParams can inherit from GameElementParams
 * to further specify parameters unique to their respective game elements.
 * @author ukgyh
 */
public class GameElementParams {
    protected final int allyPlayer;
    protected final int opponentPlayer;
    protected final Game game;

    /**
     * Initializes a new set of parameters for a game element.
     * @param game the game object for which to create the game element.
     * @param allyPlayer the player ID of the player who owns the game element.
     * @param opponentPlayer the player ID of the opponent of the player who owns the game element.
     */
    public GameElementParams(Game game, int allyPlayer, int opponentPlayer) {
        this.allyPlayer = allyPlayer;
        this.opponentPlayer = opponentPlayer;
        this.game = game;
    }

    /**
     * Gets the identifier of the ally player owning the game element.
     * @return the player ID of the owner of the element
     */
    public int getAllyPlayer() {
        return allyPlayer;
    }

    /**
     * Gets the identifier of the opponent player related to the game element.
     * @return the player ID of the opponent player
     */
    public int getOpponentPlayer() {
        return opponentPlayer;
    }

    /**
     * Gets the Game instance associated with the game element.
     * @return the game object for which to create the game element
     */
    public Game getGame() {
        return game;
    }
}
