package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the third scenario : "Le goût de la perfection"
 *
 * @author freaxmind
 */
public class Scenario6Test {

    public int[] rechercherPerfection(int n) {
        java.util.ArrayList<Integer> parfaits = new java.util.ArrayList<>();

        for (int i = 2; i <= n; i++) {
            int somme = 0;

            for (int j = 1; j < i; j++) {
                if (i % j == 0) {
                    somme += j;
                }
            }

            if (somme == i) {
                parfaits.add(i);
            }
        }

        int[] res = new int[parfaits.size()];
        for (int k = 0; k < parfaits.size(); k++) {
            res[k] = parfaits.get(k);
        }

        return res;
    }

    @Test
    public void test() {
        int[] a1 = {6, 28};
        int[] a2 = {6, 28, 496, 8128};

        assertTrue(java.util.Arrays.equals(rechercherPerfection(28), a1));
        assertTrue(java.util.Arrays.equals(rechercherPerfection(10000), a2));
    }
}
