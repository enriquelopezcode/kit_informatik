package edu.kit.orlog.model;
/**
 * The {@code PlayerStateHandler} class is responsible for managing and updating the state of players in a game.
 * It handles various game aspects such as melee and ranged damage dealt and blocked, god favor tokens,
 * and the application of damage and health changes to players. This class supports tracking and applying changes
 * to player states, including damage calculations and god favor token management.
 * @author ukgyh
 */

public class PlayerStateHandler {
    private static final int BLOCKED_DAMAGE_POSITION = 0;
    private static final int SUFFERED_DAMAGE_POSITION = 1;
    private static final int GOD_FAVOR_TOKENS_USED_POSITION = 2;
    private static final int GODFAVOR_DATA_SIZE = 3;
    private int[] meleeDamageDealt;
    private int[] meleeDamageBlocked;
    private int[] rangedDamageDealt;
    private int[] rangedDamageBlocked;

    private int[] resultingDamage;
    private int[] changedGodFavorTokens;
    private int[] tokensSpent;

    private final Player[] players;


    /**
     * Constructs a new {@code PlayerStateHandler} object.
     * It initializes the player state arrays and the game.
     * @param players array of the player objects in the game (player1 at position 0, player2 at position 1).
     */
    public PlayerStateHandler(Player[] players) {
        this.players = players;
        this.meleeDamageDealt = new int[]{0, 0};
        this.meleeDamageBlocked = new int[]{0, 0};
        this.rangedDamageDealt = new int[]{0, 0};
        this.rangedDamageBlocked = new int[]{0, 0};
        this.resultingDamage = new int[]{0, 0};
        this.changedGodFavorTokens = new int[]{0, 0};
        this.tokensSpent = new int[]{0, 0};
    }

    /**
     * Records the amount of melee damage suffered by a specific player.
     * This damage is not applied to the player immediately.
     *
     * @param damage The amount of melee damage dealt.
     * @param playerID The ID of the player who suffered the damage.
     */
    public void addMeleeDamage(int damage, int playerID) {
        meleeDamageDealt[playerID] += damage;
    }

    /**
     * Records the amount of melee damage a specific player can block.
     *
     * @param blockedDamage The amount of melee damage the player can block.
     * @param playerID The ID of the player who blocked the damage.
     */
    public void addBlockedMeleeDamage(int blockedDamage, int playerID) {
        meleeDamageBlocked[playerID] += blockedDamage;
    }

    /**
     * Records the amount of ranged damage suffered by a specific player.
     * This damage is not applied to the player immediately.
     *
     * @param damage The amount of melee damage dealt.
     * @param playerID The ID of the player who suffered the damage.
     */
    public void addRangedDamage(int damage, int playerID) {
        rangedDamageDealt[playerID] += damage;
    }

    /**
     * Records the amount of ranged damage a specific player can block.
     *
     * @param blockedDamage The amount of ranged damage blocked.
     * @param playerID The ID of the player who blocked the damage.
     */
    public void addBlockedRangedDamage(int blockedDamage, int playerID) {
        rangedDamageBlocked[playerID] += blockedDamage;
    }

    /**
     * Calculates the resulting damage for a specific player.
     * The resulting damage is the difference between the damage dealt and the damage blocked.
     * @param playerID the player for which the resulting damage is calculated.
     */
    public void calculateResultingDamage(int playerID) {
        int resultingMeleeDamage  = Math.max(0, meleeDamageDealt[playerID] - meleeDamageBlocked[playerID]);
        int resultingRangedDamage = Math.max(0, rangedDamageDealt[playerID] - rangedDamageBlocked[playerID]);
        resultingDamage[playerID] = resultingMeleeDamage + resultingRangedDamage;
    }

    /**
     * records the amount of god favor tokens added or taken from a player.
     * The token change is not applied to the player immediately
     * @param godTokens the change in god favor tokens of a player (negative for losing and positive for adding tokens)
     * @param playerID the id of the player for which the change is made
     */
    public void addGodToken(int godTokens, int playerID) {
        changedGodFavorTokens[playerID] += godTokens;
    }

    /**
     * applies the resulting damage to the players in the game.
     * the method calculateResultingDamage(int playerID) should be called before this method is called
     */
    public void applyDamage() {
        for (int i = 0; i < 2; i++) {
            //setHealth method in player object ensures health is >= 0
            players[i].setHealth(players[i].getHealth() - resultingDamage[i]);
        }
    }

