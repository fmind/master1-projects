package controllers;

import models.QueryException;
import models.Request;

/**
 * Build a request from a query
 * @author freaxmind
 */
public interface Interpreter {
    
    public Request interpret(String query) throws QueryException;
}
