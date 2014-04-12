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
     * Test of equals method, of class Link.
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
        Link bothEq = new Link("friends", Direction.BOTH, l2);
        
        assertFalse(both.equals(errName));
        assertFalse(both.equals(errDirection));
        assertTrue(both.equals(bothEq));
    }
}