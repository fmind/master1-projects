package models;

import java.util.HashMap;
import models.Link.Direction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Link class
 * @author freaxmind
 */
public class LinkTest {

    /**
     * Test of getOpposite method, of class Link.
     */
    @Test
    public void testGetOpposite() {
        System.out.println("Link.getOpposite");
        
        Link both = new Link("friends", Direction.BOTH);
        Link in = new Link("employe", Direction.IN);
        Link out = new Link("mother", Direction.OUT);
        
        assertTrue(both.getOpposite().getDirection()  == Direction.BOTH);
        assertTrue(in.getOpposite().getDirection() == Direction.OUT);
        assertTrue(out.getOpposite().getDirection() == Direction.IN);
    }
    
    /**
     * Test of testEquals method, of class Link.
     */
    @Test
    public void testEquals() {
        System.out.println("Link.equals");
        
        HashMap<String, String> l1 = new HashMap<>();
        l1.put("since", "1989");
        l1.put("share", "books,movies,tweets");
        HashMap<String, String> l2 = new HashMap<>();
        l2.put("since", "2011");
        
        Link both = new Link("friends", Direction.BOTH, l1);
        Link errName = new Link("employe", Direction.BOTH, l1);
        Link errDirection = new Link("friends", Direction.OUT, l1);
        Link expectedWithAttr = new Link("friends", Direction.BOTH, l2);
        Link expectedWithoutAttr = new Link("friends", Direction.BOTH); 
        
        // errors
        assertFalse(both.equals(errName));
        assertFalse(both.equals(errDirection));
        
        // attributes
        assertTrue(both.equals(expectedWithAttr));
        assertTrue(expectedWithAttr.equals(both));
        
        // without attributes
        assertTrue(both.equals(expectedWithoutAttr));
        assertTrue(expectedWithoutAttr.equals(both));
    }
    
    /**
     * Test of testMatch method, of class Link.
     */
    @Test
    public void testMatch() {
        System.out.println("Link.match");
        
        HashMap<String, String> l1 = new HashMap<>();
        l1.put("since", "1989");
        l1.put("share", "books,movies,tweets");
        HashMap<String, String> l2 = new HashMap<>();
        l2.put("since", "2011");
        HashMap<String, String> l3 = new HashMap<>();
        l1.put("since", "1989");
        
        Link both = new Link("friends", Direction.BOTH, l1);
        Link errName = new Link("employe", Direction.BOTH, l1);
        Link errDirection = new Link("friends", Direction.OUT, l1);
        Link errAttributes = new Link("friends", Direction.BOTH, l2);
        Link expectedJocker = new Link("*", Direction.BOTH, l1);
        Link expectedAll = new Link("friends", Direction.ALL, l1);
        Link expectedWithAttr = new Link("friends", Direction.BOTH, l3);
        Link expectedWithoutAttr = new Link("friends", Direction.BOTH); 
        
        // errors
        assertFalse(both.match(errName));
        assertFalse(both.match(errDirection));
        assertFalse(both.match(errAttributes));
        
        // jocker name
        assertTrue(both.match(expectedJocker));
        assertTrue(expectedJocker.match(both));
        
        // all direction
        assertTrue(both.match(expectedAll));
        assertTrue(expectedAll.match(both));
        
        // attributes (oriented)
        assertTrue(both.match(expectedWithAttr));
        assertFalse(expectedWithAttr.match(both));
        
        // without attributes (oriented)
        assertTrue(both.match(expectedWithoutAttr));
        assertFalse(expectedWithoutAttr.match(both));
    }
}