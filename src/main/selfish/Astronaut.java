package selfish;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.net.ssl.HandshakeCompletedEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

import selfish.deck.Card;
import selfish.deck.GameDeck;
import selfish.deck.Oxygen;
import selfish.deck.SpaceDeck;

/**
 * creates the players for the game
 * @author sofia hu
 * @version 1.0
 */
public class Astronaut implements Serializable {

    /**
     * name for the game the player partakes in
     */
    private GameEngine game;

    /**
     * name for the action cards
     */
    private List<Card> actions;

    /**
     * name for the oxygen cards
     */
    private List<Oxygen> oxygens;

    /**
     * name for the name of the player
     */
    private String name;

    /**
     * name for the collection of cards
     */
    private Collection<Card> track;

    /**
     * unique ID for the player
     */
    private final static long serialVersionUID = 5l;

    /**
     * creates player astronaut
     * @param name player name 
     * @param game game player partakes in
     */
    public Astronaut(String name, GameEngine game) {
        this.name = name;
        this.game = game;
        this.actions = new ArrayList<>();
        this.oxygens = new ArrayList<>();
        this.track = new ArrayList<>();
    }

    /**
     * adds to hand a card
     * @param card card that gets added
     */
    public void addToHand(Card card) {
        if (card instanceof Oxygen) {
            this.oxygens.add((Oxygen)card);
            this.oxygens.sort(Oxygen :: compareTo);
        }

        else {
            this.actions.add(card);
            this.actions.sort(Card :: compareTo);
        }
    }

    /**
     * adds a space card to your path
     * @param card space card added
     */
    public void addToTrack(Card card) {
        this.track.add(card);
    }

    /**
     * consumes oxygen(1)
     * @return nº of oxygen left in your hand
     */
    public int breathe() {
        if (isAlive() == false) throw new IllegalStateException();

        if (hasCard(GameDeck.OXYGEN_1) == 0) {
            Oxygen[] splitOxygen = game.splitOxygen(this.oxygens.get(0));
            this.oxygens.remove(0);
            this.oxygens.add(splitOxygen[0]);
            game.getGameDiscard().add(splitOxygen[1]);
        }

        else {
            this.oxygens.sort(Oxygen::compareTo);
            game.getGameDiscard().add(this.oxygens.get(0));
            this.oxygens.remove(0);
        }

        if (oxygenRemaining() == 0) {
            this.game.killPlayer(this);
        }

        return oxygenRemaining();
    }

    /**
     * tells the distance from players to ship
     * @return nº of spaces between player and ship
     */
    public int distanceFromShip() {
        return (6 - this.track.size());
    }

    /**
     * gets the list of action cards in your hands
     * @return list of action cards you have
     */
    public List<Card> getActions() {
        return actions;
    }

    /**
     * returns a string representation of the cards, where if:
     *  <pre> -enumerated is false, it returns:
     *      e.g, Laser blast, 2x Rocket booster, Tractor beam
     *  -enumerated is true, it returns:
     *      e.g, [A] Laser blast, [B] Rocket booster, [C] Tractor beam
     *  -excludeshields is false, it returns:
     *      e.g, Oxygen(2), 6x Oxygen(1); Hack suit, Hole in suit, Shield, Tether
     * -excludeShields is true, it returns:
     *      e.g, [A] Hack suit, [B[ Hole in suit, [C] Tether </pre>
     * @param enumerated enumerates the list of actions
     * @param excludShields excludes shiels from list
     * @return list of the actions as a string
     */
    public String getActionsStr(boolean enumerated, boolean excludShields) {
        String actionsString = "";
        Set<String> actionSet = new LinkedHashSet<>();

        for(Card action : this.actions) {
            if (!actionSet.contains(action.toString())) {
                actionSet.add(action.toString());
            }
        }

        if (enumerated == true) {
            char enumChar = 'A';

            for (String actionCard : actionSet) {
            
                if (actionCard.equals(GameDeck.SHIELD) && excludShields == true) {
                    continue;
                }

                actionsString += "[" + enumChar + "] " + actionCard + ", ";
                enumChar += 1;
            }
        }

        else {
            for (String actionCard : actionSet) {
                
                if (actionCard.toString().equals(GameDeck.SHIELD) && excludShields == true) {
                    continue;
                }

                int numberOfCard = hasCard(actionCard.toString());

                if (numberOfCard == 1) {
                    actionsString += actionCard.toString() + ", ";
                }

                else {
                    actionsString += numberOfCard + "x " + actionCard.toString() + ", ";

                }
            }
        }

        if (actionsString.length() == 0) {
            return actionsString;
        }

        return actionsString.substring(0, actionsString.length() - 2);
    }

    /**
     * gets the hand of cards
     * @return a list of the cards in player's hand
     */
    public List<Card> getHand() {
        List<Card> handCards = new ArrayList<>(this.actions);
        handCards.addAll(this.oxygens);
        handCards.sort(Card :: compareTo);
        return handCards;
    }

