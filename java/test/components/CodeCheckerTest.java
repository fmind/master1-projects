package components;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for class CodeChecker
 *
 * @author freaxmind
 */
public class CodeCheckerTest {

    @Test
    public void testNoTest() throws GameException {
        CheckResult result = CodeChecker.check("", "");

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof CompilerError);
    }

    @Test
    public void testOKWithoutCode() throws GameException {
        String testTrue = ""
                + "assert 1 == 1;\n"
                + "assert \"abc\" == \"abc\";\n"
                + "assert true == true;\n";

        CheckResult result = CodeChecker.check("", testTrue);

        assertNull(result.getError());
    }

    @Test
    public void testNotOKWithoutCode() throws GameException {
        String testFalse = "assert 1 == 10;\n"
                + "assert \"abc\" == \"abc\";\n"
                + "assert true == true;\n";

        CheckResult result = CodeChecker.check("", testFalse);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof AssertionError);
        assertTrue(result.getError().getMessage().contains("n°1"));
    }

    @Test
    public void testInvalidWithoutCode() throws GameException {
        String testInvalid = ""
                + "a;\n"
                + "!;\n";

        CheckResult result = CodeChecker.check("", testInvalid);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof CompilerError);
    }

    @Test
    public void testMissingCode() throws GameException {
        String testMissing = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";

        CheckResult result = CodeChecker.check("", testMissing);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof CompilerError);
    }

    @Test
    public void testOKWithCode() throws GameException {
        String testTrue = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";
        String codeOK = ""
                + "int addition(int a, int b) {\n"
                + "return a+b;\n"
                + "}\n";

        CheckResult result = CodeChecker.check(codeOK, testTrue);

        assertNull(result.getError());
    }

    @Test
    public void testNotOKWithCode() throws GameException {
        String testFalse = ""
                + "assert addition(0, 0) == 0;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";
        String codeNotOK = ""
                + "int addition(int a, int b) {\n"
                + "return a-b;\n" // soustraction au lieu de addition
                + "}\n";

        CheckResult result = CodeChecker.check(codeNotOK, testFalse);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof AssertionError);
        assertTrue(result.getError().getMessage().contains("n°2"));
    }

    @Test
    public void testInvalidWithCode() throws GameException {
        String testFalse = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";
        String codeInvalid = ""
                + "int addition(int a, int b) {\n"
                + "a;\n"
                + "}\n";

        CheckResult result = CodeChecker.check(codeInvalid, testFalse);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof CompilerError);
    }

    @Test
    public void testRuntimeErrorWithCode() throws GameException {
        String testFalse = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";
        String codeInvalid = ""
                + "int addition(int a, int b) {\n"
                + "int c = a / 0;"
                + "return a;\n"
                + "}\n";

        CheckResult result = CodeChecker.check(codeInvalid, testFalse);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof RuntimeError);
    }

    @Test
    public void testOutputWithoutCode() throws GameException {
        String testOutput = "System.out.println(\"Coucou\");\n";

        CheckResult result = CodeChecker.check("", testOutput);

        assertEquals("Coucou\n", result.getOutput());
    }

    @Test
    public void testOutputWithCode() throws GameException {
        String testOK = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 5;\n"
                + "assert addition(10, -3) == 7;\n";
        String codeOK = ""
                + "int addition(int a, int b) {\n"
                + "System.out.println(a + \"+\" + b);"
                + "return a+b;\n"
                + "}\n";

        CheckResult result = CodeChecker.check(codeOK, testOK);

        assertNull(result.getError());
        assertEquals("1+1\n2+3\n10+-3\n", result.getOutput());
    }

    @Test
    public void testOutputWithError() throws GameException {
        String testOK = ""
                + "assert addition(1, 1) == 2;\n"
                + "assert addition(2, 3) == 6;\n" // error here
                + "assert addition(10, -3) == 7;\n";
        String codeOK = ""
                + "int addition(int a, int b) {\n"
                + "System.out.println(a + \"+\" + b);"
                + "return a+b;\n"
                + "}\n";

        CheckResult result = CodeChecker.check(codeOK, testOK);

        assertNotNull(result.getError());
        assertTrue(result.getError() instanceof AssertionError);
        assertTrue(result.getError().getMessage().contains("n°2"));
        assertEquals("1+1\n2+3\n", result.getOutput());
    }
}
