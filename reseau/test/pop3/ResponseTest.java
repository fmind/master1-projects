package pop3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the class response
 * @author freaxmind
 */
public class ResponseTest {

    @Test(expected=POP3Exception.class)
    public void testEmpty() throws POP3Exception {
        System.out.println("Empty POP3 response");
        
        char[] raw = {};
        
        Response response = new Response(raw, raw.length);
    }
        
    @Test(expected=POP3Exception.class)
    public void testBadlyTerminatedUniline() throws POP3Exception {
        System.out.println("badly terminated uniline POP3 response");
        
        char[] raw = {'+', 'O', 'K', ' ', 'c', 'o', 'u'};
        
        Response response = new Response(raw, raw.length);
    }
    
    @Test(expected=POP3Exception.class)
    public void testBadlyTerminatedMultiline() throws POP3Exception {
        System.out.println("Badly terminated multiline POP3 response");
        
        char[] raw = {'+', 'O', 'K', ' ', 'm', 'o', 'i', '\r', '\n', '1', '2', '\r', '\n'};
        
        Response response = new Response(raw, raw.length);
    }

    @Test(expected=POP3Exception.class)
    public void testUnrecognizedStatus() throws POP3Exception {
        System.out.println("Unrecognized POP3 status");
        
        char[] raw = {'+', 'B', 'O', 'B', ' ', 'v', 'u', 'e', '\r', '\n'};
        
        Response response = new Response(raw, raw.length);
    }
    
    @Test
    public void testOKUniline() throws POP3Exception {
        System.out.println("OK uniline POP3 response");
        
        char[] raw = {'+', 'O', 'K', ' ', 'o', 'u', 'i', '\r', '\n'};
        String[] bodyLines = {};
        
        Response response = new Response(raw, raw.length);
        assertTrue(response.isOK());
        assertEquals("oui", response.getMessage());
        assertEquals("OK", response.getStatus());
        assertEquals("", response.getBody());
        assertArrayEquals(bodyLines, response.getBodyLines());
    }
    
    @Test
    public void testErrorUniline() throws POP3Exception {
        System.out.println("Error (status) POP3 response");
        
        char[] raw = {'-', 'E', 'R', 'R', ' ', 'n', 'o', 'n', '\r', '\n'};
        String[] bodyLines = {};
        
        Response response = new Response(raw, raw.length);
        assertFalse(response.isOK());
        assertEquals("non", response.getMessage());
        assertEquals("Erreur", response.getStatus());
        assertEquals("", response.getBody());
        assertArrayEquals(bodyLines, response.getBodyLines());
    }
    
    @Test
    public void testMultiline() throws POP3Exception {
        System.out.println("OK multiline POP3 response");
        
        char[] raw = {'+', 'O', 'K', ' ', 'i', 'n', 'f', 'o', '\r', '\n', '1', '\r', '\n', '2', '\r', '\n', '.', '\r', '\n'};
        String[] bodyLines = {"1", "2"};
        
        Response response = new Response(raw, raw.length);
        assertTrue(response.isOK());
        assertEquals("info", response.getMessage());
        assertEquals("OK", response.getStatus());
        assertEquals("1\n2", response.getBody());
        assertArrayEquals(bodyLines, response.getBodyLines());
    }
}