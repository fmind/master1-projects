package config;

/**
 * Game configurations
 *
 * @author freaxmind
 */
public class GameConfig {

    // directories
    public static final String USER_DIR = "SAVE/";
    public static final String APP_DIR = "DATA/";
    public static final String MUSIC_DIR = APP_DIR + "/music/";
    // files
    public static final String LOG_PATH = USER_DIR + "dev.txt";
    public static final String SAVE_PATH = USER_DIR + "game.ser";
    public static final String APP_ICON = APP_DIR + "logo.png";
    public static final String SCENARIO_DB = "jdbc:sqlite:" + APP_DIR + "scenario.sqlite";
    // URL
    public static final String SCORE_SERVER_URL = "http://127.0.0.1:9000/";
}
