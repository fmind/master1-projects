package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import models.Node;
import models.ParseException;
import models.Relation;

/**
 * Interface for graph input parser
 * @author freaxmind
 */
public interface Parser {
    
    /**
     * Load an input stream
     * @todo handle parse exception
     * @throws FileNotFoundException 
     */
    public void load(InputStream input) throws IOException;
    
// ATTENTION : lire source et target lors 1e lecture
    /**
     * Parse an input stream and return a social graph
     * @param rootRelation
     * @return a social graph
     * @throws ParseException 
     */
    public Node parse(Relation rootRelation) throws ParseException;
}
