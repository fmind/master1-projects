package controllers;

import components.CheckResult;
import components.CodeChecker;
import components.GameException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Achievement;
import models.Game;
import views.GameFrame;

/**
 * Control each step of the game
 *
 * @note singleton
 * @author freaxmind
 */
public class GameController {

    private Game game;
    private GameFrame frame;
    private static GameController instance = null;

    private GameController() {
        this.game = null;
        this.frame = null;
    }

    /**
     * Singleton method
     *
     * @return unique instance of controller
     */
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }

        return instance;
    }

    /**
     * Set the frame (user interface)
     *
     * @param frame
     */
    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }

    /**
     * Load a game and prepare the frame
     *
     * @param game
     */
    public void loadGame(Game game) {
        this.game = game;
        Achievement achievement = this.game.getLastAchievement();

        // magic method !
        this.loadAchievement(achievement);
    }

    /**
     * Save the game (with its chrono)
     *
     * @param chrono time in seconds
     */
    public void saveGame(int chrono) {
        Logger.getLogger("").log(Level.INFO, "Sauvegarde de la partie ...");

        try {
            // save chrono only if the mission is not finish
            Achievement last = this.game.getLastAchievement();
            if (last != null && !last.isAchieved()) {
                last.setChrono(chrono);
            }

            this.game.save();
        } catch (GameException ex) {
            Logger.getLogger("").log(Level.SEVERE, ex.toString());
            System.exit(1);
        }
    }

    /**
     * Load an achievement and prepare the frame
     *
     * @todo this method has to much magic, need to split it
     * @param achievement achievement to load or null to load the next
     */
    private void loadAchievement(Achievement achievement) {
        // Case 1: mission is not finish, load it as it was saved
        if (achievement != null && !achievement.isAchieved()) {
            this.frame.loadAchievement(achievement);
            return;
        }

        // Case 2: mission is finish, load the next mission
        try {
            achievement = this.game.getNext();

            if (achievement != null) {
                // one scenario found, load it
                this.frame.loadAchievement(achievement);
                return;
            }
        } catch (GameException ex) {
            Logger.getLogger("").log(Level.SEVERE, ex.toString());
            System.exit(1);
        }

        // Case 3: end of the game
        // display and post the end score
        this.frame.displayEndGame(this.game.getScoreFinal());
        this.postScore();
    }

    /**
     * Post the user score. Use it at the end of the game.
     *
     * @note automatic check if the game is already posted
     */
    private void postScore() {
        if (this.game.isPosted()) {
            return;
        }

        String name = this.frame.askName();

        // post only if the name is not empty
        if (name != null && !name.isEmpty()) {
            // can be post next time if controller encounter an error
            if (HighScoreController.getInstance().post(name, this.game.getScoreFinal())) {
                try {
                    this.game.save();
                    this.game.setPosted(true);
                    Logger.getLogger("").log(Level.INFO, "Meilleur score posté");
                } catch (GameException ex) {
                    Logger.getLogger("").log(Level.WARNING, ex.getMessage());
                }
            }
        }
    }

    /**
     * Test a user code
     *
     * @param code
     * @param lucky
     * @return result a the test
     */
    public void makeTry(String code, int chrono, boolean lucky) {
        Achievement achievement = this.game.getLastAchievement();
        achievement.addTry(lucky);

        // test the code with the test
        CheckResult result = new CheckResult();
        try {
            result = CodeChecker.check(code, achievement.getScenario().getTest());
            Logger.getLogger("").log(Level.INFO, "R\u00e9sultat: {0}", result);
        } catch (GameException ex) {
            // case not supported, need immediate shutdown to avoid strange behaviour
            Logger.getLogger("").log(Level.SEVERE, "Erreur lors de l''ex\u00e9cution du code utilisateur: {0}", ex);
            System.exit(1);
        }

        // continue or end the scenario
        if (achievement.getRemainingTries() <= 0 || result.isSuccess()) {
            // end of the scenario
            // compute the score and save the game
            achievement.setChrono(chrono);
            achievement.setAchieved(true);
            achievement.setSuccess(result.isSuccess());
            achievement.computeScore(this.game.getDifficulty());

            try {
                this.game.save();
            } catch (GameException ex) {
                Logger.getLogger("").log(Level.SEVERE, ex.toString());
                System.exit(1);
            }

            // display the result or the mission and load the next
            this.frame.displayEndScenario(achievement);
            this.loadAchievement(null);
        } else {
            // continue the scenario
            // display the result of the test
            this.frame.displayResult(result, achievement.getRemainingTries());
        }
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