    /**
     * method for recording and then applying extra health change to a specified player.
     * This method is useful for GodFavors who add extra health changes to players after the damage calculation
     * If the health change is negative and damage is taken then the damage is also recorded in the resulting damage record
     * @param healthChange the health change to be applied (negative for damage, positive for healing)
     * @param playerID the player for which the health change should be applied
     */
    public void applyHealthChangeToPlayer(int healthChange, int playerID) {
        if (healthChange < 0) {
            //if damage is applied it has to be recorded in the resulting damage record of the player
            resultingDamage[playerID] -= healthChange;
        }
        //setHealth method in player object ensures health is >= 0
        players[playerID].setHealth(players[playerID].getHealth() + healthChange);
    }

    /**
     * applies the result of token steal to both players.
     * the amount of stolen tokens can at most be the number of tokens that the player from which is stolen has.
     * this amount is added to the opponent player who stole the tokens.
     */
    public void applyStolenGodFavorTokens() {
        //absolute value of the amount of tokens stolen
        int appliedTokens = Math.abs(changedGodFavorTokens[0]);

        //index of the player who lost the tokens is initialized
        int minusPlayerIndex = 0;

        for (int i = 0; i < 2; i++) {
            if (changedGodFavorTokens[i] <= 0) {
                //if the player lost tokens then the index of the player who lost the tokens is set
                minusPlayerIndex = i;
                //if the players tokens were stolen then the amount of tokens lost is at most the amount of tokens the player has
                appliedTokens = Math.min(appliedTokens, players[minusPlayerIndex].getGodFavorTokens());
                players[minusPlayerIndex].setGodFavorTokens(players[minusPlayerIndex].getGodFavorTokens() - appliedTokens);

            }
        }
        int otherPlayerIndex = Math.abs(minusPlayerIndex - 1);

        //the player who stole the tokens gets the tokens
        players[otherPlayerIndex].setGodFavorTokens(players[otherPlayerIndex].getGodFavorTokens() + appliedTokens);


    }

    /**
     * function that provides game data used by GodFavors to calculate their effect.
     *
     * @param playerID the playerId of the player whose data should be returned
     * @return array with amount of blocked damage by player at index 0, amount of suffered damage at
     *              index 1 and amount of godFavorTokens used at index 2
     */
    public int[] getGodFavorData(int playerID) {
        int[] godFavorData = new int[GODFAVOR_DATA_SIZE];
        //used for Heimdall GodFavor
        //the total blocked damage is the minimum of the damage blocked and the damage dealt
        int totalRangedDamageBlocked = Math.min(rangedDamageBlocked[playerID], rangedDamageDealt[playerID]);
        int totalMeleeDamageBlocked = Math.min(meleeDamageBlocked[playerID], meleeDamageDealt[playerID]);
        godFavorData[BLOCKED_DAMAGE_POSITION] = totalMeleeDamageBlocked + totalRangedDamageBlocked;

        //used for Mimir GodFavor
        godFavorData[SUFFERED_DAMAGE_POSITION] = resultingDamage[playerID];

        //used for Thrymr GodFavor
        godFavorData[GOD_FAVOR_TOKENS_USED_POSITION] = tokensSpent[playerID];
        return godFavorData.clone();
    }

    /**
     * applies a change in GodFavor tokens to a player immediately.
     * @param godFavorTokens amount of tokens to be added / subtracted
     * @param playerID player whose tokens should be changed
     */
    public void applyGodFavorTokensToPlayer(int godFavorTokens, int playerID) {
        players[playerID].setGodFavorTokens(players[playerID].getGodFavorTokens() + godFavorTokens);
    }

    /**
     * resets the Game Data collected in the round.
     */
    public void reset() {
        this.meleeDamageDealt = new int[]{0, 0};
        this.meleeDamageBlocked = new int[]{0, 0};
        this.rangedDamageDealt = new int[]{0, 0};
        this.rangedDamageBlocked = new int[]{0, 0};
        this.changedGodFavorTokens = new int[]{0, 0};
        this.resultingDamage = new int[]{0, 0};
        this.tokensSpent = new int[]{0, 0};
    }

    /**
     * adds to the tokenSpent metric of a player.
     * @param tokens the tokens to be added to the record
     * @param playerId the player to whom the tokens should be added
     */
    public void applyTokensSpent(int tokens, int playerId) {
        tokensSpent[playerId] += tokens;
    }
}
