package models;

import java.util.HashMap;
import java.util.Set;
import models.Link.Direction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Node class
 * @author freaxmind
 */
public class NodeTest extends LittleGraphTest {
    
    /**
     * Test of getMirror method, of class Node.
     */
    @Test
    public void testGetMirror() {
        System.out.println("Node.getMirror");
 
        Set<Relation> c1 = root.findByTargetName(barbara.getName());
        Relation root_to_barbara = (Relation) c1.toArray()[0];
        Relation barbara_to_root = barbara.getMirror(root_to_barbara);
 
        // check reverse target/source and link direction
        assertEquals(root_to_barbara.getTarget(), barbara_to_root.getSource());
        assertEquals(root_to_barbara.getLink(), barbara_to_root.getLink().getOpposite());
    }
    
    /**
     * Test of findByTarget method, of class Node.
     */
    @Test
    public void testFindByTarget() {
        System.out.println("Node.findByTarget");
 
        // find by name
        assertEquals(1, root.findByTargetName(barbara.getName()).size());
        assertEquals(0, root.findByTargetName("bobo").size());
        
        // check mirror
        Set<Relation> c1 = root.findByTargetName(barbara.getName());
        Set<Relation> c2 = barbara.findByTargetName(root.getName());
        Relation r1 = (Relation) c1.toArray()[0];
        Relation r2 = (Relation) c2.toArray()[0];
        assertEquals(r1.getLink(), r2.getLink().getOpposite());
    }
    
    /**
     * Test of findByLink method, of class Node.
     */
    @Test
    public void testFindByLink() {
        System.out.println("Node.findByLink");

        // name jocker
        assertEquals(5, barbara.findByLink("*", Direction.ALL).size());
        assertEquals(3, barbara.findByLink("*", Direction.BOTH).size());
        assertEquals(2, barbara.findByLink("*", Direction.OUT).size());
        assertEquals(0, barbara.findByLink("*", Direction.IN).size());
        
        // direction jocker
        assertEquals(3, barbara.findByLink("friend", Direction.BOTH).size());
        assertEquals(3, barbara.findByLink("friend", Direction.ALL).size());
        assertEquals(0, barbara.findByLink("friend", Direction.OUT).size());
        assertEquals(0, barbara.findByLink("friend", Direction.IN).size());
    }
    
    /**
     * Test of findByLink method, of class Node.
     */
    @Test
    public void testFindByAttributes() {
        System.out.println("Node.findByLink (with attributes)");
        
        HashMap<String, String> l1 = new HashMap<>();
        l1.put("since", "1989");
        l1.put("share", "books,movies,tweets");
        HashMap<String, String> l2 = new HashMap<>();
        l2.put("since", "1989");
        HashMap<String, String> no = new HashMap<>();
        no.put("mage", "Gandalf");
 
        assertEquals(1, barbara.findByLink("friend", Direction.BOTH, l1).size());
        assertEquals(1, barbara.findByLink("friend", Direction.BOTH, l2).size());
        assertEquals(0, barbara.findByLink("friend", Direction.BOTH, no).size());
    }
}