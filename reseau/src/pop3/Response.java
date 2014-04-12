package pop3;

import common.Utils;
import java.util.Arrays;

/**
 * Response from a POP3 server
 * @author freaxmind
 */
public class Response {
    private Boolean ok;             // status OK or ERR
    private String message;         // main message
    private String body;            // additional informations
    private String[] bodyLines;     // same as body, but line by line
    
    /**
     * Default constructor
     * @param ok
     * @param message
     * @param bodyLines 
     */
    public Response(boolean ok, String message, String[] bodyLines) {
        this.ok = ok;
        this.message = message;
        this.bodyLines = bodyLines;
        this.body = Utils.textFromLines(this.bodyLines);
    }
    
    /**
     * Constructor from a raw input
     * @param raw
     * @param length
     * @throws POP3Exception 
     */
    public Response(char[] raw, int length) throws POP3Exception {
        String response = new String(raw, 0, length);
        String[] lines = response.split("\r\n");
        
        // check terminaison
        if (lines.length == 0) {
            throw new POP3Exception("Aucune réponse du serveur");
        } else if (lines.length == 1 && !response.endsWith("\r\n")) {
            throw new POP3Exception("Réponse uniligne mal terminée" + response);
        } else if (lines.length > 1 && !response.endsWith("\r\n.\r\n")) {
            throw new POP3Exception("Réponse multiligne mal terminée" + response);
        }
        
        // separation between status and message
        String l1 = lines[0];
        int isplit = l1.indexOf(" ");
        
        // status
        String status = l1.substring(0, isplit);
        boolean isOk;
        if (status.equals("+OK")) {
            this.ok = true;
        } else if (status.equals("-ERR")) {
            this.ok = false;
        } else {
            throw new POP3Exception("Réponse mal formatée: " + response);
        }
        
        // message
        this.message = l1.substring(isplit+1);
        
        // body (can be empty)
        if (lines.length > 1) {
            this.bodyLines = Arrays.copyOfRange(lines, 1, lines.length-1);
        } else {
            this.bodyLines = new String[0];
        }
        this.body = Utils.textFromLines(this.bodyLines);
    }
    
    public boolean isOK() {
        return this.ok;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public String[] getBodyLines() {
        return this.bodyLines;
    }
    
    public String getStatus() {
        return (this.ok) ? "OK" : "Erreur";
    }
    
    @Override
    public String toString() {
        return this.getStatus() + " => " + this.message + " (" + this.getBody() +")";
    }
}
