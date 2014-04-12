package controllers;

import components.GameException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Game;
import views.LauncherFrame;

/**
 * Controller of the launcher. Start or continue game. Display help message.
 *
 * @note singleton
 * @author freaxmind
 */
public class LauncherController {

    private LauncherFrame frame;
    private static LauncherController instance = null;

    private LauncherController() {
        this.frame = null;
    }

    /**
     * Singleton method
     *
     * @return unique instance of controller
     */
    public static LauncherController getInstance() {
        if (instance == null) {
            instance = new LauncherController();
        }

        return instance;
    }

    /**
     * Set the frame (user interface)
     *
     * @param frame
     */
    public void setFrame(LauncherFrame frame) {
        this.frame = frame;
    }

    /**
     * Start the new game (erase the previous save file)
     *
     * @param difficulty
     */
    public void newGame(Game.Difficulty difficulty) {
        Logger.getLogger("").log(Level.INFO, "Nouvelle partie (difficult\u00e9={0})", difficulty);

        // create and save the new game
        Game game = new Game(difficulty);
        try {
            game.save();
        } catch (GameException ex) {
            Logger.getLogger("").log(Level.SEVERE, ex.toString());
            System.exit(1);
        }

        // switch controller
        this.stop();
        GameController.getInstance().loadGame(game);
        GameController.getInstance().run();
    }

    /**
     * Continue the previous game of the user
     */
    public void continueGame() {
        Logger.getLogger("").log(Level.INFO, "Continuer la partie");

        // get the last game
        Game game = null;
        try {
            game = Game.getLastGame();
        } catch (GameException ex) {
            Logger.getLogger("").log(Level.SEVERE, ex.toString());
            System.exit(1);
        }

        // switch controller
        this.stop();
        GameController.getInstance().loadGame(game);
        GameController.getInstance().run();
    }

    /**
     * Quit the application
     */
    public void quit() {
        Logger.getLogger("").log(Level.INFO, "Quitter le jeu");

        System.exit(0);
    }

    /**
     * Run the user interface
     */
    public void run() {
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    /**
     * Stop the user interface
     */
    public void stop() {
        this.frame.setVisible(false);
    }
}
