package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;


/**
 * Represents the Var god favor.
 * The effect involves calculating the number of tokens used by the opponent and converting
 * them into healing points for the ally player, scaled by the level of the god favor.
 * @author ukgyh
 */
public class VarGod extends GodFavor {
    private static final String GOD_FAVOR_NAME = "Var";
    private static final int USED_TOKENS_POSITION = 2;
    private static final int INITIAL_COST = 10;
    private static final int UPGRADE_COST = 4;
    private static final int PRIORITY = 5;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new instance of the Var god favor.
     *
     * @param game The game instance.
     * @param level The level of the god favor.
     * @param allyPlayer The ally player's identifier.
     * @param opponentPlayer The opponent player's identifier.
     */
    public VarGod(Game game, int level, int allyPlayer, int opponentPlayer) {
        super(game, level, allyPlayer, opponentPlayer, INITIAL_COST, UPGRADE_COST, PRIORITY);
    }
    /**
     * Applies the healing effect of the Var god favor.
     * The effect calculates healing points for the ally player based on the number of tokens
     * used by the opponent player, multiplied by the Var god favor's level.
     */
    @Override
    public void applyEffect() {
        if (game.getHealth(allyPlayer) > 0) {
            int usedTokens = game.getPlayerStateHandler().getGodFavorData(opponentPlayer)[USED_TOKENS_POSITION];
            int healedPoints = usedTokens * level;
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
