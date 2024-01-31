package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.model.Game;

/**
 * Represents the Theft fighting element in the Orlog board game simulation.
 * The Theft element is unique in its ability to manipulate god tokens,
 * transferring them from the opponent player to the ally player.
 * @author ukgyh
 */
public class Theft extends FightingElement {
    private static final int THEFT_ADDED_GOD_TOKENS = 1;
    private static final int THEFT_REMOVED_GOD_TOKENS = 1;

    /**
     * Constructs a new Theft instance.
     *
     * @param game The game instance to which this Theft element belongs.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     */
    Theft(Game game, int allyPlayer, int opponentPlayer) {
        super(game, allyPlayer, opponentPlayer);
    }

    /**
     * Applies the effect of Theft by adjusting the god tokens of both the ally and opponent players.
     * It removes a certain number of god tokens from the opponent and adds an equivalent number to the ally player.
     * If certain conditions are met, additional god favor tokens are granted to the ally player.
     * After applying its effect, the Theft element is marked as inactive.
     */
    public void applyEffect() {
        game.getPlayerStateHandler().addGodToken(THEFT_REMOVED_GOD_TOKENS, allyPlayer);
        game.getPlayerStateHandler().addGodToken(-THEFT_REMOVED_GOD_TOKENS, opponentPlayer);



        if (producesGodToken) {
            game.getPlayerStateHandler().applyGodFavorTokensToPlayer(THEFT_ADDED_GOD_TOKENS, allyPlayer);
        }

        active = false;
    }
}
