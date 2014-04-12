package models;

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
}