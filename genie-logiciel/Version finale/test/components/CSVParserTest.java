package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import models.Link;
import models.ParseException;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import models.*;

/**
 * Tests for CSVParser
 * @author freaxmind
 */
public class CSVParserTest {
    private static InputStream stream;
    private static File file ;
    
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException {
        file = new File("test/data/big-graph.csv");
        stream = new FileInputStream(file);
    }
    
    @Test
    public void testGetLinkFromFile() throws FileNotFoundException, IOException, ParseException {
        System.out.println("CSVParser.getLinkFromFile");
        
        // load
        CSVParser parser = new CSVParser();
        parser.load(stream);
        
        // parse
        String[] t_read ={"Barbara","friend","Elizabeth","both","since=1989|share=[books,movies,tweets]"};
        Link linkCreated = parser.getLinkFromFile(t_read);

        // compare value
        HashMap<String, String> linkAttrs = new HashMap<>();        
        linkAttrs.put("since", "1989");
        linkAttrs.put("share", "[books,movies,tweets]");
        Link linkExpected = new Link("friend", Link.Direction.BOTH, linkAttrs);
        
        // test
        assertEquals(linkExpected, linkCreated);
        assertEquals(linkCreated.getAttributes(), linkAttrs);
    }
    
    @Test
    public void testParse() throws FileNotFoundException, IOException, ParseException {
        System.out.println("CSVParser.parse");
        
        // load
        CSVParser parser = new CSVParser();
        parser.load(stream);
        
        // parse
        Node root = new Node("SocialGraph");
        Link rootLink = new Link("in", Link.Direction.IN);
        root = parser.parse(root, rootLink);
        
        // tests
        // nodes are parsed
        assertTrue(root.contains("Barbara"));
        assertTrue(root.contains("Elizabeth"));
        assertTrue(root.contains("Anna"));
        assertTrue(root.contains("Carol"));
        assertTrue(root.contains("Dawn"));
        assertTrue(root.contains("Jill"));
        assertTrue(root.contains("Pramod"));
        assertTrue(root.contains("Martin"));
        assertTrue(root.contains("BigCo"));
        assertTrue(root.contains("Refactoring"));
        assertTrue(root.contains("NoSQL Distilled"));
        assertTrue(root.contains("Databases"));
        assertTrue(root.contains("Database Refactoring"));
        // relations are added (direct+mirror)
        // size() + 1 (file relations + root node relation)
        assertEquals(13, root.getRelations().size());
        assertEquals(7, root.getByChildName("Barbara").getRelations().size());
        assertEquals(4, root.getByChildName("Elizabeth").getRelations().size());
        assertEquals(4, root.getByChildName("Anna").getRelations().size());
        assertEquals(5, root.getByChildName("Carol").getRelations().size());
        assertEquals(4, root.getByChildName("Dawn").getRelations().size());
        assertEquals(3, root.getByChildName("Jill").getRelations().size());
        assertEquals(4, root.getByChildName("Pramod").getRelations().size());
        assertEquals(4, root.getByChildName("Martin").getRelations().size());
        assertEquals(4, root.getByChildName("BigCo").getRelations().size());
        assertEquals(4, root.getByChildName("Refactoring").getRelations().size());
        assertEquals(8, root.getByChildName("NoSQL Distilled").getRelations().size());
        assertEquals(3, root.getByChildName("Databases").getRelations().size());
        assertEquals(3, root.getByChildName("Database Refactoring").getRelations().size());
    }
}