package components;

import config.GameConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handle the unique connection to the JDBC database
 *
 * @todo add a shutdown hook
 * @author freaxmind
 */
public class ConnectionHandler {

    private static Connection connection = null;
    private static ConnectionHandler instance = null;

    public static void start() throws GameException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new GameException("Le connecteur SQLite pour JDBC n'est pas disponible");
        }

        try {
            connection = DriverManager.getConnection(GameConfig.SCENARIO_DB);
        } catch (SQLException ex) {
            throw new GameException("Impossible de se connecter à la base de données: " + ex);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
