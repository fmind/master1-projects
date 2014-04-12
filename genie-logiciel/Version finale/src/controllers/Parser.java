package controllers;

import java.io.IOException;
import java.io.InputStream;
import models.Link;
import models.Node;
import models.ParseException;

/**
 * Interface for graph parser
 * @author freaxmind
 */
public interface Parser {
    
    /**
     * Load an input stream
     * @throws IOException 
     */
    public void load(InputStream input) throws IOException;
    
    /**
     * Parse an input stream and return a social graph
     * @param root
     * @param rootLink
     * @return a social graph
     * @throws ParseException 
     */
    public Node parse(Node root, Link rootLink) throws ParseException;
}
