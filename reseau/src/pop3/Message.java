package pop3;

import common.Utils;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A message received from a POP3 server
 * @author freaxmind
 */
public class Message {
    private int id;             // unique ID
    private int size;           // space size in bytes
    // headers
    private String from;
    private String to;
    private String subject;
    private String date;
    // body
    private String body;        // message - header
    private String raw;         // message = header + body
    
    // Patterns of the message header
    public static Pattern fromPattern = Pattern.compile("^From: .*");
    public static Pattern toPattern = Pattern.compile("^To: .*");
    public static Pattern datePattern = Pattern.compile("^Date: .*");
    public static Pattern subjectPattern = Pattern.compile("^Subject: .*");
    
    public Message(int id, int size, String raw) {
        this.id = id;
        this.size = size;
        this.raw = raw;
        
        // retrieve header fields
        String[] lines = raw.split("\n");
        int i = 0;
        for (String line : lines) {
            int isplit = line.indexOf(" ")+1;
            i++;

            if (Pattern.matches(fromPattern.pattern(), line)) {
                this.from = line.substring(isplit);
            } else if (Pattern.matches(toPattern.pattern(), line)) {
                this.to = line.substring(isplit);
            } else if (Pattern.matches(datePattern.pattern(), line)) {
                this.date = line.substring(isplit);
            } else if (Pattern.matches(subjectPattern.pattern(), line)) {
                this.subject = line.substring(isplit);
            } else {
                break;
            }
        }
        
        String[] bodyLines = Arrays.copyOfRange(lines, i, lines.length);
        this.body = Utils.textFromLines(bodyLines);
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public String getFrom() {
        return this.from;
    }
    
    public String getTo() {
        return this.to;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public String getRaw() {
        return this.raw;
    }
    
    /**
     * User friendly display
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("[").append(this.id).append("] ");
        builder.append(this.subject);
        builder.append("\nde ").append(this.from).append(" ");
        builder.append("à ").append(this.to);
        if (this.date != null && !this.date.isEmpty()) {
            builder.append("\nEnvoyé le: ").append(this.date);
        }
        builder.append("\nContenu:\n");
        builder.append(this.body);
        
        return builder.toString();
    }
}
