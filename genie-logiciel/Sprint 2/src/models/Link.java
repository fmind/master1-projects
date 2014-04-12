package models;

import java.util.HashMap;
import java.util.Map;

/**
 * Graph link
 * @author freaxmind
 */
public class Link {
    private String name;
    private Direction direction;
    private Map<String, String> attributes;
    
    public static enum Direction {
        IN,
        OUT,
        BOTH
    }
    
    /**
     * Constructor without attributes
     * @param name
     * @param direction 
     */
    public Link(String name, Direction direction) {
        this(name, direction, new HashMap<String, String>());
    }
    
    /**
     * Constructor with attributes
     * @param name
     * @param direction
     * @param attributes 
     */
    public Link(String name, Direction direction, Map<String, String> attributes) {
        this.name = name;
        this.direction = direction;
        this.attributes = attributes;
    }
    
    @Override
    public Object clone() {
        Link l = new Link(this.name, this.direction, this.attributes);
        
        return l;
    }
    
    /**
     * Returns the opposite of this link (reverse direction)
     * in => out, out => in, both => both
     * @return a new link
     */
    public Link getOpposite() {
        Link opposite = (Link) this.clone();
        
        if (this.direction == Direction.IN) {
            opposite.direction = Direction.OUT;
        } else if (this.direction == Direction.OUT) {
            opposite.direction = Direction.IN;
        }
        
        return opposite;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public Map<String, String> getAttributes() {
        return this.attributes;
    }
    
    @Override
    public String toString() {
        return this.name + "." + this.direction;
    }
}
