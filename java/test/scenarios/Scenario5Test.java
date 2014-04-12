package scenarios;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the third scenario : "La langue des humains"
 *
 * @author freaxmind
 */
public class Scenario5Test {

    public String traduire(int number) {
        String[] first20 = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] tens = {null, null, "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

        if (number < first20.length) {
            return first20[number];
        } else if (number < 100) {
            if (number % 10 == 0) {
                return tens[number / 10];
            } else {
                return tens[number / 10] + " " + first20[number % 10];
            }
        } else {
            return first20[number / 100] + " hundred " + traduire(number % 100);
        }
    }

    @Test
    public void test() {
        assertTrue(traduire(4).equals("four"));
        assertTrue(traduire(143).equals("one hundred forty three"));
        assertTrue(traduire(12).equals("twelve"));
        assertTrue(traduire(101).equals("one hundred one"));
        assertTrue(traduire(212).equals("two hundred twelve"));
    }
}
