package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.GameElementParams;


/**
 * Class representing the parameters required for creating a GodFavor.
 * This class extends GameElementParams and adds parameter 'level' specific to god favors.
 *
 * God favors in the game can have different levels, which may affect their cost or impact when used.
 * This class encapsulates all the necessary information to create a specific god favor,
 * including the game instance, the level of the god favor, and the ally and opponent player identifiers.
 *
 * Instances of this class are used to pass the required parameters to the constructors of god favors
 * when they are created by the GodFavorFactory.
 *
 * @author ukgyh
 */
public class GodFavorParams extends GameElementParams {
    private final int level;
    /**
     * Constructs a new instance of GodFavorParams.
     *
     * @param game The instance of the Game associated with the god favor.
     * @param level The level of the god favor, which may influence its effects or cost.
     * @param allyPlayer The identifier of the ally player associated with the god favor.
     * @param opponentPlayer The identifier of the opponent player in context of the god favor.
     */
    public GodFavorParams(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
        this.level = level;
    }

    /**
     * Gets the level of the god favor.
     *
     * @return The level of the god favor.
     */
    public int getLevel() {
        return level;
    }


}
