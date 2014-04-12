package components;

import java.util.HashMap;
import models.Link;
import models.Link.Direction;
import models.Node;
import org.junit.Test;
import org.junit.BeforeClass;

/**
 * Tests for Node class
 * @author freaxmind
 */
public class RequestExecuterTest {
    private static Node root;
    
    /**
     * It's big ... just skip it !
     * file: big-graph.csv
     */
    @BeforeClass
    public static void setUpClass() {
        root = new Node("Social Graph");
        Link rootLink = new Link("in", Direction.IN);
        
        // Nodes
        Node barbara = new Node("Barbara");
        Node carol = new Node("Carol");
        Node dawn = new Node("Dawn");
        Node pramod = new Node("Pramod");
        Node elizabeth = new Node("Elizabeth");
        Node anna = new Node("Anna");
        Node jill = new Node("Jill");
        Node martin = new Node("Martin");
        Node bigco = new Node("BigCo");
        Node nosql = new Node("NoSQL Distilled");
        Node database = new Node("Database Refactoring");
        Node refactoring = new Node("Refactoring");
        
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
        HashMap<String, String> d_j_at = new HashMap<>();
        d_j_at.put("since", "2002");
        HashMap<String, String> p_m_at = new HashMap<>();
        p_m_at.put("since", "2003");
        HashMap<String, String> e_j_at = new HashMap<>();
        e_j_at.put("since", "1998");
        HashMap<String, String> a_b_at = new HashMap<>();
        b_e_at.put("role", "Developper");
        b_e_at.put("hired", "03.06");
        HashMap<String, String> b_b_at = new HashMap<>();
        b_e_at.put("role", "Architect");
        b_e_at.put("hired", "02.05");
        HashMap<String, String> c_b_at = new HashMap<>();
        b_e_at.put("role", "Research");
        b_e_at.put("hired", "10.08");
        
        // Links
        Link b_e_l = new Link("friend", Direction.BOTH, b_e_at);
        Link b_a_l = new Link("friend", Direction.BOTH, b_a_at);
        Link b_c_l = new Link("friend", Direction.BOTH, b_c_at);
        Link c_d_l = new Link("friend", Direction.BOTH, c_d_at);
        Link d_j_l = new Link("friend", Direction.BOTH, d_j_at);
        Link p_m_l = new Link("friend", Direction.BOTH, p_m_at);
        Link e_j_l = new Link("friend", Direction.BOTH, e_j_at);
        Link a_b_l = new Link("employee_of", Direction.OUT, a_b_at);
        Link b_b_l = new Link("employee_of", Direction.OUT, b_b_at);
        Link c_b_l = new Link("employee_of", Direction.OUT, c_b_at);
        Link likes = new Link("likes", Direction.OUT);
        Link category = new Link("likes", Direction.OUT);
        Link author = new Link("likes", Direction.OUT);
        
        // Add to root link
        root.addDirectAndMirrorRelation(rootLink, barbara);
        root.addDirectAndMirrorRelation(rootLink, carol);
        root.addDirectAndMirrorRelation(rootLink, dawn);
        root.addDirectAndMirrorRelation(rootLink, pramod);
        root.addDirectAndMirrorRelation(rootLink, elizabeth);
        root.addDirectAndMirrorRelation(rootLink, anna);
        root.addDirectAndMirrorRelation(rootLink, anna);
        root.addDirectAndMirrorRelation(rootLink, jill);
        root.addDirectAndMirrorRelation(rootLink, martin);
        root.addDirectAndMirrorRelation(rootLink, bigco);
        root.addDirectAndMirrorRelation(rootLink, refactoring);
        root.addDirectAndMirrorRelation(rootLink, nosql);
        root.addDirectAndMirrorRelation(rootLink, database);

        // Add to node
        barbara.addDirectAndMirrorRelation(b_e_l, elizabeth);
        barbara.addDirectAndMirrorRelation(b_a_l, anna);
        barbara.addDirectAndMirrorRelation(b_c_l, carol);
        carol.addDirectAndMirrorRelation(c_d_l, dawn);
        dawn.addDirectAndMirrorRelation(d_j_l, jill);
        pramod.addDirectAndMirrorRelation(p_m_l, martin);
        elizabeth.addDirectAndMirrorRelation(e_j_l, jill);
        anna.addDirectAndMirrorRelation(a_b_l, bigco);
        barbara.addDirectAndMirrorRelation(b_b_l, bigco);
        carol.addDirectAndMirrorRelation(c_b_l, bigco);
        anna.addDirectAndMirrorRelation(likes, refactoring);
        barbara.addDirectAndMirrorRelation(likes, refactoring);
        barbara.addDirectAndMirrorRelation(likes, nosql);
        carol.addDirectAndMirrorRelation(likes, nosql);
        dawn.addDirectAndMirrorRelation(likes, nosql);
        elizabeth.addDirectAndMirrorRelation(likes, nosql);
        nosql.addDirectAndMirrorRelation(category, database);
        database.addDirectAndMirrorRelation(category, database);
        database.addDirectAndMirrorRelation(author, pramod);
        nosql.addDirectAndMirrorRelation(author, pramod);
        nosql.addDirectAndMirrorRelation(author, martin);
        refactoring.addDirectAndMirrorRelation(author, martin);
        
        root.print();
    }

    /**
     * Test of execute method, of class RequestExecuter.
     */
    @Test
    public void testExecute() {
        System.out.println("RequestExecuter.execute");

        
    }
}