package models;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Mission in progress or finish by a player
 *
 * @todo set scenario to transcient and handle an id
 * @author freaxmind
 */
public class Achievement implements Serializable {

    private Scenario scenario;
    private boolean achieved;           // mission is finish
    private boolean success;            // mission is a success
    private LinkedList<Boolean> tries;  // keep order of tries
    private int remainingTries;         // try before mission is over
    private int chrono;                 // in seconds
    private int score;                  // final score, 0 until the mission is not finish

    public Achievement(Scenario scenario, int startingTries) {
        this.scenario = scenario;
        this.achieved = false;
        this.success = false;
        this.tries = new LinkedList<>();
        this.remainingTries = startingTries;
        this.chrono = 0;
        this.score = 0;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public boolean isAchieved() {
        return this.achieved;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public int getRemainingTries() {
        return this.remainingTries;
    }

    public int getChrono() {
        return this.chrono;
    }

    public int getScore() {
        return this.score;
    }

    public boolean isLastTryLucky() {
        if (this.tries.isEmpty()) {     // avoid bug
            return false;
        }

        return this.tries.getLast();
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setChrono(int chrono) {
        this.chrono = chrono;
    }

    /**
     * Normal try count for -1, lucky try for -5.
     *
     * @note Can't be inferior to 0
     *
     * @param lucky
     */
    public void addTry(boolean lucky) {
        this.remainingTries -= (lucky) ? 5 : 1;

        if (this.remainingTries <= 0) {
            this.remainingTries = 0;
        }

        this.tries.add(lucky);
    }

    /**
     * Compute the score of this achievement
     *
     * @note max score/scenario = 1200 (difficulty hard, chrono 0s, lucky try)
     * @param difficulty difficulty of the game
     */
    public void computeScore(Game.Difficulty difficulty) {
        // not achieved or not a success => 0 !
        if (!this.isAchieved() || !this.success) {
            this.score = 0;
            return;
        }

        int sco = 0;
        int nbTries = this.tries.size();

        // add more tries depending on the difficulty (act as a coefficient)
        switch (difficulty) {
            case HARD:
                nbTries += 50;
                break;
            case NORMAL:
                nbTries += 20;
                break;
        }

        sco += nbTries * 10;

        if (this.tries.getLast()) { // lucky try double the score at this step
            sco *= 2;
        }

        // log(22027) = 100 => return a number between 100 (fast) and 0 (slow)
        sco += (this.chrono < 22027) ? 100 - Math.log(this.chrono) * 10 : 0;

        this.score = sco;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        str.append("Récompense du scénario: ").append(this.scenario.getName());

        if (this.achieved) {
            str.append(" TERMINE (succès=").append(this.success).append(") ");
            str.append(" score=").append(this.score);
        } else {
            str.append(" chrono=").append(this.chrono).append(" s");
        }

        return str.toString();
    }
}
