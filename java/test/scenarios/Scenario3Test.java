package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the third scenario : "Vente automatique"
 *
 * @author freaxmind
 */
public class Scenario3Test {

    public int vendre(int prix_vendeur, int augmentation_vendeur, int prix_acheteur, int reduction_acheteur) {
        while (prix_vendeur <= prix_acheteur) {
            prix_vendeur += augmentation_vendeur;

            if (prix_vendeur >= prix_acheteur) {
                return prix_acheteur;
            }

            prix_acheteur -= reduction_acheteur;
        }

        return prix_vendeur;
    }

    @Test
    public void test() {
        assertTrue(vendre(150, 50, 1000, 100) == 450);
        assertTrue(vendre(150, 50, 900, 100) == 400);
    }
}
