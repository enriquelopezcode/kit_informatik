package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;

/**
 * Represents the Thor god favor.
 * This class implements the specific effect of the Thor god favor, which involves dealing damage
 * to the opponent player. The damage inflicted is determined by a base damage value plus an additional
 * amount that scales with the god favor's level.
 * @author ukgyh
 */
public class Thor extends GodFavor {

    /**
     * The base damage value of the Thor god favor.
     */
    public static final int BASE_DAMAGE = 2;

    /**
     * The additional damage value of the Thor god favor, added with increased level.
     */
    public static final int UPGRADE_DAMAGE = 3;
    private static final String GOD_FAVOR_NAME = "Thor";

    private static final int INITIAL_COST = 4;
    private static final int UPGRADE_COST = 4;
    private static final int PRIORITY = 2;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new instance of the Thor god favor.
     *
     * @param game The game instance.
     * @param level The level of the god favor.
     * @param allyPlayer The ally player's identifier.
     * @param opponentPlayer The opponent player's identifier.
     */
    public Thor(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }

    /**
     * Applies the damage effect of Thor, reducing the health of the opponent player.
     * The damage is calculated based on the Thor god favor's level.
     */
    @Override
    public void applyEffect() {
        game.getPlayerStateHandler().applyHealthChangeToPlayer(-(BASE_DAMAGE + UPGRADE_DAMAGE * (level - 1)), opponentPlayer);
    }

    /**
     * Returns the name of the god favor.
     * @return The name of the god favor.
     */
    public String getName() {
        return name;
    }
}
