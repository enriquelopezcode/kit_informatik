package edu.kit.orlog.model;


import java.util.Set;

/**
 * This class represents a player in the game.
 * Contains all information about the player and provides methods to retrieve and change them.
 * @author ukgyh
 */
public class Player {
    private int health;
    private int godFavorTokens;
    private final String name;
    private final Set<String> godFavors;
    private final int id;



    /**
     * Creates a new player with the specified name, health, god favor tokens, god favors and id.
     * @param name the name of the player
     * @param health the health of the player
     * @param godFavorTokens the starting god favor tokens of the player
     * @param godFavors String identifiers of the 3 god favors chosen by the player
     * @param id the id of the player
     */
    public Player(String name, int health, int godFavorTokens, Set<String> godFavors, int id) {
        this.name = name;
        this.health = health;
        this.godFavorTokens = godFavorTokens;
        this.godFavors = godFavors;
        this.id = id;
    }

    /**
     * Returns the health of the player.
     * @return integer value of health points
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the amount of god favor tokens the player has.
     * @return integer value of god favor tokens
     */
    public int getGodFavorTokens() {
        return godFavorTokens;
    }

    /**
     * Returns the name of the player.
     * @return String value of the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the String identifiers of the god favors of the player.
     * @return Set of Strings representing the god favors
     */
    public Set<String> getGodFavors() {
        return this.godFavors;
    }

    /**
     * Returns the id of the player.
     * @return integer value of the id
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the health of the player.
     * The health is never set to be lower than 0.
     * @param health the new health value
     */
    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    /**
     * Sets the god favor tokens of the player.
     * The god favor tokens are never set to be lower than 0.
     * @param godFavorTokens the new god favor tokens value
     */
    public void setGodFavorTokens(int godFavorTokens) {
        this.godFavorTokens = Math.max(godFavorTokens, 0);
    }
}
