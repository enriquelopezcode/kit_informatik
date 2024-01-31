package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.GameElement;

/**
 * Abstract class representing a fighting element.
 *
 * Fighting elements are components of the game that participate in battles between players.
 * They can have various effects and properties, including the ability to produce god tokens,
 * which are a key part of the game's mechanics.
 *
 * This class provides a foundation for specific fighting elements. It includes a flag indicating
 * whether the element produces god tokens, which can be set through a dedicated method.
 * The exact behavior and effect of each fighting element are defined in subclasses that extend
 * this class.
 * @author ukgyh
 */
public abstract class FightingElement extends GameElement {
    protected boolean producesGodToken;

    /**
     * Constructs a new FightingElement instance.
     *
     * @param game The game instance to which this fighting element belongs.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     */
    public FightingElement(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Sets whether this fighting element produces a god token.
     * When set, the element is marked as active.
     *
     * @param producesGodToken A boolean indicating whether the element produces a god token.
     */
    public void setProducesGodToken(boolean producesGodToken) {
        this.producesGodToken = producesGodToken;
        this.active = true;
    }
}
