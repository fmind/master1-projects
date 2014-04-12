package controllers;

import java.util.Set;
import models.Node;
import models.Request;

/**
 * Execute a request and return a set of node
 * @author freaxmind
 */
public interface Executer {
    
    /**
     * Return a set of node from a request
     * @param request
     * @return distinct node list
     */
    public Set<Node> execute(Request request);
}
