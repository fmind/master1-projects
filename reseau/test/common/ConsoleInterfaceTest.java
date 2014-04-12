package common;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the class ConsoleInterface
 * @author freaxmind
 */
public class ConsoleInterfaceTest {

    /**
     * Correct TFTP commands
     */
    @Test
    public void testTFTPOK() {
        System.out.println("Interface TFTP OK");
        
        ConsoleInterface ci = new ConsoleInterface(tftp.Client.PATTERNS);
        
        assertTrue(ci.match("help"));
        assertTrue(ci.match("connect 192.168.0.12"));
        assertTrue(ci.match("connect 1.2.3.4 6000"));
        assertTrue(ci.match("get test/toto.txt"));
        assertTrue(ci.match("put test/machine.png"));
        assertTrue(ci.match("quit"));
    }
    
    /**
     * Incorrect TFTP commands
     */
    @Test
    public void testTFTPNotOK() {
        System.out.println("Interface TFTP Not OK");
        
        ConsoleInterface ci = new ConsoleInterface(tftp.Client.PATTERNS);
        
        assertFalse(ci.match(""));
        assertFalse(ci.match("connect abc"));
        assertFalse(ci.match("connect 1.2.3.4 abc"));
        assertFalse(ci.match("get test/toto.txt 11"));
        assertFalse(ci.match("put %test/machine.png%"));
        assertFalse(ci.match("pouet"));
    }
    
    /**
     * Correct POP3 commands
     */
    @Test
    public void testPOP3OK() {
        System.out.println("Interface POP3 OK");
        
        ConsoleInterface ci = new ConsoleInterface(pop3.Client.PATTERNS);
        
        assertTrue(ci.match("help"));
        assertTrue(ci.match("connect 127.0.0.1"));
        assertTrue(ci.match("connect 255.255.255.255 1100"));
        assertTrue(ci.match("auth bob 123456"));
        assertTrue(ci.match("stat"));
        assertTrue(ci.match("collect"));
        assertTrue(ci.match("list"));
        assertTrue(ci.match("read 12"));
        assertTrue(ci.match("quit"));
    }
    
    /**
     * Incorrect POP3 commands
     */
    @Test
    public void testPOP3NotOK() {
        System.out.println("Interface POP3 Not OK");
        
        ConsoleInterface ci = new ConsoleInterface(pop3.Client.PATTERNS);
        
        assertFalse(ci.match("help aa 11"));
        assertFalse(ci.match("connect"));
        assertFalse(ci.match("connect abc"));
        assertFalse(ci.match("auth momo mumu mimi"));
        assertFalse(ci.match(" stat"));
        assertFalse(ci.match("collec"));
        assertFalse(ci.match("liste"));
        assertFalse(ci.match("read A"));
        assertFalse(ci.match(""));
    }
    
    /**
     * Split commands in action and parameters
     */
    @Test
    public void testSplit() {
        System.out.println("Interface TFTP Split");
        
        ConsoleInterface ci = new ConsoleInterface(pop3.Client.PATTERNS);
        
        Command c1 = ci.split("");
        Command c2 = ci.split("get");
        Command c3 = ci.split("put toto.txt 11 lala");
        
        String[] expected1 = {};
        String[] expected2 = {};
        String[] expected3 = {"toto.txt", "11", "lala"};
        
        assertEquals("", c1.getAction());
        assertArrayEquals(expected1, c2.getParams());
        assertEquals("get", c2.getAction());
        assertArrayEquals(expected2, c2.getParams());
        assertEquals("put", c3.getAction());
        assertArrayEquals(expected3, c3.getParams());
    }
}
