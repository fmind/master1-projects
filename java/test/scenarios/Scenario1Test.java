package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the first scenario : "Chargement du vaisseau"
 *
 * @author freaxmind
 */
public class Scenario1Test {

    public int repartirRecc(int[] poids, int idx, int pileA, int pileB) {
        if (idx == poids.length) {
            return Math.abs(pileA - pileB);
        }

        int A = repartirRecc(poids, idx + 1, pileA + poids[idx], pileB);
        int B = repartirRecc(poids, idx + 1, pileA, pileB + poids[idx]);

        if (A < B) {
            return A;
        } else {
            return B;
        }
    }

    public int repartir(int... poids) {
        return repartirRecc(poids, 0, 0, 0);
    }

    @Test
    public void test() {
        assertTrue(repartir(10, 10) == 0);
        assertTrue(repartir(10) == 10);
        assertTrue(repartir(5, 8, 13, 27, 14) == 3);
        assertTrue(repartir(5, 5, 6, 5) == 1);
        assertTrue(repartir(12, 30, 30, 32, 42, 49) == 9);
    }
}
