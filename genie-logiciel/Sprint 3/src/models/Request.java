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
    private TraversingConstraints constraints;
    
    /**
     * Request with default parameters
     * @param links
     * @param source 
     */
    public Request(Collection<Link> links, Node source) {
        this.select = new LinkedList<>();
        this.from = source;
        this.constraints = new TraversingConstraints();
    }
    
    public Collection<Link> getSelect() {
        return this.select;
    }
    
    public Node getFrom() {
        return this.from;
    }

    public TraversingConstraints getConstraints() {
        return this.constraints;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("SELECT ").append(this.select).append("\n");
        builder.append("FROM ").append(this.from).append("\n");
        builder.append("SET ").append(this.constraints);
        
        return builder.toString();
    }
}
