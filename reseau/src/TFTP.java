
import tftp.Client;

/**
 * Entry point for TFTP client (main)
 * @author freaxmind
 */
public class TFTP {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // GO !
        Client client = new Client();
        client.run();
    }
}
