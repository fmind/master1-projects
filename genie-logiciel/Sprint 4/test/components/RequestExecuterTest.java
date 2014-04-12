package components;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import models.Link;
import models.Link.Direction;
import models.Node;
import models.Relation;
import models.Request;
import models.TraversingConstraints;
import models.TraversingConstraints.Uniqueness;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Tests for Node class
 * @note we build our own graph (manualy) because it's an unit test, not an integration test
 * @note we test both size() and containsAll() of result set (in case result.length > expected.length)
 * @author freaxmind
 */
public class RequestExecuterTest {
    // root children
    private static Node root;
    private static Node barbara;
    private static Node carol;
    private static Node dawn;
    private static Node pramod;
    private static Node elizabeth;
    private static Node anna;
    private static Node jill;
    private static Node martin;
    private static Node bigco;
    private static Node nosql;
    private static Node databases;
    private static Node databaseRefactoring;
    private static Node refactoring;
    
    // print the graph only 1 time
    private static boolean printed = false;

    /**
     * Check no explored flag are left
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
        Link rootLink = new Link("in", Direction.IN);
        
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
    
    @Test
    public void test1LevelRequest() {
        System.out.println("\nElements of SocialGraph");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("in", Direction.IN));
        
        // request
        Request requestDepth = new Request(select, root);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, root);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // tests
        assertEquals(13, depthRes.size());
        assertEquals(13, breadthRes.size());
        assertTrue(depthRes.containsAll(root.getChilds()));
        assertTrue(breadthRes.containsAll(root.getChilds()));
        assertFalse(depthRes.contains(root));
        assertFalse(breadthRes.contains(root));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void test2LevelRequest() {
        System.out.println("\nFriends of BigCo employee");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(anna); expected.add(barbara); expected.add(carol);
        expected.add(dawn); expected.add(elizabeth);
        
        // test
        assertEquals(5, depthRes.size());
        assertEquals(5, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void test3LevelRequest() {
        System.out.println("\nFriends of friends of BigCo employee");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expectedBreadth = new LinkedList<>();
        expectedBreadth.add(jill); expectedBreadth.add(carol); expectedBreadth.add(barbara);
        Collection<Node> expectedDepth = new LinkedList<>();
        expectedDepth.add(jill); expectedDepth.add(carol); expectedDepth.add(elizabeth); expectedDepth.add(anna);

        // test
        assertEquals(4, depthRes.size());
        assertEquals(3, breadthRes.size());
        assertTrue(depthRes.containsAll(expectedDepth));
        assertTrue(breadthRes.containsAll(expectedBreadth));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void test4LevelRequest() {
        System.out.println("\nFriends of friends of friends of BigCo employee");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expectedBreadth = new LinkedList<>();
        expectedBreadth.add(dawn); expectedBreadth.add(elizabeth);
        Collection<Node> expectedDepth = new LinkedList<>();
        expectedDepth.add(elizabeth); expectedDepth.add(dawn); expectedDepth.add(barbara); expectedDepth.add(jill);

        // test
        assertEquals(4, depthRes.size());
        assertEquals(2, breadthRes.size());
        assertTrue(depthRes.containsAll(expectedDepth));
        assertTrue(breadthRes.containsAll(expectedBreadth));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testDepthLimitInferior() {
        System.out.println("\nDepth limit inferior to number of links (depth=2)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setDepth(2);              // set limit
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.getConstraints().setDepth(2);            // set limit
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(dawn); expected.add(barbara); expected.add(carol); expected.add(anna); expected.add(elizabeth);
        
        // test
        // must be the same result than the 3 level test
        assertEquals(5, depthRes.size());
        assertEquals(5, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    /**
     * Same resultat that 3 level request
     */
    @Test
    public void testDepthLimitSuperior() {
        System.out.println("\nDepth limit superior to number of links for friends of Jill (depth=3)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("friend", Direction.BOTH));
        
        // request
        Request requestDepth = new Request(select, jill);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setDepth(3);
        Request requestBreadth = new Request(select, jill);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestDepth.getConstraints().setDepth(3);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(carol); expected.add(anna); expected.add(elizabeth);
        expected.add(dawn); expected.add(barbara);

        // test
        assertEquals(5, depthRes.size());
        assertEquals(5, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    /**
     * Test a default parameter because it is not based on a previous request
     */
    @Test
    public void testWithoutNodeUniqueness() {
        System.out.println("\nLikes of friends of BigCo employee (without node uniqueness)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("likes", Direction.OUT));
        
        // request
        // node uniqueness default value: true
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(nosql); expected.add(refactoring);
        
        // test
        assertEquals(2, depthRes.size());
        assertEquals(2, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }

    @Test
    public void testWithNodeUniqueness() {
        System.out.println("\nLikes of friends of BigCo employee (with node uniqueness)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("likes", Direction.OUT));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setNodeUniqueness(Uniqueness.PARTIAL);        // same that Uniqueness.TOTAL
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.getConstraints().setNodeUniqueness(Uniqueness.TOTAL);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expectedBreadth = new LinkedList<>();
        expectedBreadth.add(nosql);
        Collection<Node> expectedDepth = new LinkedList<>();
        expectedDepth.add(nosql); expectedDepth.add(refactoring);
        
        // test
        assertEquals(2, depthRes.size());
        assertEquals(1, breadthRes.size());
        assertTrue(depthRes.containsAll(expectedDepth));
        assertTrue(breadthRes.containsAll(expectedBreadth));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    /**
     * Same base request that 3 level request
     */
    @Test
    public void testWithNoLinkUniqueness() {
        System.out.println("\nFriends of friends of BigCo employee (with no link uniqueness)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));

        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setLinkUniqueness(Uniqueness.NO);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.getConstraints().setLinkUniqueness(Uniqueness.NO);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(anna); expected.add(barbara); expected.add(carol);
        expected.add(dawn); expected.add(elizabeth); expected.add(jill);
        
        // test
        assertEquals(6, depthRes.size());
        assertEquals(6, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    /**
     * Same base request that 3 level request
     */
    @Test
    public void testWithTotalLinkUniqueness() {
        System.out.println("\nFriends of friends of BigCo employee (with total link uniqueness)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("friend", Direction.BOTH));

        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setLinkUniqueness(Uniqueness.TOTAL);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.getConstraints().setLinkUniqueness(Uniqueness.TOTAL);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expectedBreadth = new LinkedList<>();
        expectedBreadth.add(jill);
        Collection<Node> expectedDepth = new LinkedList<>();
        expectedDepth.add(anna); expectedDepth.add(elizabeth); expectedDepth.add(jill);
        
        // test
        assertEquals(3, depthRes.size());
        assertEquals(1, breadthRes.size());
        assertTrue(depthRes.containsAll(expectedDepth));
        assertTrue(breadthRes.containsAll(expectedBreadth));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testIncorrectName() {
        System.out.println("\nGlouby of friends of BigCo employee");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("glouby", Direction.OUT));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // test
        assertEquals(0, depthRes.size());
        assertEquals(0, breadthRes.size());
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testIncorrectDirection() {
        System.out.println("\nLikes of friends of BigCo employee (error: out instead of in)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.OUT));  // error here
        select.add(new Link("friend", Direction.BOTH));
        select.add(new Link("likes", Direction.OUT));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // test
        assertEquals(0, depthRes.size());
        assertEquals(0, breadthRes.size());
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testJockerName() {
        System.out.println("\nAll relations out of database refactoring (jocker name)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("*", Direction.OUT));
        
        // request
        Request requestDepth = new Request(select, databaseRefactoring);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, databaseRefactoring);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(databases); expected.add(pramod); expected.add(root);
        
        // test
        assertEquals(3, depthRes.size());
        assertEquals(3, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testJockerDirectionAndName() {
        System.out.println("\nAll relations of martin (jocker direction and name)");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("*", Direction.ALL));
        
        // request
        Request requestDepth = new Request(select, martin);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        Request requestBreadth = new Request(select, martin);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Collection<Node> depthRes = executer.execute(requestDepth);
        Collection<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Collection<Node> expected = new LinkedList<>();
        expected.add(refactoring); expected.add(nosql); expected.add(pramod); expected.add(root);
        
        // test
        assertEquals(4, depthRes.size());
        assertEquals(4, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
}