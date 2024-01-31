package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;

/**
 * Represents an Axe as a fighting element in the Orlog board game simulation.
 * The Axe is a type of fighting element that, when activated, applies melee damage to the opponent player.
 *
 * After applying its effect, the Axe deactivates itself, indicating that its effect has been executed for the turn.
 * @author ukgyh
 */
public class Axe extends FightingElement {
    private static final int AXE_MELEE_DAMAGE = 1;
    /**
     * Constructs a new Axe instance.
     *
     * @param game The game instance to which this Axe belongs.
     * @param allyPlayer The identifier of the ally player who will use the Axe.
     * @param opponentPlayer The identifier of the opponent player who will receive the Axe's effect.
     */
    public Axe(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Applies the effect of the Axe, dealing melee damage to the opponent player.
     * After applying its effect, the Axe is deactivated.
     */
    public void applyEffect() {
        game.getPlayerStateHandler().addMeleeDamage(AXE_MELEE_DAMAGE, opponentPlayer);
        active = false;
    }
}
