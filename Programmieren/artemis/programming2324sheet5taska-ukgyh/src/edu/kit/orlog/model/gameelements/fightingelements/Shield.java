package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;

/**
 * Represents a Shield as a fighting element.
 * The Shield is focused on defense, specifically designed to block ranged damage directed at the opponent player.
 * It also has the capability to generate god favor tokens for the ally player under certain conditions.
 * @author ukgyh
 */
public class Shield extends FightingElement {
    private static final int SHIELD_BLOCKED_RANGED_DAMAGE = 1;
    private static final int SHIELD_ADDED_GOD_TOKENS = 1;

    /**
     * Constructs a new Shield instance.
     *
     * @param game The game instance to which this Shield belongs.
     * @param allyPlayer The identifier of the ally player who will use the Shield.
     * @param opponentPlayer The identifier of the opponent player affected by the Shield.
     */
    Shield(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Applies the Shield's effect by blocking a certain amount of ranged damage aimed at the opponent player.
     * Additionally, if certain conditions are met, it grants god favor tokens to the ally player.
     * The Shield is deactivated after its effect is applied, marking the end of its action for the turn.
     */
    public void applyEffect() {
        game.getPlayerStateHandler().addBlockedRangedDamage(SHIELD_BLOCKED_RANGED_DAMAGE, allyPlayer);

        if (producesGodToken) {
            game.getPlayerStateHandler().applyGodFavorTokensToPlayer(SHIELD_ADDED_GOD_TOKENS, allyPlayer);
        }

        active = false;
    }
}
