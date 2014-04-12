package models;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Interpreted request from a String query
 * @author freaxmind
 */
public class Request {
    private Collection<Link> select;
    private Node from;
    private Mode mode;
    private int depth;              // < 0 for no limit
    private boolean uniqueness;     // unique visit or not
    
    public static enum Mode {
        DEPTH,
        BREADTH
    }
    
    /**
     * Request with default parameters
     * @param links
     * @param source 
     */
    public Request(Collection<Link> links, Node source) {
        this.select = new LinkedList<>();
        this.from = source;
        this.mode = Mode.DEPTH;
        this.depth = -1;
        this.uniqueness = true;
    }
    
    public Collection<Link> getSelect() {
        return this.select;
    }
    
    public Node getFrom() {
        return this.from;
    }
    
    public Mode getMode() {
        return this.mode;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public boolean getUniqueness() {
        return this.uniqueness;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT ").append(this.select).append("\n");
        builder.append("FROM ").append(this.from).append("\n");
        builder.append("SET mode=").append(this.mode).append(", niveau=").append(this.depth).append("unicité=").append(this.uniqueness);
        
        return builder.toString();
    }
}
