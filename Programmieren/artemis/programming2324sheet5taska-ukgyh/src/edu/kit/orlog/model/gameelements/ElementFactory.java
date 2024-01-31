package edu.kit.orlog.model.gameelements;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract factory class for creating game elements.
 * This class serves as a generic blueprint for factories that produce various types of
 * game elements. It uses a map to associate string identifiers with functions that instantiate game elements.
 *
 * The class is designed to be extended by specific element factory classes, such as
 * FightingElementFactory and GodFavorFactory, which implement the {@code initElements}
 * method to populate the map with specific element creators.
 *
 * @param <T> The type of game element this factory produces.
 * @param <P> The type of parameters required to create the game elements.
 * @author ukgyh
 */
public abstract class ElementFactory<T, P> {
    protected final Map<String, Function<P, T>> creators = new HashMap<>();
    /**
     * Constructor for ElementFactory. Initializes the factory and calls the {@code initElements}
     * method to populate the creators map.
     */
    public ElementFactory() {
        initElements();
    }

    /**
     * Abstract method to initialize the creators map with specific game element constructors.
     * This method should be implemented by subclasses to define how different game elements
     * are created based on their type and parameters.
     */
    protected abstract void initElements();
}
