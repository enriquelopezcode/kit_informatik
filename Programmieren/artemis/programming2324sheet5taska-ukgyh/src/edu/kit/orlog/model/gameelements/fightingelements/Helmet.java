package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;

/**
 * Represents a Helmet as a fighting element.
 * The Helmet is a defensive fighting element designed to block melee damage aimed at the ally player.
 * Additionally, it has the capability to generate god favor tokens for the ally player under certain conditions.
 * @author ukgyh
 */
public class Helmet extends FightingElement {
    private static final int HELMET_BLOCKED_MELEE_DAMAGE = 1;
    private static final int HELMET_ADDED_GOD_TOKENS = 1;
    /**
     * Constructs a new Helmet instance.
     *
     * @param game The game instance to which this Helmet belongs.
     * @param allyPlayer The identifier of the ally player who will use the Helmet.
     * @param opponentPlayer The identifier of the opponent player.
     */
    Helmet(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Applies the Helmet's effect by blocking a certain amount of melee damage directed at the ally player
     * and, if certain conditions are met, adding god favor tokens to the ally player.
     * After applying its effect, the Helmet is deactivated.
     */
    public void applyEffect() {
        game.getPlayerStateHandler().addBlockedMeleeDamage(HELMET_BLOCKED_MELEE_DAMAGE, allyPlayer);

        if (producesGodToken) {
            game.getPlayerStateHandler().applyGodFavorTokensToPlayer(HELMET_ADDED_GOD_TOKENS, allyPlayer);
        }

        active = false;
    }
}

