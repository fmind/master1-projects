package models;

import java.util.HashMap;
import models.Link.Direction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Node class
 * @author freaxmind
 */
public class NodeTest {

    /**
     * Test of addRelation method, of class Node.
     * id generation
     */
    @Test
    public void testAddRelation() {
        System.out.println("Node.addRelation");
        
        Node bob = new Node("Bob");
        Node mary = new Node("Mary");
        Node paul = new Node("Paul");
        Node michel = new Node("Michel");
        Link friends = new Link("friends", Direction.BOTH);

        bob.addRelation(friends, mary);
        bob.addRelation(friends, paul);
 
        // exists
        assertEquals(mary, bob.findByTarget(mary).getTarget());
        assertEquals(paul, bob.findByTargetName("Paul").getTarget());
        assertEquals(bob, bob.findByTarget(mary).getSource());
        assertEquals(friends, bob.findByTargetName("Paul").getLink());
        
        // doesn't exist
        assertNull(bob.findByTargetName("Robert"));
        assertNull(bob.findByTarget(michel));
    }
    
    /**
     * Test of addMirrorRelation method, of class Node.
     * id generation
     */
    @Test
    public void testAddMirrorRelation() {
        System.out.println("Node.addMirrorRelation");
        
        Node bob = new Node("Bob");
        Node mary = new Node("Mary");
        Node bigco = new Node("Bigco");
        Link friends = new Link("friends", Direction.BOTH);
        Link work = new Link("work", Direction.IN);

        bob.addRelation(friends, mary);
        bob.addRelation(work, bigco);
        bob.addMirrorRelation(friends, mary);
        bob.addMirrorRelation(work, bigco);
 
        assertEquals(bob, mary.findByTarget(bob).getTarget());
        assertEquals(bob, bigco.findByTargetName("Bob").getTarget());
        assertEquals(mary, mary.findByTarget(bob).getSource());
        assertEquals(Direction.BOTH, mary.findByTargetName("Bob").getLink().getDirection());
        assertEquals(Direction.OUT, bigco.findByTarget(bob).getLink().getDirection());
    }
    
    /**
     * Test of findByLink method, of class Node.
     * id generation
     */
    @Test
    public void testFindByLink() {
        System.out.println("Node.findByLink");
        
        Node bob = new Node("Bob");
        Node mary = new Node("Mary");
        Node paul = new Node("Paul");
        Node bigco = new Node("Bigco");
        Link friendOf = new Link("friends", Direction.IN);
        Link friends = new Link("friends", Direction.BOTH);
        Link work = new Link("work", Direction.IN);

        bob.addRelation(friendOf, mary);
        bob.addRelation(friends, paul);
        bob.addRelation(work, bigco);
 
        assertEquals(2, bob.findByLink("friends").size());
        assertEquals(1, bob.findByLink("friends", Direction.IN).size());
        assertEquals(1, bob.findByLink("work").size());
        assertEquals(0, bob.findByLink("no").size());
    }
    
    /**
     * Test of findByLink method, of class Node.
     * id generation
     */
    @Test
    public void testFindByAttributes() {
        System.out.println("Node.findByLink (with attributes)");
        
        HashMap<String, String> l1 = new HashMap<>();
        l1.put("from", "1999");
        l1.put("to", "2010");
        HashMap<String, String> l2 = new HashMap<>();
        l2.put("from", "1999");
        HashMap<String, String> no = new HashMap<>();
        no.put("mage", "Gandalf");
        
        Node bob = new Node("Bob");
        Node mary = new Node("Mary");
        Node paul = new Node("Paul");
        Node remi = new Node("Remi");
        Node nat = new Node("Nat");
        Link friendsL1 = new Link("friends", Direction.BOTH, l1);
        Link friendsL2 = new Link("friends", Direction.BOTH, l2);
        Link friends = new Link("friends", Direction.BOTH);

        bob.addRelation(friendsL1, mary);
        bob.addRelation(friendsL1, paul);
        bob.addRelation(friendsL2, remi);
        bob.addRelation(friends, nat);
 
        assertEquals(2, bob.findByLink("friends", Direction.BOTH, l1).size());
        assertEquals(3, bob.findByLink("friends", Direction.BOTH, l2).size());
        assertEquals(0, bob.findByLink("friends", Direction.BOTH, no).size());
    }
}