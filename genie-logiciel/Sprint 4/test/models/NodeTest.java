package models;

import java.util.Collection;
import java.util.HashMap;
import models.Link.Direction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Tests for Node class
 * @author freaxmind
 */
public class NodeTest {
    private static Node root;
    private static Node barbara;

    /**
     * It's big ... just skip it !
     * file: lite-graph.csv
     */
    @BeforeClass
    public static void setUpClass() {
        // Root
        root = new Node("Social Graph");
        Link rootLink = new Link("in", Direction.IN);
        
        // Nodes
        barbara = new Node("Barbara");
        Node elizabeth = new Node("Elizabeth");
        Node anna = new Node("Anna");
        Node carol = new Node("Carol");
        Node dawn = new Node("Dawn");
        Node bigco = new Node("BigCo");
        
        // Attributes
        HashMap<String, String> b_e_at = new HashMap<>();
        b_e_at.put("since", "1989");
        b_e_at.put("share", "books,movies,tweets");
        HashMap<String, String> b_a_at = new HashMap<>();
        b_a_at.put("since", "2011");
        HashMap<String, String> b_c_at = new HashMap<>();
        b_c_at.put("since", "1999");
        HashMap<String, String> c_d_at = new HashMap<>();
        c_d_at.put("since", "2005");
        HashMap<String, String> a_b_at = new HashMap<>();
        a_b_at.put("role", "Developper");
        a_b_at.put("hired", "03.06");
        HashMap<String, String> b_b_at = new HashMap<>();
        b_b_at.put("role", "Architect");
        b_b_at.put("hired", "02.05");
        HashMap<String, String> c_b_at = new HashMap<>();
        c_b_at.put("role", "Research");
        c_b_at.put("hired", "10.08");
        
        // Links
        Link b_e_l = new Link("friend", Direction.BOTH, b_e_at);
        Link b_a_l = new Link("friend", Direction.BOTH, b_a_at);
        Link b_c_l = new Link("friend", Direction.BOTH, b_c_at);
        Link c_d_l = new Link("friend", Direction.BOTH, c_d_at);
        Link a_b_l = new Link("employee_of", Direction.OUT, a_b_at);
        Link b_b_l = new Link("employee_of", Direction.OUT, b_b_at);
        Link c_b_l = new Link("employee_of", Direction.OUT, c_b_at);
        
        // Add to root link
        root.addDirectAndMirrorRelation(rootLink, barbara);
        root.addDirectAndMirrorRelation(rootLink, carol);
        root.addDirectAndMirrorRelation(rootLink, dawn);
        root.addDirectAndMirrorRelation(rootLink, elizabeth);
        root.addDirectAndMirrorRelation(rootLink, anna);
        root.addDirectAndMirrorRelation(rootLink, bigco);

        // Add to node
        barbara.addDirectAndMirrorRelation(b_e_l, elizabeth);
        barbara.addDirectAndMirrorRelation(b_a_l, anna);
        barbara.addDirectAndMirrorRelation(b_c_l, carol);
        carol.addDirectAndMirrorRelation(c_d_l, dawn);
        anna.addDirectAndMirrorRelation(a_b_l, bigco);
        barbara.addDirectAndMirrorRelation(b_b_l, bigco);
        carol.addDirectAndMirrorRelation(c_b_l, bigco);
        
        root.print();
    }
    
    /**
     * Test of getMirror method, of class Node.
     */
    @Test
    public void testGetMirror() {
        System.out.println("Node.getMirror");
 
        Collection<Relation> c1 = root.findByTarget(barbara);
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
        assertEquals(1, root.findByTarget(barbara).size());
        assertEquals(0, root.findByTarget(new Node("bobo")).size());
        
        // check mirror
        Collection<Relation> c1 = root.findByTarget(barbara);
        Collection<Relation> c2 = barbara.findByTarget(root);
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