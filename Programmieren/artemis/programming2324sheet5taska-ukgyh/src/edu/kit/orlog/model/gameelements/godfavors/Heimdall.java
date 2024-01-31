package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;


/**
 * class that represents the Heimdall god favor.
 * This class defines the initial cost, upgrade cost, priority, and specific effect.
 *
 * When the Heimdall god favor is applied, it calculates the total blocked damage to both the ally and opponent players
 * and uses this value to heal the ally player based on the level of the god favor.
 *
 * @author ukgyh
 */
public class Heimdall extends GodFavor {
    private static final String GOD_FAVOR_NAME = "Heimdall";
    private static final int BLOCKED_DAMAGE_POSITION = 0;
    private static final int INITIAL_COST = 4;
    private static final int UPGRADE_COST = 3;
    private static final int PRIORITY = 4;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new Heimdall god favor instance.
     *
     * @param game The game instance to which this god favor belongs.
     * @param level The level of the Heimdall god favor.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     */
    public Heimdall(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }

    /**
     * Applies the effect of the Heimdall god favor.
     * The effect involves calculating the total blocked damage from both players and
     * healing the ally player based on this value and the level of the god favor.
     * This effect is only applied if the ally player's health is above zero.
     */
    @Override
    public void applyEffect() {
        if (game.getHealth(allyPlayer) > 0) {
            int allyBlockedDamage = game.getPlayerStateHandler().getGodFavorData(allyPlayer)[BLOCKED_DAMAGE_POSITION];
            int healedPoints = allyBlockedDamage * level;
            game.getPlayerStateHandler().applyHealthChangeToPlayer(healedPoints, allyPlayer);
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
