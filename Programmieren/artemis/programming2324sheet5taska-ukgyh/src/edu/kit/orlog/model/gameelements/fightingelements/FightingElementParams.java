package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.GameElementParams;

/**
 * Class representing the parameters required for creating fighting elements.
 * This class extends {@link GameElementParams}, inheriting fields for the ally player ID, opponent player ID,
 * and the game instance.
 *
 * The {@code FightingElementParams} class can be used to instantiate fighting elements, providing
 * necessary information such as the players involved and the game context.
 * @author ukgyh
 */
public class FightingElementParams extends GameElementParams {
    /**
     * Constructor for FightingElementParams. Initializes a new set of parameters specifically for a fighting element.
     *
     * @param game The instance of the Game associated with the fighting element.
     * @param allyPlayer The identifier of the ally player owning the fighting element.
     * @param opponentPlayer The identifier of the opponent player related to the fighting element.
     */
    public FightingElementParams(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }
}
