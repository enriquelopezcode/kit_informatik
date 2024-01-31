package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;

/**
 * Represents the Idun god favor.
 *
 * The Idun god favor provides a healing effect to the ally player, which increases with its level.
 * The healing amount is calculated based on a base value plus an additional amount that scales
 * with the god favor's level.
 * @author ukgyh
 */
public class Idun extends GodFavor {
    private static final String GOD_FAVOR_NAME = "Idun";
    private static final int BASE_HEAL = 2;
    private static final int UPGRADE_HEAL = 2;
    private static final int INITIAL_COST = 4;
    private static final int UPGRADE_COST = 3;
    private static final int PRIORITY = 3;
    protected String name = GOD_FAVOR_NAME;


    /**
     * Constructs a new Idun god favor instance.
     *
     * @param game The game instance to which this god favor belongs.
     * @param level The level of the Idun god favor.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     */
    public Idun(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }

    /**
     * Applies the healing effect of the Idun god favor.
     * The effect involves healing the ally player based on the base heal amount plus an additional
     * amount that scales with the god favor's level. This effect is only applied if the ally player's
     * health is above zero.
     */
    @Override
    public void applyEffect() {
        if (game.getHealth(allyPlayer) > 0) {
            game.getPlayerStateHandler().applyHealthChangeToPlayer(BASE_HEAL + UPGRADE_HEAL * (level - 1), allyPlayer);
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
