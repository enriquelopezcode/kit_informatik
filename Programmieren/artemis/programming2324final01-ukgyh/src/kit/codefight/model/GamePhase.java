package kit.codefight.model;

/**
 * represents the possible phases of the game.
 * @author ukgyh
 */
public enum GamePhase {
    /**
     * represents initialization phase of the game where AI are registered.
     */
    INITIALIZATION,
    /**
     * represents phase where a game is currently running.
     */
    RUNNING,
    /**
     * dummy value used when commands don't require a game phase to be executed.
     */
    NONE
}
