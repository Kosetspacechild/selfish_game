package selfish.deck;

/**
 * creates the oxygen card
 * @author sofia hu
 * @version 1.0
 */
public class Oxygen extends Card {

    /**
     * holds the value of the oxygen
     */
    private int value;

    /**
     * unique ID for Oxygen
     */
    private final static long serialVersionUID = 3l;

    /**
     * creates oxygen with a value of either 1 or 2
     * @param value possible values that oxygen can be
     */
    public Oxygen(int value) {
        super(GameDeck.OXYGEN, GameDeck.OXYGEN);
        this.value = value;
    }

    /**
     * gets the value for oxygen
     * @return value of oxygen
     */
    public int getValue() {
        return this.value;
    }

    /**
     * returns a string representation for oxygen
     * @return string representation of oxygen(1)/oxygen(2)
     */
    public String toString() {
        if (value == 1) {
            return GameDeck.OXYGEN_1;
        }
        else {
            return GameDeck.OXYGEN_2;
        }
    }
}
