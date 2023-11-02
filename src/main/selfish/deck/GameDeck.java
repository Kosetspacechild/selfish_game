package selfish.deck;

import java.util.ArrayList;
import java.util.List;

import selfish.GameException;

/**
 * creates the game deck that holds the action cards
 * @author sofia hu
 * @version 1.0
 */
public class GameDeck extends Deck {

    /**
     * identifier for Hack suit 
     */
    public final static String HACK_SUIT = "Hack suit";

    /**
     * identifier for Hole in suit
     */
    public final static String HOLE_IN_SUIT = "Hole in suit";

    /**
     * identifier for Laser blast
     */
    public final static String LASER_BLAST = "Laser blast";
    
    /**
     * identifier for Oxygen
     */
    public final static String OXYGEN = "Oxygen";
    
    /**
     * identifier for Oxygen(1)
     */
    public final static String OXYGEN_1 = "Oxygen(1)";

    /**
     * identifier for Oxygen(2) 
     */
    public final static String OXYGEN_2 = "Oxygen(2)";

    /**
     * identifier for Oxygen siphon  
     */
    public final static String OXYGEN_SIPHON = "Oxygen siphon";

    /**
     * identifier for Rocket booster
     */
    public final static String ROCKET_BOOSTER = "Rocket booster";

    /**
     * identifier for Shield
     */
    public final static String SHIELD = "Shield";

    /**
     * identifier for Tether 
     */
    public final static String TETHER = "Tether";

    /**
     * identifier for Tractor beam 
     */
    public final static String TRACTOR_BEAM = "Tractor beam";

    /**
     * unique ID for GameDeck
     */
    private final static long serialVersionUID = 2l;

    /**
     * creates GameDeck
     */
    public GameDeck() {
        super();
    }

    /**
     * creates GameDeck with filepath
     * @throws GameException when there is an error in reading the file
     * @param path points towards the file containing the action cards
     */
    public GameDeck(String path) throws GameException {
        super.add(loadCards(path));
        for (int i = 0; i < 10; i++) {
            Oxygen oxygen_2 = new Oxygen(2);
            add(oxygen_2);
        }
        for (int i = 0; i < 38; i++) {
            Oxygen oxygen_1 = new Oxygen(1);
            add(oxygen_1);
        }
    }

    /**
     * draws oxygen
     * @param value oxygen's value, can be 1 or 2
     * @return drawn oxygen
     */
    public Oxygen drawOxygen(int value) {
        int deckSize = size();
        List<Card> cardsDrawn = new ArrayList<>();
        for (int i = 0; i < deckSize; i++){
            Card possibleOxygen = draw();
            if (possibleOxygen instanceof Oxygen) {
                if (possibleOxygen.compareTo(new Oxygen(value)) == 0) {
                    add(cardsDrawn);
                    return ((Oxygen)possibleOxygen);
                }
            }
        
        cardsDrawn.add(possibleOxygen);
        }

        add(cardsDrawn);
        throw new IllegalStateException();
    }

    /**
     * splits the oxygen
     * @param dbl oxygen with value 2
     * @return returns an array with the split oxygen
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() != 2) throw new IllegalArgumentException();
        
        Oxygen[] splitOxygens = new Oxygen[2];
        splitOxygens[1] = this.drawOxygen(1);
        splitOxygens[0] = this.drawOxygen(1);
        if (splitOxygens[0] == null || splitOxygens[1] == null) throw new IllegalStateException();
        this.add(dbl);
        return splitOxygens;
    }
}
