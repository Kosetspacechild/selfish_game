package selfish.deck;

import java.io.Serializable;

/**
 * creates the cards for the game
 * @author Sofia Hu
 * @version 1.0
 */
public class Card implements Comparable<Card>, Serializable {

    /**
     * name for the cards
     */
    private String name;

    /**
     * descriptions for the cards
     */
    private String description;

    /**
     * unique ID for Cards, as it is serializable
     */
    private final static long serialVersionUID = 0l;

    /**
     * creates an object for the cards
     * @param name name for the card
     * @param description describes the effects of the cards
     */
    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * returns the card's description
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * returns the string representation of the Card
     * @return string representation
     */
    public String toString() {
        return this.name;
    }

    /**
     * compares two cards together
     * @param o the card youre comparing yours to
     * @return -1 if card is before o, 0 if it's the same and 1 if it's after
     */
    public int compareTo(Card o) {
        if (this instanceof Oxygen && o instanceof Oxygen) {
            return Integer.compare(((Oxygen)this).getValue(), ((Oxygen)o).getValue());
        }
        else {
            return this.name.compareTo(o.name);
        }
    }

}
