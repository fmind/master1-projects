package models;

import java.util.HashMap;
import java.util.Set;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 * Base class for a model test which use a graph
 * @author freaxmind
 */
public class LittleGraphTest {
    protected static Node root;
    protected static Node barbara;
    protected static Node elizabeth;
    protected static Node anna;
    protected static Node carol;
    protected static Node dawn;
    protected static Node bigco;

    /**
     * It's big ... just skip it !
     * file: lite-graph.csv
     */
    @BeforeClass
    public static void setUpClass() {
        // Root
        root = new Node("Social Graph");
        Link rootLink = new Link("in", Link.Direction.IN);
        
        // Nodes
        barbara = new Node("Barbara");
        elizabeth = new Node("Elizabeth");
        anna = new Node("Anna");
        carol = new Node("Carol");
        dawn = new Node("Dawn");
        bigco = new Node("BigCo");
        
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
        Link b_e_l = new Link("friend", Link.Direction.BOTH, b_e_at);
        Link b_a_l = new Link("friend", Link.Direction.BOTH, b_a_at);
        Link b_c_l = new Link("friend", Link.Direction.BOTH, b_c_at);
        Link c_d_l = new Link("friend", Link.Direction.BOTH, c_d_at);
        Link a_b_l = new Link("employee_of", Link.Direction.OUT, a_b_at);
        Link b_b_l = new Link("employee_of", Link.Direction.OUT, b_b_at);
        Link c_b_l = new Link("employee_of", Link.Direction.OUT, c_b_at);
        
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
     * Assert that a relation list is equals to a set à node
     * use relation.getTarget() to compare the two list
     * @param expected
     * @param actual 
     */
    public void assertNodes(Set<Node> expected, Set<Relation> actual) {
        assertEquals(expected.size(), actual.size());
        
        for (Relation rel : actual) {
            assertTrue(expected.contains(rel.getTarget()));
        }
    }
}
