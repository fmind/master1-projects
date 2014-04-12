package scenarios;

import components.ConnectionHandler;
import components.GameException;
import models.Scenario;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Integration Test for class Scenario
 *
 * @author freaxmind
 */
public class ScenarioTest {

    @BeforeClass
    public static void setUpClass() throws GameException {
        ConnectionHandler.start();
    }

    @Test
    public void testFindByID() throws GameException {
        Scenario s1 = Scenario.findByID(1);
        Scenario s2 = Scenario.findByID(2);
        Scenario s3 = Scenario.findByID(3);
        Scenario s100 = Scenario.findByID(100);     // not exists

        assertNotNull(s1);
        assertNotNull(s2);
        assertNotNull(s3);
        assertNull(s100);
    }
}
