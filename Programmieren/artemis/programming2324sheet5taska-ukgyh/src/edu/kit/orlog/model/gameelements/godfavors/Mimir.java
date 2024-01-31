package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;

/**
 * Represents the Mimir god favor in the Orlog board game simulation, extending the GodFavor class.
 *
 * The effect of Mimir involves granting additional tokens to the ally player, proportional to the damage
 * they have suffered, and scaled by the god favor's level.
 * @author ukgyh
 */
public class Mimir extends GodFavor {
    private static final String GOD_FAVOR_NAME = "Mimir";
    private static final int INITIAL_COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int PRIORITY = 6;
    private static final int SUFFERED_DAMAGE_POSITION = 1;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new instance of Mimir.
     *
     * @param game The game instance.
     * @param level The level of the god favor.
     * @param allyPlayer The ally player's identifier.
     * @param opponentPlayer The opponent player's identifier.
     */
    public Mimir(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }

    /**
     * Applies Mimir's effect based on the damage suffered by the ally player.
     * Additional tokens are granted to the ally player, calculated as the product
     * of the suffered damage and the god favor's level.
     */
    @Override
    public void applyEffect() {
        int sufferedDamage = game.getPlayerStateHandler().getGodFavorData(allyPlayer)[SUFFERED_DAMAGE_POSITION];
        int addedTokens = sufferedDamage * level;
        game.getPlayerStateHandler().applyGodFavorTokensToPlayer(addedTokens, allyPlayer);
    }

    /**
     * Returns the name of the god favor.
     * @return The name of the god favor.
     */
    public String getName() {
        return name;
    }

}
