package components;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import models.Filter;
import models.Link;
import models.Link.Direction;
import models.Node;
import models.Request;
import models.TraversingConstraints;
import models.TraversingConstraints.Uniqueness;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Node class
 * @note we test both size() and containsAll() of result set (in case result.length > expected.length)
 * @author freaxmind
 */
public class RequestExecuterTest extends BigGraphTest {
    
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // tests
        assertEquals(13, depthRes.size());
        assertEquals(13, breadthRes.size());
        assertTrue(depthRes.containsAll(root.getChildren()));
        assertTrue(breadthRes.containsAll(root.getChildren()));
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expectedBreadth = new LinkedHashSet<>();
        expectedBreadth.add(jill); expectedBreadth.add(carol); expectedBreadth.add(barbara);
        Set<Node> expectedDepth = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expectedBreadth = new LinkedHashSet<>();
        expectedBreadth.add(dawn); expectedBreadth.add(elizabeth);
        Set<Node> expectedDepth = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
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
     * and we need to compare with a none default parameter
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
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.getConstraints().setNodeUniqueness(Uniqueness.NO);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.getConstraints().setNodeUniqueness(Uniqueness.NO);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expectedBreadth = new LinkedHashSet<>();
        expectedBreadth.add(nosql);
        Set<Node> expectedDepth = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expectedBreadth = new LinkedHashSet<>();
        expectedBreadth.add(jill);
        Set<Node> expectedDepth = new LinkedHashSet<>();
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // test
        assertTrue(depthRes.isEmpty());
        assertTrue(breadthRes.isEmpty());
        
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
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // test
        assertTrue(depthRes.isEmpty());
        assertTrue(breadthRes.isEmpty());
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testFilterScope() {
        System.out.println("\nStarting from BigCo, level 1=employe barbara, level 2= likes NoSQL, level 3=author Pramod");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee_of", Direction.IN));
        select.add(new Link("likes", Direction.OUT));
        select.add(new Link("author", Direction.OUT));

        // filter
        Set<Filter> filters = new LinkedHashSet<>();
        filters.add(new Filter(Filter.Type.NAME, "arbara", "", Filter.Operator.PLUS, 1));
        filters.add(new Filter(Filter.Type.NAME, "NoSQL", "", Filter.Operator.PLUS, 2));
        filters.add(new Filter(Filter.Type.NAME, "mod", "", Filter.Operator.PLUS, -1));
        
        // request
        Request requestDepth = new Request(select, bigco);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.setFilters(filters);
        Request requestBreadth = new Request(select, bigco);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.setFilters(filters);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(pramod);
        
        // test
        assertEquals(1, depthRes.size());
        assertEquals(1, breadthRes.size());
        assertTrue(depthRes.containsAll(expected));
        assertTrue(breadthRes.containsAll(expected));
        
        // display
        System.out.println("\tResult (depht): " + depthRes);
        System.out.println("\tResult (breadth): " + breadthRes);
        
        assertTrue(isClean());
    }
    
    @Test
    public void testFilterOnSameLevel() {
        System.out.println("\nFrom NoSQL, people who are hired in 10.08 or are friend with Barbara");
        
        // select
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("likes", Direction.IN));

        // filter
        Set<Filter> filters = new LinkedHashSet<>();
        filters.add(new Filter(Filter.Type.ATTRIBUTE, "hired", "10.08"));
        filters.add(new Filter(Filter.Type.LINK, "friend", "Barbara"));
        
        // request
        Request requestDepth = new Request(select, nosql);
        requestDepth.getConstraints().setMode(TraversingConstraints.Mode.DEPTH);
        requestDepth.setFilters(filters);
        Request requestBreadth = new Request(select, nosql);
        requestBreadth.getConstraints().setMode(TraversingConstraints.Mode.BREADTH);
        requestBreadth.setFilters(filters);
        
        // execution
        RequestExecuter executer = new RequestExecuter();
        Set<Node> depthRes = executer.execute(requestDepth);
        Set<Node> breadthRes = executer.execute(requestBreadth);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(carol); expected.add(elizabeth);

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
}