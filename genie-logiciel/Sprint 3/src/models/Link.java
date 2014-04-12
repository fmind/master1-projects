package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * Links are equals if their name and direction are equals
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link other = (Link) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.direction != other.direction) {
            return false;
        }
        return true;
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
    
    public static String label(Direction direction) {
        switch (direction) {
            case IN: return "<<";
            case OUT: return ">>";
            case BOTH: return "<>";
            default: return "";
        }
    }
    
    @Override
    public String toString() {
        return this.name + "." + Link.label(this.direction);
    }
}
