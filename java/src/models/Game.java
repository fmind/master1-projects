package models;

import components.GameException;
import config.GameConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Game played by the user
 *
 * @note only one game per installation (like pokemon on Gameboy)
 *
 * @author freaxmind
 */
public class Game implements Serializable {

    private boolean posted;         // game is posted on the server
    private Difficulty difficulty;
    private LinkedList<Achievement> achievements;

    public static enum Difficulty {

        EASY, NORMAL, HARD
    };

    public Game(Difficulty difficulty) {
        this.posted = false;
        this.difficulty = difficulty;
        this.achievements = new LinkedList<>();
    }

    public boolean isPosted() {
        return this.posted;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    /**
     * Sum up all the score of achievements
     *
     * @return final score
     */
    public int getScoreFinal() {
        int scoreFinal = 0;

        for (Achievement achievement : this.achievements) {
            scoreFinal += achievement.getScore();
        }

        return scoreFinal;
    }

    /**
     * Return the last achievement of this game
     *
     * @return last achievement or null if the list is empty
     */
    public Achievement getLastAchievement() {
        if (!this.achievements.isEmpty()) {
            return this.achievements.getLast();
        } else {
            return null;
        }
    }

    /**
     * Return the next scenario to play (and its achievement)
     *
     * @return next scenario and null if their is no scenario
     */
    public Achievement getNext() throws GameException {
        // id are linear
        int id = this.achievements.size() + 1;
        Scenario scenario = Scenario.findByID(id);

        if (scenario != null) {
            Achievement achievement = new Achievement(scenario, this.getStartingTries());
            this.achievements.add(achievement);
            return achievement;
        } else {
            return null;
        }
    }

    /**
     * Return the number of starting tries from the game difficulty
     *
     * @return starting tries
     */
    public int getStartingTries() {
        switch (this.difficulty) {
            case EASY:
                return 20;
            case NORMAL:
                return 10;
            case HARD:
                return 5;
            default:
                return 10;
        }
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    /**
     * Save this game
     *
     * @throws IOException
     */
    public void save() throws GameException {
        try {
            // write the file (object serialization)
            FileOutputStream file = new FileOutputStream(GameConfig.SAVE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new GameException("Impossible de sauvegarder la partie: " + ex);
        }
    }

    /**
     * Test if the game has a save file
     *
     * @return true if the game has a save file
     */
    public static boolean hasSave() {
        return new File(GameConfig.SAVE_PATH).exists();
    }

    /**
     * Retrieve the last game played
     *
     * @return last game played
     */
    public static Game getLastGame() throws GameException {
        try {
            // read the file (object serialization)
            FileInputStream file = new FileInputStream(GameConfig.SAVE_PATH);
            ObjectInputStream in = new ObjectInputStream(file);
            Game game = (Game) in.readObject();
            in.close();

            return game;
        } catch (IOException | ClassNotFoundException ex) {
            throw new GameException("Impossible de récupérer la dernière partie: " + ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Partie: difficulté=").append(this.difficulty);
        str.append(", nombre de récompense=").append(this.achievements.size());

        return str.toString();
    }
}
