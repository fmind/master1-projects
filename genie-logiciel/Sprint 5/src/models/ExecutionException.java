package models;

/**
 * Exception raised from an error during a request execution
 * @author freaxmind
 */
public class ExecutionException extends Exception {
    
    public ExecutionException(String message) {
        super(message);
    }
}

