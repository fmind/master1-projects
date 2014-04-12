
import pop3.Client;

/**
 * Entry point for POP3 client (main)
 * @author freaxmind
 */
public class POP3 {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // GO !
        Client client = new Client();
        client.run();
    }
}
