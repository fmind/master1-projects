package models;

import components.ConnectionHandler;
import components.GameException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Mission of the game
 *
 * @note stored in a database, but you don't have to know that
 *
 * @author freaxmind
 */
public class Scenario implements Serializable {

    private Integer id;
    private String name;
    private String test;
    private String story;

    public Scenario(int id, String name, String test, String story) {
        this.id = id;
        this.name = name;
        this.test = test;
        this.story = story;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getTest() {
        return this.test;
    }

    public String getStory() {
        return this.story;
    }

    /**
     * Retrieve a scenario from its id
     *
     * @param id
     * @return an instance of scenario if found else null
     * @throws GameException
     */
    public static Scenario findByID(int id) throws GameException {
        Connection connection = ConnectionHandler.getConnection();

        try {
            // query
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Scenarios WHERE ID = '" + id + "'";
            ResultSet rs = statement.executeQuery(query);

            // fetch
            if (rs.next()) {
                return new Scenario(id, rs.getString("name"), rs.getString("test"), rs.getString("story"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new GameException("Impossible de récupérer le scénario " + id + ": " + ex);
        }
    }

    @Override
    public String toString() {
        return "Scenario: " + this.getName();
    }
}
