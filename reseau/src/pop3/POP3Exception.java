package pop3;

/**
 * Exception fetching messages or getting responses
 * @author freaxmind
 */
public class POP3Exception extends Exception {

    public POP3Exception(String message) {
        super(message);
    }
    
    public POP3Exception(Response response) {
        super(response.getMessage() + " " + response.getBody());
    }
}
