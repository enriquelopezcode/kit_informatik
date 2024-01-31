package edu.kit.orlog.model;

/**
 * This enum represents the different phases of the game.
 * The phases are used to determine which commands are allowed at a given time.
 * @author ukgyh
 */
public enum GamePhase {
    /**
     * The phase in which the players roll their fighting elements.
     */
    DICEPHASE,
    /**
     * The phase in which the players choose their GodFavors.
     */
    GODFAVORPHASE,
    /**
     * The phase in which the effects of the game elements on the players are calculated.
     */
    EVALUATIONPHASE,

    /**
     * The phase in which the game is over.
     */
    POSTGAMEPHASE
}
