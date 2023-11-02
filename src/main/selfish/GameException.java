package selfish;

/**
 * creates the Game exception class
 * @author sofia hu
 * @version 1.0
 */
public class GameException extends Exception {
    
    /**
     * creates the game exception object
     * @param msg custom exception message
     * @param e the exception
     */
    public GameException(String msg, Throwable e) {
        super(msg, e);
    }
}
