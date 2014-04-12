/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pop3;

import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the class Message
 * @author freaxmind
 */
public class MessageTest {
    
    @Test
    public void testHeaderPatterns() {
        System.out.println("Message header patterns");
        
        assertTrue(Pattern.matches(Message.fromPattern.pattern(), "From: Bob Dylan <bob.dylan@gmail.com>"));
        assertTrue(Pattern.matches(Message.toPattern.pattern(), "To: Alice Wonder <alice.wonder@caramail.com>"));
        assertTrue(Pattern.matches(Message.datePattern.pattern(), "Date: 12/12/12"));
        assertTrue(Pattern.matches(Message.subjectPattern.pattern(), "Subject: Hi Alice"));
    }
    
    @Test
    public void testSplit() {
        System.out.println("Message split header/body");
        
        String text = "";
        text += "From: Alice <alice@gmail.com>\n";
        text += "To: Bob <bob@hotmail.com>\n";
        text += "Date: Wed, 10 Feb 2010 02:38:34 -0800\n";
        text += "Subject: hi!\n";
        text += "\n";
        text += "Hi, how are you?\n";
        text += "- Alice";
        
        Message message = new Message(1, 200, text);
        
        assertEquals(1, message.getId());
        assertEquals(200, message.getSize());
        assertEquals("Alice <alice@gmail.com>", message.getFrom());
        assertEquals("Bob <bob@hotmail.com>", message.getTo());
        assertEquals("Wed, 10 Feb 2010 02:38:34 -0800", message.getDate());
        assertEquals("hi!", message.getSubject());
        assertEquals("Hi, how are you?\n- Alice", message.getBody());
    }
}