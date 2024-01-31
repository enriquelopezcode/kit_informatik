package edu.kit.orlog.model;
import edu.kit.orlog.exceptions.GameElementCreationException;
import edu.kit.orlog.model.gameelements.fightingelements.FightingElement;
import edu.kit.orlog.model.gameelements.fightingelements.FightingElementFactory;
import edu.kit.orlog.model.gameelements.godfavors.GodFavor;
import edu.kit.orlog.model.gameelements.godfavors.GodFavorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;


/**
 * This class represents the game and its current state
 * It contains all information about the current state of the game and provides methods
 * to manage game elements, phases, and player interactions. The class is responsible for
 * progressing the game through its different phases and applying game mechanics such as
 * fighting elements and god favors.
 * @author ukgyh
 */
public class Game {
    private static final String PLAYER_SEPARATOR = ";";
    private static final Set<String> USAGE_DESPITE_LOST = Set.of("Mimir", "Thrymr");
    private final Player[] players;
    private GamePhase currentGamePhase;
    private int playerTurn;
    private List<FightingElement> currentFightingElements;
    private List<GodFavor> currentGodFavors;

    private final FightingElementFactory fightingElementFactory;
    private final GodFavorFactory godFavorFactory;
    private final PlayerStateHandler playerStateHandler;






    /**
     * Constructor for creating a new Game instance.
     * Initializes the game with two players, sets the initial game phase to DICEPHASE,
     * and prepares all necessary game elements and factories.
     *
     * @param player1 the first player
     * @param player2 the second player
     */

    public Game(Player player1, Player player2) {
        this.players = new Player[]{player1, player2};
        this.currentGamePhase = GamePhase.DICEPHASE;
        this.playerTurn = 0;
        this.godFavorFactory = new GodFavorFactory();
        this.fightingElementFactory = new FightingElementFactory();
        this.currentFightingElements = new ArrayList<>();
        this.currentGodFavors = new ArrayList<>();
        this.playerStateHandler = new PlayerStateHandler(players);
    }

    /**
     * adds the specified fighting element to the current fighting elements in the round.
     * @param fightingElements the fighting elements to add
     */
    public void addToCurrentFightingElements(List<FightingElement> fightingElements) {
        this.currentFightingElements.addAll(fightingElements);
    }

    /**
     * Adds a god favor to the current round.
     * This method is used to include a new god favor that a player has activated or acquired
     * during the current round.
     *
     * @param godFavor the god favor to be added
     */
    public void addToCurrentGodFavors(GodFavor godFavor) {
        this.currentGodFavors.add(godFavor);
    }

    /**
     * returns the current phase of the game.
     * @return the current phase of the game
     */
    public GamePhase getCurrentGamePhase() {
        return this.currentGamePhase;
    }

    /**
     * Provides a reference to the player state handler of the game.
     * The PlayerStateHandler is responsible for managing the states of the players
     * throughout the game, including health, tokens, and effects.
     *
     * @return the PlayerStateHandler object
     */
    public PlayerStateHandler getPlayerStateHandler() {
        return playerStateHandler;
    }

    /**
     * Advances the game to the next phase.
     * This method is responsible for progressing the game through its phases in a cyclic order.
     * It also manages the turn-taking mechanism between players.
     * If the game is over, the game phase is set to POSTGAMEPHASE.
     *
     * @param gameOver whether the game is over
     */
    public void progressPhase(boolean gameOver) {
        if (gameOver) {
            this.currentGamePhase = GamePhase.POSTGAMEPHASE;
            return;
        }
        this.playerTurn = (this.playerTurn + 1) % 2;

        if (this.currentGamePhase == GamePhase.EVALUATIONPHASE) {
            //if the game is in the evaluation phase, the game is reset to the dice phase
            this.currentGamePhase = GamePhase.DICEPHASE;
            this.playerTurn = 0;

        } else if (this.playerTurn == 0) {
            //if it is the first players turn again we are in a new game phase
            this.currentGamePhase = getNextPhase();
        }
    }

    /**
     * Returns the next phase of the game.
     * @return GamePhase object of the next phase
     */
    private GamePhase getNextPhase() {
        return switch (getCurrentGamePhase()) {
            case DICEPHASE -> GamePhase.GODFAVORPHASE;
            case GODFAVORPHASE -> GamePhase.EVALUATIONPHASE;
            case EVALUATIONPHASE -> GamePhase.DICEPHASE;
            case POSTGAMEPHASE -> GamePhase.POSTGAMEPHASE;
        };
    }

