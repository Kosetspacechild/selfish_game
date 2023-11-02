package selfish;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import selfish.deck.GameDeck;
import selfish.deck.Card;
import selfish.deck.Deck;
import selfish.deck.SpaceDeck;
import selfish.deck.Oxygen;
import java.io.*;

/**
 * creates game engine
 * @author sofia hu
 * @version 1.0 
 */
public class GameEngine implements Serializable {

    /**
     * shows the players that are alive
     */
    private Collection<Astronaut> activePlayers;

    /**
     * shows the players that are dead
     */
    private List<Astronaut> corpses;

    /**
     * player whose turn is in progress
     */
    private Astronaut currentPlayer;

    /**
     * shows that the game has started
     */
    private boolean hasStarted;

    /**
     * randomizer for shuffling
     */
    private Random random;

    /**
     * withdrawable action cards 
     */
    private GameDeck gameDeck;

    /**
     * disposed action cards
     */
    private GameDeck gameDiscard;

    /**
     * withdrawable space cards
     */
    private SpaceDeck spaceDeck;

    /**
     * disposed space cards
     */
    private SpaceDeck spaceDiscard;

    /**
     * unique ID for GameEngine
     */
    private final static long serialVersionUID = 6l;

    /**
     * creates game engine
     */
    private GameEngine() { }

    /**
     * creates game engine objects and shuffles game deck and space deck
     * @throws GameException when there is an error in the reading of the file
     * @param seed seed for the randomizer
     * @param gameDeck action cards for the game
     * @param spaceDeck space cards for the game
     */
    public GameEngine(long seed, String gameDeck, String spaceDeck) throws GameException {
        this.activePlayers = new ArrayList<>();
        this.corpses = new ArrayList<>();
        this.random = new Random(seed);
        this.gameDeck = new GameDeck(gameDeck);
        this.gameDiscard = new GameDeck();
        this.spaceDeck = new SpaceDeck(spaceDeck);
        this.spaceDiscard = new SpaceDeck();
        
        this.gameDeck.shuffle(random);
        this.spaceDeck.shuffle(random);
    }

    /**
     * creates new astronaut instances unless max players reached or game started already
     * @param player astronaut joining the game
     * @return nº of total astronauts
     */
    public int addPlayer(String player) {
        if (activePlayers.size() == 5 || hasStarted == true){
            throw new IllegalStateException();
        }
        Astronaut newasAstronaut = new Astronaut(player, this);
        this.activePlayers.add(newasAstronaut);
        return this.activePlayers.size();
    }

    /**
     * returns number of players alive after a turn ends
     * @return nº of players alive
     */
    public int endTurn() {
        if (this.currentPlayer.isAlive() == true) {
            this.activePlayers.add(currentPlayer);
        }
        this.currentPlayer = null;
        return this.activePlayers.size();
    }

