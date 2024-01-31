package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;

/**
 * Represents a Bow as a fighting element.
 * The Bow is a type of fighting element that inflicts ranged damage to the opponent player.
 * Additionally, it has the potential to generate god favor tokens for the ally player.
 * @author ukgyh
 *
 */
public class Bow extends FightingElement {
    private static final int BOW_RANGED_DAMAGE = 1;
    private static final int BOW_ADDED_GOD_TOKENS = 1;

    /**
     * Constructs a new Bow instance.
     *
     * @param game The game instance to which this Bow belongs.
     * @param allyPlayer The identifier of the ally player who will use the Bow.
     * @param opponentPlayer The identifier of the opponent player who will receive the Bow's effects.
     */
    Bow(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Applies the Bow's effects by inflicting ranged damage on the opponent player and,
     * if conditions allow, adding god favor tokens to the ally player.
     * After applying its effect, the Bow is deactivated.
     */
    public void applyEffect() {
        game.getPlayerStateHandler().addRangedDamage(BOW_RANGED_DAMAGE, opponentPlayer);

        if (producesGodToken) {
            game.getPlayerStateHandler().applyGodFavorTokensToPlayer(BOW_ADDED_GOD_TOKENS, allyPlayer);
        }

        active = false;
    }
}