    /**
     * Evaluates the outcomes of the current round.
     * This method applies the effects of active fighting elements and god favors, calculates
     * and applies damage, and resets the game elements for the next round.
     */
    public void evaluate() {
        for (FightingElement fightingElement : currentFightingElements) {
            if (fightingElement.isActive()) {
                //god favor tokens produced by fighting elements are applied directly to the player
                fightingElement.applyEffect();
            }

        }

        //the stealing of god favor tokens is applied
        playerStateHandler.applyStolenGodFavorTokens();

        //the damage is calculated and applied
        for (int i = 0; i < 2; i++) {
            playerStateHandler.calculateResultingDamage(i);
        }
        playerStateHandler.applyDamage();

        //GodFavors are sorted by priority and then applied
        Collections.sort(currentGodFavors);
        for (GodFavor godFavor : currentGodFavors) {
            //first it is checked whether the player can afford to activate the favor, then his god favor tokens are reduced
            if (players[godFavor.getAllyPlayer()].getGodFavorTokens() >= godFavor.calculateCost()) {

                //Only three GodFavors are allowed to be used despite health being zero
                if (players[godFavor.getAllyPlayer()].getHealth() > 0 || USAGE_DESPITE_LOST.contains(godFavor.getName())) {
                    playerStateHandler.applyGodFavorTokensToPlayer(-godFavor.calculateCost(), godFavor.getAllyPlayer());

                    //tokens spent are recorded in the player state handler
                    playerStateHandler.applyTokensSpent(godFavor.calculateCost(), godFavor.getAllyPlayer());
                } else {
                    godFavor.setActive(false);
                }


            } else {
                godFavor.setActive(false);
            }
        }

        for (GodFavor godFavor : currentGodFavors) {
            if (godFavor.isActive()) {
                godFavor.applyEffect();
            }
        }


        resetCurrentGameElements();

    }

    /**
     * Resets the game elements for the next round.
     * This method resets the current fighting elements and god favors for the next round.
     * It also resets the player state handler.
     */
    private void resetCurrentGameElements() {
        this.currentFightingElements = new ArrayList<>();
        this.currentGodFavors = new ArrayList<>();
        playerStateHandler.reset();
    }


    /**
     * provides the string identifiers of the god favors chosen by the player.
     * @param playerID the player whose god favor id's should be returned
     * @return a set of strings containing the identifiers of the god favors
     */
    public Set<String> getGodFavors(int playerID) {
        return players[playerID].getGodFavors();
    }

    /**
     * provides the health of a player.
     * @param playerID the player whose health should be returned
     * @return the health of the player
     */
    public int getHealth(int playerID) {
        return players[playerID].getHealth();
    }

    /**
     * provides the name of the player whose turn it is not.
     * @return name of the player
     */
    public String getNextPlayerName() {
        return this.players[Math.abs(playerTurn - 1)].getName();
    }

    /**
     * provides the name of the player whose turn it is.
     * @return name of the player
     */
    public String getCurrentPlayerName() {
        return this.players[playerTurn].getName();
    }

    /**
     * provides the id of the player whose turn it is.
     * @return id of the player
     */
    public int getCurrentPlayerID() {
        return this.players[playerTurn].getID();
    }

    /**
     * provides the id of the player whose turn it is not.
     * @return id of the player
     */
    public int getNextPlayerID() {
        return this.players[Math.abs(playerTurn - 1)].getID();
    }


    /**
     * forwarding method for the fighting element factory that creates a fighting element which is later added to the game.
     * @param elementId the string identifier of the fighting element
     * @param game the game object
     * @param ally the player id of the player who activated the fighting element
     * @param opponent the player id of the opponent of the player who activated the fighting element
     * @return the created fighting element
     * @throws GameElementCreationException if the creation of the fighting element fails because of an unknown identifier
     */
    public FightingElement createFightingElement(String elementId, Game game, int ally, int opponent) throws GameElementCreationException {
        return fightingElementFactory.createElement(elementId, game, ally, opponent);
    }

    /**
     * forwarding method for the god favor factory that creates a god favor which is later added to the game.
     * @param godFavorId the string identifier of the god favor
     * @param game the game object
     * @param level the level of the god favor
     * @param ally the player id of the player who activated the god favor
     * @param opponent the player id of the opponent of the player who activated the god favor
     * @return the created god favor
     * @throws GameElementCreationException if the creation of the god favor fails because of an unknown identifier
     */
    public GodFavor createGodFavor(String godFavorId, Game game, int level, int ally, int opponent) throws GameElementCreationException {
        return godFavorFactory.createElement(godFavorId, game, level, ally, opponent);
    }

    /**
     * reduces the level of the opponents activated god favor by one.
     * @param playerID the id of the opponent whose god favor should be reduced in level
     */
    public void reduceGodFavorLevel(int playerID) {
        for (GodFavor godFavor : currentGodFavors) {
            if (godFavor.getAllyPlayer() == playerID) {
                godFavor.reduceLevel();
            }
        }
    }


    /**
     * provides the string representation of the game.
     * for each player the name, health, and god favor tokens are provided.
     * @return the string representation of the game
     */
    @Override
    public String toString() {
        //each line is filled with player information and seperated by a new line
        StringJoiner lineJoiner = new StringJoiner(System.lineSeparator());

        for (Player player : players) {
            lineJoiner.add(formatPlayerData(player));
        }

        return lineJoiner.toString();
    }

    /**
     * helper method for formatting the player data.
     * the format is as follows: name;health;godFavorTokens
     * @param player the player object whose data should be formatted
     * @return the formatted string
     */
    private String formatPlayerData(Player player) {
        return String.join(PLAYER_SEPARATOR,
                player.getName(),
                String.valueOf(player.getHealth()),
                String.valueOf(player.getGodFavorTokens()));
    }



}

