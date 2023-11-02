package selfish.deck;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.Serializable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import selfish.GameException;
import java.util.Collection;
import java.util.Collections;

/**
 * creates the deck of cards for the game
 * @author Sofia Hu
 * @version 1.0
 */
public abstract class Deck implements Serializable {
    /**
     * collection of cards
     */
    private Collection<Card> cards;

    /**
     * unique ID for the Deck
     */
    private final static long serialVersionUID = 1l;
    
    /**
     * creates a Deck
     */
    protected Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * creates a list for the cards
     * @throws GameException when there is an error in reading the file
     * @param path file path to the list of cards
     * @return the file as a list of cards
     */
    protected static List<Card> loadCards(String path) throws GameException {
        List<Card> loadedCards = new ArrayList<>();
        try {
            File cardsFile = new File(path);
            Scanner cardsFileReader = new Scanner(cardsFile);
            cardsFileReader.nextLine();
            while (cardsFileReader.hasNextLine()) {
                String cardData = cardsFileReader.nextLine();
                loadedCards.addAll(Arrays.asList(stringToCards(cardData)));
            }
            cardsFileReader.close();
        } 

        catch (FileNotFoundException e) {
            throw new GameException("There was an error when handling the file", e);
        }

        return loadedCards;
    }

    /**
     * creates an array for the cards
     * @param str each of the card types
     * @return array of cards
     */
    protected static Card[] stringToCards(String str) {
        String[] cardTypes = str.split("; ");
        int quantity = Integer.parseInt(cardTypes[2]);
        Card[] cards = new Card[quantity];
        for (int i = 0; i < quantity; i++) {
            cards[i] = new Card(cardTypes[0], cardTypes[1]);
        }
        return cards;
    }

    /**
     * adds the Card chosem to the collection of cards in the Deck
     * @param card chosen card
     * @return number of cards in the collection
     */
    public int add(Card card) {
        this.cards.add(card);
        return this.cards.size();
    }

    /**
     * adds a list of cards to the collection of cards in the Deck
     * @param cards chosen cards
     * @return number of cards in the collection
     */
    protected int add(List<Card> cards) {
        this.cards.addAll(cards);
        return this.cards.size();
    }

    /**
     * drawing a card
     * @return the card drawn
     */
    public Card draw() {
        if (this.cards.size() == 0) throw new IllegalStateException();

        List<Card> cardList = new ArrayList<>(this.cards);
        Card cardRemoved = cardList.get(cardList.size()-1);
        this.cards.remove(cardRemoved);
        return cardRemoved;
    }

    /**
     * removing a card
     * @param card card chosen to be removed
     */
    public void remove(Card card) {
        this.cards.remove(card);
    }

    /**
     * shuffling the cards in your Deck
     * @param random type of randomizer we use to shuffle
     */
    public void shuffle(Random random) {
        Collections.shuffle((List<Card>)this.cards, random);
    }

    /**
     * size of the collection of cards
     * @return size of the Deck
     */
    public int size() {
        return this.cards.size();
    }
}
