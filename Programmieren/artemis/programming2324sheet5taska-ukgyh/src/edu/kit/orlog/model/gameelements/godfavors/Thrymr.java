package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;

/**
 * Represents the Thrymr god favor.
 * The Thrymr god favor is characterized by its ability to reduce the level of the opponent's god favors.
 * This effect is applied multiple times based on the level of the Thrymr god favor.
 * @author ukgyh
 */
public class Thrymr extends GodFavor {
    private static final String GOD_FAVOR_NAME = "Thrymr";
    private static final int INITIAL_COST = 3;
    private static final int UPGRADE_COST = 3;
    private static final int PRIORITY = 1;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new instance of the Thrymr god favor.
     *
     * @param game The game instance.
     * @param level The level of the god favor.
     * @param allyPlayer The ally player's identifier.
     * @param opponentPlayer The opponent player's identifier.
     */
    public Thrymr(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }

    /**
     * Applies the effect of Thrymr, reducing the level of the opponent's god favors.
     * The effect is applied multiple times based on the level of the Thrymr god favor.
     */
    @Override
    public void applyEffect() {
        for (int i = 0; i < level; i++) {
            game.reduceGodFavorLevel(opponentPlayer);
        }
    }

    /**
     * Returns the name of the god favor.
     * @return The name of the god favor.
     */
    public String getName() {
        return name;
    }
}
