package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the third scenario : "Affaire de précision"
 *
 * @author freaxmind
 */
public class Scenario4Test {

    public String additionPrecise(String a, String b) {
        java.math.BigDecimal ap = new java.math.BigDecimal(a);
        java.math.BigDecimal bp = new java.math.BigDecimal(b);

        return ap.add(bp).toString();
    }

    @Test
    public void test() {
        assertTrue(additionPrecise("0.4", "0.6").equals("1.0"));
        assertTrue(additionPrecise("0.3", "0.6").equals("0.9"));
    }
}
