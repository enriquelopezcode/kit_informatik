package edu.kit.orlog.model.gameelements.fightingelements;

import edu.kit.orlog.exceptions.GameElementCreationException;
import edu.kit.orlog.model.Game;
import edu.kit.orlog.model.gameelements.ElementFactory;

import java.util.Set;


/**
 * Factory class for creating instances of FightingElement.
 * This class extends the generic ElementFactory to specifically handle the creation of fighting elements.
 * It maintains a set of predefined identifiers for different types of fighting elements and
 * maps these identifiers to their respective constructors.
 *
 * The factory includes a method for creating a FightingElement instance based on a given identifier
 * and parameters, ensuring the creation of the correct type of fighting element.
 * It also handles special cases where the fighting element identifier indicates the production
 * of god tokens.
 * @author ukgyh
 */
public class FightingElementFactory extends ElementFactory<FightingElement, FightingElementParams> {

    /**
     * The identifier for the axe fighting element.
     */
    public static final String AXE_SYMBOL = "MA";

    /**
     * The identifier for the helmet fighting element.
     */
    public static final String HELMET_SYMBOL = "MD";

    /**
     * The identifier for the bow fighting element.
     */
    public static final String BOW_SYMBOL = "RA";

    /**
     * The identifier for the shield fighting element.
     */
    public static final String SHIELD_SYMBOL = "RD";

    /**
     * The identifier for the theft fighting element.
     */
    public static final String THEFT_SYMBOL = "ST";

    /**
     * The identifier for the god token production prefix.
     */
    public static final char TOKEN_CHAR = 'G';

    /**
     * Set of identifiers for the different types of fighting elements.
     * This set is used to check if a given identifier corresponds to a known fighting element type.
     */
    public static final Set<String> FIGHTING_ELEMENTS = Set.of(AXE_SYMBOL, HELMET_SYMBOL, BOW_SYMBOL, SHIELD_SYMBOL, THEFT_SYMBOL,
            TOKEN_CHAR + HELMET_SYMBOL, TOKEN_CHAR + BOW_SYMBOL, TOKEN_CHAR + SHIELD_SYMBOL, TOKEN_CHAR + THEFT_SYMBOL);





    /**
     * Initializes the creators map with specific constructors for each type of fighting element.
     * This method maps each fighting element identifier to a corresponding lambda function that
     * instantiates a new fighting element object with the provided parameters.
     */
    @Override
    protected void initElements() {

        // Maps fighting element identifiers to lambda functions for instantiating objects
        creators.put(AXE_SYMBOL, (params) ->
                new Axe(params.getGame(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(HELMET_SYMBOL, (params) ->
                new Helmet(params.getGame(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(BOW_SYMBOL, (params) ->
                new Bow(params.getGame(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(SHIELD_SYMBOL, (params) ->
                new Shield(params.getGame(), params.getAllyPlayer(), params.getOpponentPlayer()));

        creators.put(THEFT_SYMBOL, (params) ->
                new Theft(params.getGame(), params.getAllyPlayer(), params.getOpponentPlayer()));
    }

    /**
     * Creates a FightingElement instance based on the provided identifier and parameters.
     * Handles special cases for identifiers that indicate the element should produce god tokens.
     *
     * @param elementIdentifier The identifier of the fighting element to create.
     * @param game The game instance to which the fighting element belongs.
     * @param ally The identifier of the ally player.
     * @param opponent The identifier of the opponent player.
     * @return A new instance of the specified FightingElement.
     * @throws GameElementCreationException if the identifier does not correspond to a known fighting element.
     */
    public FightingElement createElement(String elementIdentifier, Game game, int ally, int opponent) throws GameElementCreationException {
        boolean producesTokens = false;
        String fightingElementKey = elementIdentifier;

        if (!FIGHTING_ELEMENTS.contains(elementIdentifier)) {
            throw new GameElementCreationException(elementIdentifier);
        }

        // Check if the element identifier starts with 'G' indicating god token production
        if (elementIdentifier.charAt(0) == TOKEN_CHAR) {
            producesTokens = true;
            fightingElementKey = elementIdentifier.substring(1); // Remove the 'G' prefix
        }

        FightingElementParams params = new FightingElementParams(game, ally, opponent);
        FightingElement fightingElement = creators.get(fightingElementKey).apply(params); //gets the corresponding lambda function
        fightingElement.setProducesGodToken(producesTokens); // Creates a new instance every time with the provided parameters


        return fightingElement;

    }

}
