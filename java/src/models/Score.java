package models;

/**
 * High score of a player
 *
 * @author freaxmind
 */
public class Score {

    private int ranking;
    private String name;
    private int score;
    private String date;    // string representation, we don't use date algebra anyway

    public Score(int ranking, String name, int score, String date) {
        this.ranking = ranking;
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public int getRanking() {
        return this.ranking;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public String getDate() {
        return this.date;
    }
}
