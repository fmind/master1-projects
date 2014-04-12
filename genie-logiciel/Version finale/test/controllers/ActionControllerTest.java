package controllers;

import components.CSVParser;
import components.CustomSQLInterpreter;
import components.RequestExecuter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import models.ExecutionException;
import models.Node;
import models.QueryException;
import models.TraversingConstraints;
import models.TraversingConstraints.Mode;
import models.TraversingConstraints.Uniqueness;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
  * Integration test for ActionController class
  * many concrete request that mix all constraints/filters
  * @author freaxmind
  */
public class ActionControllerTest {
    static private ActionController ctrl;
    
    /**
     * Build a set of node from a list of name
     * @param names
     * @return
     */
    Set<Node> toSetNode(String... names) {
        Set<Node> nodes = new LinkedHashSet<>();
        
        for (String name : names) {
            nodes.add(new Node(name));
        }
        
        return nodes;
    }
    
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, IOException {
        ctrl = new ActionController();

        // data source
        File file = new File("test/data/big-graph.csv");
        InputStream input = new FileInputStream(file);

        // components
        Parser parser = new CSVParser();
        parser.load(input);
        Interpreter interpreter = new CustomSQLInterpreter();
        Executer executer = new RequestExecuter();

        // compose the controller
        ctrl.setParser(parser);
        ctrl.setInterpreter(interpreter);
        ctrl.setExecuter(executer);
    }
    
    @Test
    public void testAllNodes() throws QueryException, ExecutionException {
        System.out.println("Get all nodes");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT in< FROM SocialGraph");
        
        // test
        assertEquals(13, actual.size());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testAuthors() throws QueryException, ExecutionException {
        System.out.println("\nGet all authors");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT in< FROM SocialGraph WHERE #author>");
        Set<Node> expected = this.toSetNode("Martin", "Pramod");

        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testEmployeesOfBigcoWhoLikeNoSQL() throws QueryException, ExecutionException {
        System.out.println("\nAll employees of BigCo who like NoSQl Distilled");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT likes< FROM NoSQL Distilled WHERE #employee_of>=BigCo");
        Set<Node> expected = this.toSetNode("Carol", "Barbara");
        
        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }

    @Test
    public void testEmployeesOfBigcoWhoDontLikeNoSQL() throws QueryException, ExecutionException {
        System.out.println("\nAll BigCo employees who don't like NoSQL Distilled");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT employee_of< FROM BigCo WHERE NOT #likes>=NoSQL Distilled");
        Set<Node> expected = this.toSetNode("Anna");
        
        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testFriendsofFriendsOfCarol() throws QueryException, ExecutionException {
        System.out.println("\nFriends of friends of Carol (no backward friendship)");
        
        // compare value
        TraversingConstraints constraints = new TraversingConstraints(Mode.BREADTH, 2, Uniqueness.NO, Uniqueness.TOTAL);
        Set<Node> actual = ctrl.handleQueryWithConstraints("SELECT friend FROM Carol", constraints);
        Set<Node> expected = this.toSetNode("Elizabeth", "Anna", "Jill");

        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testAllThingsConnectedFromDawn() throws QueryException, ExecutionException {
        System.out.println("\nAll things connected from Dawn");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT ** FROM Carol");
        Set<Node> expected = this.toSetNode("NoSQL Distilled", "BigCo", "Dawn", "Barbara", "SocialGraph");

        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testEmployeWithAnFriendWithBaFriendWithCa() throws QueryException, ExecutionException {
        System.out.println("\nEmployee of BigCo with An, friend with Ba, friend with Ca");
        
        // compare value
        TraversingConstraints constraints = new TraversingConstraints(Mode.BREADTH, 3, Uniqueness.NO, Uniqueness.NO);
        Set<Node> actual = ctrl.handleQueryWithConstraints("SELECT employee_of<, friend FROM BigCo WHERE ~An ON 1 AND ~Ba ON 2 AND ~Ca", constraints);
        Set<Node> expected = this.toSetNode("Carol");

        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testPeopleNotHiredOn1008() throws QueryException, ExecutionException {
        System.out.println("\nPeople not hired on 10.08");
        
        // compare value
        Set<Node> actual = ctrl.handleQuery("SELECT employee_of< FROM BigCo WHERE NOT @hired=10.08 ON 1");
        Set<Node> expected = this.toSetNode("Barbara", "Anna");

        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
    
    @Test
    public void testFriends() throws QueryException, ExecutionException {
        System.out.println("\nGet all friends");
        
        // compare value
        TraversingConstraints constraints = new TraversingConstraints(Mode.BREADTH, 5, Uniqueness.NO, Uniqueness.NO);
        Set<Node> actual = ctrl.handleQueryWithConstraints("SELECT friend FROM Barbara", constraints);
        Set<Node> expected = this.toSetNode("Elizabeth", "Anna", "Carol", "Dawn", "Jill", "Barbara");
        System.out.println(actual);
        // test
        assertArrayEquals(expected.toArray(), actual.toArray());
        
        // display
        System.out.println("\tResult: " + actual);
    }
}
