package selfish.deck;

import selfish.GameException;

/**
 * creates the space deck that holds the space cards
 * @author sofia hu
 * @version 1.0
 */
public class SpaceDeck extends Deck {

    /**
     * identifier for Asteroid field
     */
    public final static String ASTEROID_FIELD = "Asteroid field";

    /**
     * identifier for Blank space 
     */
    public final static String BLANK_SPACE = "Blank space";

    /**
     * identifier for Cosmic radiation
     */
    public final static String COSMIC_RADIATION = "Cosmic radiation";

    /**
     * identifier for Gravitational anomaly
     */
    public final static String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";

    /**
     * identifier for Hyperspace
     */
    public final static String HYPERSPACE = "Hyperspace";

    /**
     * identifier for Meteroid
     */
    public final static String METEOROID = "Meteoroid";

    /**
     * identifier for Mysterious nebula
     */
    public final static String MYSTERIOUS_NEBULA = "Mysterious nebula";

    /**
     * identifier for Solar flare
     */
    public final static String SOLAR_FLARE = "Solar flare";

    /**
     * identifier for Useful junk
     */
    public final static String USEFUL_JUNK = "Useful junk";

    /**
     * identifier for Wormhole
     */
    public final static String WORMHOLE = "Wormhole";

    /**
     * unique ID for SpaceDeck
     */
    private final static long serialVersionUID = 4l;

    /**
     * creates space deck
     */
    public SpaceDeck() {
        super();
    }

    /**
     * creates space deck with filepath
     * @throws GameException when there is an error in reading the file
     * @param path pointer towards the file that contains the cards in space deck
     */
    public SpaceDeck(String path) throws GameException {
        super.add(loadCards(path));
    }
}
