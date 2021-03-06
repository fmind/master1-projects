package models;

import models.Link.Direction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Relation class
 * @author freaxmind
 */
public class RelationTest {

    /**
     * Test of setExplored method, of class Relation.
     */
    @Test
    public void testSetExplored() {
        System.out.println("Relation.setExplored");
        
        Node n1 = new Node("Bob");
        Node n2 = new Node("Mary");
        Link l1 = new Link("friends", Direction.BOTH);
        Link l2 = new Link("employee_of", Direction.OUT);
        Link l2bis = new Link("employee_of", Direction.IN);
        Relation r1 = new Relation(l1, n2);
        Relation r1bis = new Relation(l1, n1);
        Relation r2 = new Relation(l2, n2);
        Relation r2bis = new Relation(l2bis, n1);
        
        n1.addRelation(r1);
        n1.addRelation(r2);
        n2.addRelation(r1bis);
        n2.addRelation(r2bis);
        
        // without change on mirror
        assertFalse(r1.isExplored());
        r1.setExplored(true, true);
        assertTrue(r1.isExplored());
        assertTrue(r1bis.isExplored());
        
        // with change on mirror
        assertFalse(r2.isExplored());
        r2.setExplored(true, false);
        assertTrue(r2.isExplored());
        assertFalse(r2bis.isExplored());
        
        // reverse operation
        r1.setExplored(false, false);
        assertFalse(r1.isExplored());
        assertTrue(r1bis.isExplored());
        r1.setExplored(false, true);
        assertFalse(r1.isExplored());
        assertFalse(r1bis.isExplored());
    }
}