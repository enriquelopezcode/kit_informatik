package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.GameElement;

/**
 * Abstract class representing a god favor, which applies effects that help the player who activates it.
 * This class extends GameElement and provides the functionality common to all god favors.
 * God favors are special elements in the game that can be invoked based on certain criteria.
 *
 * Each god favor has a level, initial cost, upgrade cost, and a priority. The cost of a god favor
 * can change based on its level. The class also implements the Comparable interface to allow
 * god favors to be compared based on their priority, facilitating sorting and priority-based logic.
 *
 * Specific types of god favors should extend this class, providing implementations for the abstract
 * methods inherited from GameElement and potentially adding additional functionality unique to the
 * specific god favor.
 *
 * @see GameElement
 * @author ukgyh
 */
public abstract class GodFavor extends GameElement implements Comparable<GodFavor> {
    private static final String GOD_FAVOR_NAME = "GodFavor";
    protected int level;
    protected int initialCost;
    protected int upgradeCost;
    protected int priority;
    protected String name = GOD_FAVOR_NAME;

    /**
     * Constructs a new GodFavor instance.
     *
     * @param game The game instance to which this god favor belongs.
     * @param level The initial level of the god favor.
     * @param allyPlayer The identifier of the ally player.
     * @param opponentPlayer The identifier of the opponent player.
     * @param initialCost The initial invocation cost of the god favor.
     * @param upgradeCost The cost required to activate the favor at higher levels.
     * @param priority The priority of the god favor, used for comparison and effect application order.
     */
    public GodFavor(Game game, int level, int allyPlayer, int opponentPlayer, int initialCost, int upgradeCost, int priority) {
        super(game, allyPlayer, opponentPlayer);
        this.level = level;
        this.initialCost = initialCost;
        this.upgradeCost = upgradeCost;
        this.priority = priority;
        setActive(true);
    }

    /**
     * Calculates the current cost of the god favor based on its level and upgrade cost.
     *
     * @return The calculated cost of the god favor.
     */
    public int calculateCost() {
        return initialCost + (level - 1) * upgradeCost;
    }

    /**
     * Compares this god favor with another based on their priorities.
     *
     * @param otherGodFavor The other god favor to compare with.
     * @return A negative integer, zero, or a positive integer as this god favor
     *         has less than, equal to, or greater than the other specified god favor's priority.
     */
    @Override
    public int compareTo(GodFavor otherGodFavor) {
        return Integer.compare(this.priority, otherGodFavor.priority);
    }

    /**
     * Reduces the level of the god favor. If the level drops below 1, the god favor is set to inactive.
     */
    public void reduceLevel() {
        level--;
        if (level < 1) {
            setActive(false);
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
