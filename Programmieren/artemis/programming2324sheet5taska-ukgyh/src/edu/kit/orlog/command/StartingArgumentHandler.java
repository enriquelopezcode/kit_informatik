package edu.kit.orlog.command;

import edu.kit.orlog.exceptions.StartingArgumentInvalidException;
import edu.kit.orlog.model.Player;
import edu.kit.orlog.model.gameelements.godfavors.GodFavorFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles parsing of starting arguments for a game session.
 * This class includes methods to validate and parse command line arguments
 * provided at the start of the game, ensuring they adhere to the expected format
 * and constraints.
 *
 * @author ukgyh
 */
public final class StartingArgumentHandler {
    private static final String ARGUMENT_AMOUNT_ERROR = "starting argument amount must be %d";
    private static final String FORBIDDEN_NAME_SYMBOL_ERROR = "Name can't contain symbol '%c'";
    private static final String HEALTH_NOT_INTEGER_ERROR = "health value is not a valid integer";
    private static final String GODFAVOR_AMOUNT_ERROR = "GodFavor amount must be %d";
    private static final String GODFAVOR_NOT_VALID_ERROR = "GodFavor %s does not exist";
    private static final String GODFAVOR_DUPLICATE_ERROR = "GodFavors of Player must be unique";
    private static final String TOKEN_NOT_INTEGER_ERROR = "token amount is not a valid integer";
    private static final String TOKEN_NEGATIVE_ERROR = "token amount must be non-negative";
    private static final String MINIMUM_HEALTH_ERROR = "health value must be at least %d";
    private static final char[] FORBIDDEN_NAME_SYMBOLS = {';', ' '};
    private static final String GODFAVOR_SEPARATOR = ";";
    private static final int MINIMUM_HEALTH = 5;
    private static final int EXPECTED_ARGUMENTS_COUNT = 6;
    private static final int PLAYER1_NAME_INDEX = 0;
    private static final int PLAYER2_NAME_INDEX = 2;
    private static final int PLAYER1_GODFAVOR_NAME_INDEX = 1;
    private static final int PLAYER2_GODFAVOR_NAME_INDEX = 3;
    private static final int GODFAVOR_AMOUNT = 3;
    private static final int PLAYER_HEALTH_INDEX = 4;
    private static final int PLAYER_TOKENS_INDEX = 5;
    private static final int PLAYER_ONE_ID = 0;
    private static final int PLAYER_TWO_ID = 1;


    private StartingArgumentHandler() {
    }

    /**
     * The command line arguments must be in the following format:
     * "name1" "godFavors1" "name2" "godFavors2" "points" "strength"
     * The requirements for the arguments are as follows:
     * - "name1" and "name2" are non-empty strings representing the names of the players.
     *   These names should not contain spaces or semicolons.
     * - "godFavors1" and "godFavors2" represent the pre-chosen god favors for each player.
     *   These should be a list of exactly three unique god favor codes, separated by semicolons (e.g., "TS;TT;IR").
     * - "points" represents the initial life points of each player. This value must be an integer
     *   and at least 5.
     * - "tokens" represents the initial god strength for each player. This value must be a non-negative integer.
     * If any of the requirements are not met, the program will halt immediately.
     *
     * @param startingArguments the starting game conditions, including player names, god favors, points, and strength.
     * @return a list of players with the specified starting conditions
     * @throws StartingArgumentInvalidException if the starting arguments are not in the correct format
     */
    public static List<Player> parseStartingArguments(String[] startingArguments) throws StartingArgumentInvalidException {
        if (startingArguments.length != EXPECTED_ARGUMENTS_COUNT) {
            throw new StartingArgumentInvalidException(ARGUMENT_AMOUNT_ERROR.formatted(EXPECTED_ARGUMENTS_COUNT));
        }

        String player1Name = checkName(startingArguments[PLAYER1_NAME_INDEX]);
        String player2Name = checkName(startingArguments[PLAYER2_NAME_INDEX]);

        String[] player1GodFavors = parseGodFavors(startingArguments[PLAYER1_GODFAVOR_NAME_INDEX]);
        String[] player2GodFavors = parseGodFavors(startingArguments[PLAYER2_GODFAVOR_NAME_INDEX]);

        Set<String> player1GodFavorsSet = new HashSet<>(Arrays.asList(player1GodFavors));
        Set<String> player2GodFavorsSet = new HashSet<>(Arrays.asList(player2GodFavors));

        int playerHealth = parsePlayerHealth(startingArguments[PLAYER_HEALTH_INDEX]);
        int godFavorTokens = parseTokens(startingArguments[PLAYER_TOKENS_INDEX]);

        Player player1 = new Player(player1Name, playerHealth, godFavorTokens, player1GodFavorsSet, PLAYER_ONE_ID);
        Player player2 = new Player(player2Name, playerHealth, godFavorTokens, player2GodFavorsSet, PLAYER_TWO_ID);

        return List.of(player1, player2);


    }

