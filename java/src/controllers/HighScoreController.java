package controllers;

import config.GameConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Score;

/**
 * Post and get high score from a server using HTTP
 *
 * @note singleton
 * @author freaxmind
 */
public class HighScoreController {

    private static HighScoreController instance = null;

    private HighScoreController() {
    }

    /**
     * Singleton method
     *
     * @return unique instance of controller
     */
    public static HighScoreController getInstance() {
        if (instance == null) {
            instance = new HighScoreController();
        }

        return instance;
    }

    /**
     * Return the score on the server
     *
     * @return a list of score (ordered score desc)
     */
    public List<Score> getScores() {
        List<Score> scores = new LinkedList<>();

        try {
            URL top = new URL(GameConfig.SCORE_SERVER_URL + "top");
            URLConnection connection = top.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                String[] cols = inputLine.split(";");

                if (cols.length != 4) {
                    Logger.getLogger("").log(Level.WARNING, "Impossible de lire la ligne de score: {0}", inputLine);
                    continue;
                }

                Score score = new Score(Integer.valueOf(cols[0]), cols[1], Integer.valueOf(cols[2]), cols[3]);
                scores.add(score);
            }

            in.close();
        } catch (IOException ex) {
            Logger.getLogger("").log(Level.WARNING, "Impossible de r\u00e9cup\u00e9rer le top score: {0}", ex.getMessage());
        }

        return scores;
    }

    /**
     * Post a new score on the server
     *
     * @param name
     * @param score
     * @note use get method (URL)
     * @return true if the score is posted
     */
    public boolean post(String name, int score) {
        try {
            URL top = new URL(GameConfig.SCORE_SERVER_URL + "new/" + name + "/" + score);
            InputStream in = top.openStream();
            // we don't care about reading, just to post data (with get method)
            in.close();
        } catch (IOException ex) {
            Logger.getLogger("").log(Level.WARNING, "Impossible de poster le score: {0}", ex.getMessage());
            return false;
        }

        return true;
    }
}