    /**
     * expressess the players state
     * @return a sring listing what the player has in their hands
     */
    public String getHandStr() {
        String handString = "";
        int numberOfOxy1 = hasCard(GameDeck.OXYGEN_1);
        int numberOfOxy2 = hasCard(GameDeck.OXYGEN_2);

        
        if (hasCard(GameDeck.OXYGEN_2) >= 1) {
            if (numberOfOxy2 == 1) {
                handString += GameDeck.OXYGEN_2;
            }

            else {
                handString += numberOfOxy2 + "x " + GameDeck.OXYGEN_2;
            }

            if (numberOfOxy1 >= 1) {
                handString += ", ";
            }

        }

        if (hasCard(GameDeck.OXYGEN_1) >= 1) {
            if (numberOfOxy1 == 1) {
                handString += GameDeck.OXYGEN_1;
            }

            else {
                handString += numberOfOxy1 + "x " + GameDeck.OXYGEN_1;
            }
        }

        handString +=  "; " + getActionsStr(false, false);

        return handString;
    }

    /**
     * gives the collection of cards that make up the track
     * @return full track
     */
    public Collection<Card> getTrack() {
        return track;
    }

    /**
     * removes a card from a player's hand
     * @param card chosen card to be removed
     */
    public void hack(Card card) {
        if (getHand().contains(card)) {
            if (card instanceof Oxygen) {
                this.oxygens.remove(card);

                if (this.oxygens.size() == 0) {
                    game.killPlayer(this);
                }
            }

            else {
                this.actions.remove(card);
            }
        }

        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * removes the card from the string that contains it
     * @param card chosen card to be removed
     * @return chosen card that was removed
     */
    public Card hack(String card) {
        if (card == null) throw new IllegalArgumentException();

        if (getHandStr().contains(card)) {
            for (Card cardsCard : getHand()) {
                if (cardsCard.toString().equals(card)) {
                    hack(cardsCard);

                    return cardsCard;
                }
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * tells player how many cards of a specific type you have
     * @param card specific card player asked about
     * @return nº of cards the player has
     */
    public int hasCard(String card) {
        int cardQuantity = 0;

        for (Card cardsCard : getHand()) {
            if (cardsCard.toString().equals(card)) {
                cardQuantity += 1;
            }
        }
        return cardQuantity;
    }

    /**
     * <pre> if there is a Solar flare behind the player:
     *      -returns true
     * if not:
     *      -returns false </pre>
     * @return boolean value
     */
    public boolean hasMeltedEyeballs() {
        if (peekAtTrack().toString().equals(SpaceDeck.SOLAR_FLARE)) {
            return true;
        }

        return false;
    }

    /**
     * returns true if the player reaches the ship
     * @return boolean value
     */
    public boolean hasWon() {
        if (distanceFromShip() == 0  && isAlive() == true){
            return true;
        }

        return false;
    }

    /**
     * returns false when the player runs out of oxygen
     * @return boolean value
     */
    public boolean isAlive() {
        if (this.oxygens.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * removes and returns the topmost card from player's track
     * @return topmost card from player's track
     */
    public Card laserBlast() {
        if (this.track.size() == 0) throw new IllegalArgumentException();

        Card topCard = peekAtTrack();
        this.track.remove(peekAtTrack());

        return topCard;
    }

    /**
     * shows the ammount of oxygen the player has left
     * @return nº of oxygen player has left
     */
    public int oxygenRemaining() {
        int oxygenQuantity = 0;

        for (Oxygen oxygensOxygen : this.oxygens) {
            if (oxygensOxygen.getValue() == 2) {
                oxygenQuantity += 2;
            }

            if (oxygensOxygen.getValue() == 1) {
                oxygenQuantity += 1;
            }
        }

        return oxygenQuantity;
    }

    /**
     * <pre>returns the card directly behind the player
     * if the player is in the starting space it returns null</pre>
     * @return card behind player
     */
    public Card peekAtTrack() {
        List<Card> trackList = new ArrayList<>(this.track);

        if (this.track.size() == 0) {
            return null;
        }

        return trackList.get(this.track.size() - 1);
    }

    /**
     * removes oxygen(1) from a players hand and gives it to the one who used siphon
     * @return oxygen(1) to the player who used siphon
     */
    public Oxygen siphon() {
        if (this.oxygens.size() == 0) throw new IllegalStateException();
        Oxygen removedOxygen = this.oxygens.get(0);

        if (removedOxygen.getValue() == 1) {
            this.oxygens.remove(removedOxygen);

            if (oxygenRemaining() == 0) {
                this.game.killPlayer(this);
            }

            return removedOxygen;
        }

        else {
            Oxygen[] splitRemovedOxygen = game.splitOxygen(removedOxygen);
            this.oxygens.add(splitRemovedOxygen[0]);
            this.oxygens.remove(removedOxygen);

            return splitRemovedOxygen[1];
        }
    }

    /**
     * removes a random card from one player and gives it to the one who used steal
     * @return random card stolen from player's hand
     */
    public Card steal() {
        Random random = new Random();
        List<Card> fullhand = new ArrayList<>(getHand());
        Card stolenCard = fullhand.get(random.nextInt(fullhand.size()));

        hack(stolenCard);

        return stolenCard;
    }

    /**
     * swap's player's track with the swapee's track
     * @param swapee player whose track gets swapped
     */
    public void swapTrack(Astronaut swapee) {
        List<Card> swapperTrack = new ArrayList<>(this.track);
        List<Card> swapeeTrack = new ArrayList<>(swapee.track);

        this.track = swapeeTrack;
        swapee.track = swapperTrack;
    }

    /**
     * returns a string saying player's name (is dead) if the player is dead,
     * otherwise just the player's name
     * @return string representation of the player
     */
    public String toString() {
        if (isAlive() == true) {
            return this.name;
        }
        
        String msg = this.name + " (is dead)";
        return msg;
    }
 }
