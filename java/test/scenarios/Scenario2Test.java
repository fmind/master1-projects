package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the second scenario : "Sécuriser les communications"
 *
 * @author freaxmind
 */
public class Scenario2Test {

    public boolean verifier(String pass) {
        boolean num = false;
        boolean upper = false;
        boolean lower = false;

        if (pass.length() < 10) {
            return false;
        }

        for (int i = 0; i < pass.length(); i++) {
            num = num || Character.isDigit(pass.charAt(i));
            upper = upper || Character.isUpperCase(pass.charAt(i));
            lower = lower || Character.isLowerCase(pass.charAt(i));
        }

        return num && upper && lower;
    }

    @Test
    public void test() {
        assertTrue(verifier("4ZertYuio") == false);
        assertTrue(verifier("A1213pokl") == false);
        assertTrue(verifier("QSDFG123456") == false);
        assertTrue(verifier("JAOSovjePPP") == false);
        assertTrue(verifier("bAse730onE4") == true);
    }
}
