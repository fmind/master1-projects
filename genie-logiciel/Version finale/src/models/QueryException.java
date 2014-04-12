package models;

/**
 * Exception raised from an incorrect query
 * @author freaxmind
 */
public class QueryException extends Exception {
    
    public QueryException(String message) {
        super(message);
    }
}
