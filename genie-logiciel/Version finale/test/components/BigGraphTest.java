package components;

import java.util.HashMap;
import models.Link;
import models.Node;
import models.Relation;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/**
 * Base class for a component test which use a graph
 * @note we build our own graph (manualy) because it's an unit test, not an integration test
 * @author freaxmind
 */
public class BigGraphTest {
    // root children
    protected static Node root;
    protected static Node barbara;
    protected static Node carol;
    protected static Node dawn;
    protected static Node pramod;
    protected static Node elizabeth;
    protected static Node anna;
    protected static Node jill;
    protected static Node martin;
    protected static Node bigco;
    protected static Node nosql;
    protected static Node databases;
    protected static Node databaseRefactoring;
    protected static Node refactoring;
    
    // print the graph only 1 time
    private static boolean printed = false;

    /**
     * Checks no explored flag are left
     * @return 
     */
    public static boolean isClean() {
        // check root
        if (root.isExplored()) {
            return false;
        }
        
        for (Relation elementRelation : root.getRelations()) {
            // check root children and root links
            if (elementRelation.isExplored() || elementRelation.getTarget().isExplored()) {
                return false;
            }
            
            // check child relations
            for (Relation relation : elementRelation.getTarget().getRelations()) {
                if (relation.isExplored()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * It's big ... just skip it !
     * file: big-graph.csv
     * set up each execution to avoid conflit with explored flag
     */
    @Before
    public void setUp() {
        root = new Node("Social Graph");
        Link rootLink = new Link("in", Link.Direction.IN);
        
        // Nodes
        barbara = new Node("Barbara");
        carol = new Node("Carol");
        dawn = new Node("Dawn");
        pramod = new Node("Pramod");
        elizabeth = new Node("Elizabeth");
        anna = new Node("Anna");
        jill = new Node("Jill");
        martin = new Node("Martin");
        bigco = new Node("BigCo");
        nosql = new Node("NoSQL Distilled");
        databases = new Node("Database");
        databaseRefactoring = new Node("Database Refactoring");
        refactoring = new Node("Refactoring");
        
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
        Link b_e_l = new Link("friend", Link.Direction.BOTH, b_e_at);
        Link b_a_l = new Link("friend", Link.Direction.BOTH, b_a_at);
        Link b_c_l = new Link("friend", Link.Direction.BOTH, b_c_at);
        Link c_d_l = new Link("friend", Link.Direction.BOTH, c_d_at);
        Link d_j_l = new Link("friend", Link.Direction.BOTH, d_j_at);
        Link p_m_l = new Link("friend", Link.Direction.BOTH, p_m_at);
        Link e_j_l = new Link("friend", Link.Direction.BOTH, e_j_at);
        Link a_b_l = new Link("employee_of", Link.Direction.OUT, a_b_at);
        Link b_b_l = new Link("employee_of", Link.Direction.OUT, b_b_at);
        Link c_b_l = new Link("employee_of", Link.Direction.OUT, c_b_at);
        Link likes = new Link("likes", Link.Direction.OUT);
        Link category = new Link("category", Link.Direction.OUT);
        Link author = new Link("author", Link.Direction.OUT);
        
        // Add to root link
        root.addDirectAndMirrorRelation(rootLink, barbara);
        root.addDirectAndMirrorRelation(rootLink, carol);
        root.addDirectAndMirrorRelation(rootLink, dawn);
        root.addDirectAndMirrorRelation(rootLink, pramod);
        root.addDirectAndMirrorRelation(rootLink, elizabeth);
        root.addDirectAndMirrorRelation(rootLink, anna);
        root.addDirectAndMirrorRelation(rootLink, jill);
        root.addDirectAndMirrorRelation(rootLink, martin);
        root.addDirectAndMirrorRelation(rootLink, bigco);
        root.addDirectAndMirrorRelation(rootLink, refactoring);
        root.addDirectAndMirrorRelation(rootLink, nosql);
        root.addDirectAndMirrorRelation(rootLink, databases);
        root.addDirectAndMirrorRelation(rootLink, databaseRefactoring);

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
        nosql.addDirectAndMirrorRelation(category, databases);
        databaseRefactoring.addDirectAndMirrorRelation(category, databases);
        databaseRefactoring.addDirectAndMirrorRelation(author, pramod);
        nosql.addDirectAndMirrorRelation(author, pramod);
        nosql.addDirectAndMirrorRelation(author, martin);
        refactoring.addDirectAndMirrorRelation(author, martin);
        
        // print the graph on first time
        if (!printed) {
            root.print();
            printed = true;
        }
        
        assertTrue(isClean());
    }
}
