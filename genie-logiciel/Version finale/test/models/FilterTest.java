package models;

import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Filter class
 * @note assertNodes is a method from LittleGraphTest
 * @author freaxmind
 */
public class FilterTest extends LittleGraphTest {
    
    @Test
    public void testFilterName() {
        System.out.println("\nAll node name that contains 'ar'");
        
        // filters
        Filter filter = new Filter(Filter.Type.NAME, "ar", "");
        Filter falseFilter = new Filter(Filter.Type.NAME, "AAA", "");
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(barbara); expected.add(carol);
        
        // result
        Set<Relation> actual = filter.apply(root.getRelations());

        // test
        assertNodes(expected, actual);
        assertTrue(falseFilter.apply(root.getRelations()).isEmpty());
        
        // display
        System.out.println("\tResult : " + actual);
    }
    
    @Test
    public void testFilterMinus() {
        System.out.println("\nAll node name that not contains 'ar'");
        
        // filters
        Filter filter = new Filter(Filter.Type.NAME, "ar", "", Filter.Operator.MINUS);
        Filter falseFilter = new Filter(Filter.Type.NAME, "AAA", "", Filter.Operator.MINUS);
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(anna); expected.add(dawn); expected.add(elizabeth); expected.add(bigco);
        
        // result
        Set<Relation> actual = filter.apply(root.getRelations());

        // test
        assertNodes(expected, actual);
        assertEquals(6, falseFilter.apply(root.getRelations()).size());     // contains all nodes
        
        // display
        System.out.println("\tResult : " + actual);
    }
    
    @Test
    public void testFilterAttributeExists() {
        System.out.println("\nBarbara's relations with an attribute 'hired'");
        
        // filters
        Filter filter = new Filter(Filter.Type.ATTRIBUTE, "hired", "");
        Filter falseFilter = new Filter(Filter.Type.ATTRIBUTE, "fr", "");
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(bigco);
        
        // result
        Set<Relation> actual = filter.apply(barbara.getRelations());

        // test
        assertNodes(expected, actual);
        assertTrue(falseFilter.apply(barbara.getRelations()).isEmpty());
        
        // display
        System.out.println("\tResult : " + actual);
    }
    
    @Test
    public void testFilterAttributeValue() {
       System.out.println("\nBigCo employee hired on Oct 08");
        
        // filters
        Filter filter = new Filter(Filter.Type.ATTRIBUTE, "hired", "10.08");
        Filter falseFilter = new Filter(Filter.Type.ATTRIBUTE, "hired", "O8");
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(carol);
        
        // result
        Set<Relation> actual = filter.apply(bigco.getRelations());

        // test
        assertNodes(expected, actual);
        assertTrue(falseFilter.apply(bigco.getRelations()).isEmpty());
        
        // display
        System.out.println("\tResult : " + actual);
    }
    
    @Test
    public void testFilterLinkExists() {
        System.out.println("\nAll nodes with a employee_of relation");
        
        // filters
        Filter filter = new Filter(Filter.Type.LINK, "employee_of>", "");
        Filter filterReverse = new Filter(Filter.Type.LINK, "employee_of<", "");
        Filter falseFilter = new Filter(Filter.Type.LINK, "emp", "");
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(anna); expected.add(barbara); expected.add(carol);
        Set<Node> expectedReverse = new LinkedHashSet<>();
        expectedReverse.add(bigco);
        
        // result
        Set<Relation> actual = filter.apply(root.getRelations());
        Set<Relation> actualReverse = filterReverse.apply(root.getRelations());

        // test
        assertNodes(expected, actual);
        assertNodes(expectedReverse, actualReverse);
        assertTrue(falseFilter.apply(root.getRelations()).isEmpty());
        
        // display
        System.out.println("\tResult : " + actual);
        System.out.println("\tResult (link reversed): " + actualReverse);
    }
    
    @Test
    public void testFilterLinkValue() {
        System.out.println("\nAll node which has Barbara as friend");
        
        // filters
        Filter filter = new Filter(Filter.Type.LINK, "friend", "Barbara");
        Filter falseFilter = new Filter(Filter.Type.LINK, "friend", "momo");
        
        // compare value
        Set<Node> expected = new LinkedHashSet<>();
        expected.add(anna); expected.add(elizabeth); expected.add(carol);
        
        // result
        Set<Relation> actual = filter.apply(root.getRelations());

        // test
        assertNodes(expected, actual);
        assertTrue(falseFilter.apply(root.getRelations()).isEmpty());
        
        // display
        System.out.println("\tResult : " + actual);
    }
    

}