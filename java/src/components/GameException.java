package components;

/**
 * Exception throws when the game is running
 *
 * @author freaxmind
 */
public class GameException extends Exception {

    public GameException(String msg) {
        super(msg);
    }

    @Override
    public String toString() {
        return "Game exception: " + this.getMessage();
    }
}