    /**
     * returns true when someone wins or when everyone looses
     * @return boolean value
     */
    public boolean gameOver() {
        if (this.activePlayers.size() == 0 || getWinner() != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * gives a list of the players who were playing when game started
     * @return list of players
     */
    public List<Astronaut> getAllPlayers() {
        List<Astronaut> allPlayersList = new ArrayList<>(this.activePlayers);
        allPlayersList.addAll(corpses);
        
        if(this.currentPlayer != null && !allPlayersList.contains(this.currentPlayer)) {
            allPlayersList.add(this.currentPlayer);
        }

        return allPlayersList;
    }

    /**
     * gives the current player
     * @return current player
     */
    public Astronaut getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * gives the nº of players when the game started
     * @return nº of players when game started
     */
    public int getFullPlayerCount() {
        return getAllPlayers().size();
    }

    /**
     * gives deck of action cards
     * @return deck of action cards
     */
    public GameDeck getGameDeck() {
        return this.gameDeck;
    }

    /**
     * gives deck of cards discarded
     * @return deck of action cards discarded
     */
    public GameDeck getGameDiscard() {
        return this.gameDiscard;
    }

    /**
     * gives deck of space cards
     * @return deck of space cards
     */
    public SpaceDeck getSpaceDeck() {
        return this.spaceDeck;
    }

    /**
     * gives deck of space cards discarded
     * @return deck of space cards discarded
     */
    public SpaceDeck getSpaceDiscard() {
        return this.spaceDiscard;
    }

    /**
     * returns the winning player if there is one, if none then null
     * @return winning player or null
     */
    public Astronaut getWinner() {
        for (Astronaut player : getAllPlayers()) {
            if (player.hasWon() == true){
                return player;
            }
        }
        
        return null;
    }

    /**
     * updates player status to dead when player runs out of oxygen
     * @param corpse player who run out of oxygen
     */
    public void killPlayer(Astronaut corpse) {
        this.activePlayers.remove(corpse);
        for(Card actionCard : corpse.getActions()) {
            this.gameDiscard.add(actionCard);
        }

        corpse.getActions().clear();
        this.corpses.add(corpse);
    }

    /**
     * allows objects to be serialised out to a file, saving and resuming in-progress games
     * @param path pointer towards file where data is saved
     * @return GameEngine loaded from filepath
     * @throws GameException when an error occurs in loading the data from the file
     */
    public final static GameEngine loadState(String path) throws GameException {
        try {
            //deserialization
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            GameEngine loadedGameEngine = (GameEngine)in.readObject();
            in.close();
            file.close();
            return loadedGameEngine;
        }

        catch(IOException ex) {
            throw new GameException("An error occurred when handling the file", ex);
        }
         
        catch(ClassNotFoundException ex) {
            throw new GameException("An error occurred when deserializing", ex);
        }
    }

    /**
     * deck1 gets restocked with cards on deck2
     * @param deck1 deck to be restocked
     * @param deck2 deck used to restock
     */
    public void mergeDecks(Deck deck1, Deck deck2) {
        while (deck2.size() > 0) {
            deck1.add(deck2.draw());
        }
        deck1.shuffle(random);
    }

    /**
     * allows objects to be serialised out to a file, saving and resuming in-progress games
     * @param path pointer towards file where data is saved
     * @throws GameException when an error occurs in saving the data into the file
     */
    public void saveState(String path) throws GameException {
        try {  
            //serialization
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
        }

        catch(IOException ex) {
            throw new GameException("An error occured while handling the file", ex);
        }
    }

    /**
     * splits oxygen (2) into 2 oxygen(1)
     * @param dbl oxygen(2) to be split
     * @return array with 2 oxygen(1)
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() == 2) {
            if (gameDeck.size() >= 2) {
                return gameDeck.splitOxygen(dbl);
            }
            if (gameDiscard.size() >= 2) {
                return gameDiscard.splitOxygen(dbl);
            }
            else {
                Oxygen[] splitOxygens = new Oxygen[2];
                splitOxygens[0] = gameDeck.drawOxygen(1);
                splitOxygens[1] = gameDiscard.drawOxygen(1);
                gameDiscard.add(dbl);
                return splitOxygens;
            }
        }
        return null;
    }

    /**
     * deals cards to each character and sets hasStarted
     */
    public void startGame() {
        if (this.hasStarted == true) throw new IllegalStateException();
        if (activePlayers.size() < 2 || activePlayers.size() >5) throw new IllegalStateException();
        else {
            for (int i = 0; i < 4; i++) {
                for (Astronaut astronaut : activePlayers) {
                    astronaut.addToHand(gameDeck.draw());
                }
            }
            
            for (Astronaut astronaut : activePlayers) {
                astronaut.addToHand(gameDeck.drawOxygen(2));
                for (int i = 0; i < 4; i++) {
                    astronaut.addToHand(gameDeck.drawOxygen(1));
                }
            }
            hasStarted = true;
        }
    }

    /**
     * updates currentPlayer and activePlayers
     */
    public void startTurn() {
        if (!hasStarted || this.currentPlayer != null || gameOver() == true) throw new IllegalStateException();
        
        this.currentPlayer = this.activePlayers.iterator().next();
        this.activePlayers.remove(this.currentPlayer);
    }

    /**
     * reduces player's oxygen by 2, draws a card, adds card to player's track
     * (unless the card is Gravitational anomaly) and returns the drawn card
     * @param traveller player whose turn is the current one
     * @return drawn card
     */
    public Card travel(Astronaut traveller) {
        traveller.breathe();
        traveller.breathe();

        Card trackCard = spaceDeck.draw();
        if (trackCard.toString().equals(SpaceDeck.GRAVITATIONAL_ANOMALY)) {
            spaceDiscard.add(trackCard);
        }
        else {
            traveller.addToTrack(trackCard);
        }
        return trackCard;
    }
}
