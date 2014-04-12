package models;

/**
 * Exception raised from an incorrect input stream
 * @author freaxmind
 */
public class ParseException extends Exception {
    
    public ParseException(String message) {
        super(message);
    }
}

