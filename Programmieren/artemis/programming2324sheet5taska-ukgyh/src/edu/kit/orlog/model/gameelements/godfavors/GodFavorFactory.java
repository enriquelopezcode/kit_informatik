package edu.kit.orlog.model.gameelements.godfavors;

import edu.kit.orlog.exceptions.GameElementCreationException;
import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.ElementFactory;

import java.util.Set;
import java.util.function.Function;

/**
 * Factory class for creating instances of GodFavor.
 * It extends the generic ElementFactory to specifically handle the creation of god favors.
 * This factory uses a set of predefined identifiers for different types of god favors
 * and maps these identifiers to their respective constructors.
 *
 * The factory provides a method to create a GodFavor instance based on a given identifier,
 * ensuring the creation of the correct type of god favor with the appropriate parameters.
 * The factory defines an initialization method to populate the creators map with specific
 * constructors for each type of god favor, linking the identifiers to their corresponding god favor classes.
 * @author ukgyh
 */

public class GodFavorFactory extends ElementFactory<GodFavor, GodFavorParams> {

    /**
     * The identifier for the Heimdall god favor.
     */
    public static final String HEIMDALL_SYMBOL = "HW";

    /**
     * The identifier for the Mimir god favor.
     */
    public static final String MIMIR_SYMBOL = "MW";

    /**
     * The identifier for the Thor god favor.
     */
    public static final String THOR_SYMBOL = "TS";

    /**
     * The identifier for the Thrymr god favor.
     */
    public static final String THRYMR_SYMBOL = "TT";

    /**
     * The identifier for the Var god favor.
     */
    public static final String VAR_SYMBOL = "VB";

    /**
     * The identifier for the Idun god favor.
     */
    public static final String IDUN_SYMBOL = "IR";

    /**
     * Set of identifiers for the different types of god favors.
     * This set is used to check if a given identifier corresponds to a known god favor type.
     */
    public static final Set<String> GODFAVOR_IDENTIFIERS =
            Set.of(HEIMDALL_SYMBOL, MIMIR_SYMBOL, THOR_SYMBOL, THRYMR_SYMBOL, VAR_SYMBOL, IDUN_SYMBOL);

    /**
     * Initializes the creators map with specific constructors for each type of god favor.
     * This method maps each god favor identifier to a corresponding lambda function that
     * instantiates a new god favor object with given parameters.
     */
    protected void initElements() {

        //maps the god favor identifiers to a lambda function that creates a new instance of a god favor object from the parameter object
        creators.put(HEIMDALL_SYMBOL, (params) ->
                new Heimdall(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(IDUN_SYMBOL, (params) ->
                new Idun(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(MIMIR_SYMBOL, (params) ->
                new Mimir(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(THOR_SYMBOL, (params) ->
                new Thor(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(VAR_SYMBOL, (params) ->
                new VarGod(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(THRYMR_SYMBOL, (params) ->
                new Thrymr(params.getGame(), params.getLevel(), params.getAllyPlayer(), params.getOpponentPlayer()));
    }


    /**
     * Creates a GodFavor instance based on the provided identifier and parameters.
     * It retrieves the appropriate constructor function from the creators map and applies it
     * with the given parameters to create the specific type of god favor.
     *
     * @param godFavorId The identifier of the god favor to create.
     * @param game The game instance to which the god favor belongs.
     * @param level The level of the god favor.
     * @param ally The identifier of the ally player.
     * @param opponent The identifier of the opponent player.
     * @return A new instance of the specified GodFavor.
     * @throws GameElementCreationException if the identifier does not correspond to a known god favor type.
     */
    public GodFavor createElement(String godFavorId, Game game, int level, int ally, int opponent) throws GameElementCreationException {
        if (!creators.containsKey(godFavorId)) {
            throw new GameElementCreationException(godFavorId);
        }


        GodFavorParams params = new GodFavorParams(game, level, ally, opponent);
        Function<GodFavorParams, GodFavor> creator = creators.get(godFavorId); //gets the corresponding lambda function
        return creator.apply(params); // Creates a new instance every time with the provided parameters
    }

}


