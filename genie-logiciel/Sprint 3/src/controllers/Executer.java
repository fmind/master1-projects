package controllers;

import models.Node;
import models.Request;

/**
 * Execute a request and return a partial graph
 * @author freaxmind
 */
public interface Executer {
    
    /**
     * Return a partial graph
     * @param root
     * @param request
     * @return root node of the partial graph
     */
    public Node execute(Node root, Request request);
}
