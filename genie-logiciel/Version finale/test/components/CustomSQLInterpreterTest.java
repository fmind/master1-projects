package components;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import models.Link;
import org.junit.Test;
import org.junit.BeforeClass;
import models.*;
import models.Link.Direction;
import static org.junit.Assert.*;

/**
 * Test for interpreter
 * @note 2 parts: good and bad request
 * @author freaxmind
 */
public class CustomSQLInterpreterTest {
    private static CustomSQLInterpreter interpreter;
    
    @BeforeClass
    public static void setUpClass(){
        interpreter = new CustomSQLInterpreter();
    }
   
    /**************************************************************************/
    /*************************** Good request *********************************/
    /**************************************************************************/
    
    @Test
    public void testSimpleRequest() throws QueryException {
        String query = "SELECT friends FROM Barbara";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("friends", Direction.BOTH));
        Node from = new Node("Barbara");
        Request expected = new Request(select, from);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSelectMultipleLinks() throws QueryException {
        String query = "SELECT friends, author<, category> FROM Martin";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("friends", Direction.BOTH));
        select.add(new Link("author", Direction.IN));
        select.add(new Link("category", Direction.OUT));
        Node from = new Node("Martin");
        Request expected = new Request(select, from);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSelectJocker() throws QueryException {
        String query = "SELECT *>, friends*, ** FROM NoSQL Distilitted";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("*", Direction.OUT));
        select.add(new Link("friends", Direction.ALL));
        select.add(new Link("*", Direction.ALL));
        Node from = new Node("NoSQL Distilitted");
        Request expected = new Request(select, from);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAllFilterType() throws QueryException {
        String query = "SELECT friends FROM NoSQL Distilitted WHERE ~ar AND @since AND @since=1996 AND #likes AND #likes=NoSQL Distillited";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("friends", Direction.BOTH));
        Node from = new Node("NoSQL Distilitted");
        Set<Filter> where = new LinkedHashSet<>();
        where.add(new Filter(Filter.Type.NAME, "ar", ""));
        where.add(new Filter(Filter.Type.ATTRIBUTE, "since", ""));
        where.add(new Filter(Filter.Type.ATTRIBUTE, "since", "1996"));
        where.add(new Filter(Filter.Type.LINK, "likes", ""));
        where.add(new Filter(Filter.Type.LINK, "likes", "NoSQL Distillited"));
        Request expected = new Request(select, from);
        expected.setFilters(where);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilterNot() throws QueryException {
        String query = "SELECT friends FROM NoSQL Distilitted WHERE NOT ~bab AND NOT @hired=10.08 AND #employee";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("friends", Direction.BOTH));
        Node from = new Node("NoSQL Distilitted");
        Set<Filter> where = new LinkedHashSet<>();
        where.add(new Filter(Filter.Type.NAME, "bab", "", Filter.Operator.MINUS));
        where.add(new Filter(Filter.Type.ATTRIBUTE, "hired", "10.08", Filter.Operator.MINUS));
        where.add(new Filter(Filter.Type.LINK, "employee", ""));
        Request expected = new Request(select, from);
        expected.setFilters(where);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilterScope() throws QueryException {
        String query = "SELECT employee> FROM BigCo WHERE NOT ~bab ON 1 AND @hired=10 08 ON 2";
        
        // display
        System.out.println(query);
        
        // compare value
        Collection<Link> select = new LinkedList<>();
        select.add(new Link("employee", Direction.OUT));
        Node from = new Node("BigCo");
        Set<Filter> where = new LinkedHashSet<>();
        where.add(new Filter(Filter.Type.NAME, "bab", "", Filter.Operator.MINUS, 1));
        where.add(new Filter(Filter.Type.ATTRIBUTE, "hired", "10 08", Filter.Operator.PLUS, 2));
        Request expected = new Request(select, from);
        expected.setFilters(where);
                
        // interpretation
        Request actual = interpreter.interpret(query);
        
        // test
        assertEquals(expected, actual);
    }
    
    /**************************************************************************/
    /*************************** Bad request **********************************/
    /**************************************************************************/
    
    @Test(expected=QueryException.class)
    public void testMissingSelectKeyword() throws QueryException {
        String query = "friends FROM Barbara";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMissingFromKeyword() throws QueryException {
        String query = "SELECT friends FR Barbara";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testBadKeywordOrder() throws QueryException {
        String query = "WHERE friends SELECT Barbara";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testEmptySelect() throws QueryException {
        String query = "SELECT   FROM Martin";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testEmptyFrom() throws QueryException {
        String query = "SELECT friends FROM   ";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testEmptyWhere() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE  ";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }

    @Test(expected=QueryException.class)
    public void testMultipleSelectKeyword() throws QueryException {
        String query = "SELECT friends FROM Martin SELECT";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMultipleFromKeyword() throws QueryException {
        String query = "SELECT friends FROM MarFROMtin";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMultipleWhereKeyword() throws QueryException {
        String query = "SELECT friends FROM Martin WHEREWHERE @since";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testFilterEmpty() throws QueryException {
        String query = "SELECT author FROM NoSQL WHERE @since AND AND ~bo";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMissingFilterType() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE since";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }

    @Test(expected=QueryException.class)
    public void testEqualMisplaced() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @=since";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMultipleEquals() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @since=1008=11 09";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testMissingKey() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @=10 08";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testScopeInferior0() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @since=10 08 ON -3";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testScopeNotANumber() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @since=10 08 ON abc";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testScopeEmpty() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @since=10 08 ON";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
    
    @Test(expected=QueryException.class)
    public void testScopeMultipleValue() throws QueryException {
        String query = "SELECT friends FROM Martin WHERE @since=10 08 ON 1 2";
        
        // display
        System.out.println(query);

        // interpretation
        Request actual = interpreter.interpret(query);
    }
}