    /**
     * Validates the provided name to ensure it does not contain any forbidden symbols.
     *
     * This method checks each character of the provided name against a predefined set of forbidden symbols.
     * If the name contains a forbidden symbol, a StartingArgumentInvalidException is thrown.
     *
     * @param name The name to be validated.
     * @return The validated name if no forbidden symbols are found.
     * @throws StartingArgumentInvalidException If the name contains any of the forbidden symbols.
     */
    private static String checkName(String name) throws StartingArgumentInvalidException {
        for (char symbol : FORBIDDEN_NAME_SYMBOLS) {
            if (name.contains(GODFAVOR_SEPARATOR)) {
                throw new StartingArgumentInvalidException(FORBIDDEN_NAME_SYMBOL_ERROR.formatted(symbol));
            }
        }
        return name;
    }

    /**
     * Parses and validates the health value of a player from a string.
     *
     * This method attempts to parse the healthString argument as an integer.
     * If the parsing fails or the parsed value is less than the minimum allowed health,
     * a StartingArgumentInvalidException is thrown.
     *
     * @param healthString The string representation of the player's health.
     * @return The integer value of the player's health.
     * @throws StartingArgumentInvalidException If the healthString is not a valid integer or is less than the minimum allowed health.
     */
    private static int parsePlayerHealth(String healthString) throws StartingArgumentInvalidException {
        int health;
        try {
            health = Integer.parseInt(healthString);
        } catch (NumberFormatException e) {
            throw new StartingArgumentInvalidException(HEALTH_NOT_INTEGER_ERROR);
        }

        if (health < MINIMUM_HEALTH) {
            throw new StartingArgumentInvalidException(MINIMUM_HEALTH_ERROR.formatted(MINIMUM_HEALTH));
        }

        return health;

    }

    /**
     * Parses and validates a string representing god favors.
     *
     * This method splits the input string by ';' and validates that the number of god favors
     * matches the expected GODFAVOR_AMOUNT. It also checks each god favor against a list of valid identifiers,
     * throwing an exception if an invalid or duplicate god favor is found.
     *
     * @param godFavorsString The string representation of god favors, separated by semicolons.
     * @return An array of validated god favors.
     * @throws StartingArgumentInvalidException If the number of god favors is incorrect, any god favor is invalid, or duplicates are found.
     */
    private static String[] parseGodFavors(String godFavorsString) throws StartingArgumentInvalidException {
        String[] godFavors = godFavorsString.split(GODFAVOR_SEPARATOR);
        if (godFavors.length != GODFAVOR_AMOUNT) {
            throw new StartingArgumentInvalidException(GODFAVOR_AMOUNT_ERROR.formatted(GODFAVOR_AMOUNT));
        }

        for (String godFavor : godFavors) {
            if (!GodFavorFactory.GODFAVOR_IDENTIFIERS.contains(godFavor)) {
                throw new StartingArgumentInvalidException(GODFAVOR_NOT_VALID_ERROR.formatted(godFavor));
            }

        }

        Set<String> godFavorsSet = new HashSet<>(Arrays.asList(godFavors));
        if (godFavorsSet.size() != godFavors.length) {
            throw new StartingArgumentInvalidException(GODFAVOR_DUPLICATE_ERROR);
        }

        return godFavors;

    }

    /**
     * Parses and validates the token count from a string.
     *
     * This method attempts to parse the tokenString argument as an integer.
     * If the parsing fails or the parsed value is negative, a StartingArgumentInvalidException is thrown.
     *
     * @param tokenString The string representation of the token count.
     * @return The integer value of the token count.
     * @throws StartingArgumentInvalidException If the tokenString is not a valid integer or is negative.
     */
    private static int parseTokens(String tokenString) throws StartingArgumentInvalidException {
        int tokens;
        try {
            tokens = Integer.parseInt(tokenString);
        } catch (NumberFormatException e) {
            throw new StartingArgumentInvalidException(TOKEN_NOT_INTEGER_ERROR);
        }

        if (tokens < 0) {
            throw new StartingArgumentInvalidException(TOKEN_NEGATIVE_ERROR);
        }

        return tokens;

    }
}
