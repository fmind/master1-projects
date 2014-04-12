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
        ALL,
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
    
    /**
     * Factory method
     * returns a link from its representation
     * e.g:
     *      - likes> returns Link(name=likes, direction=OUT)
     *      - author< returns Link(name=author, direction=IN)
     *      - friends returns Link(name=friends, direction=BOTH)
     * @param v
     * @return a new link
     */
    public static Link fromString(String v) {
        if (v.charAt(v.length()-1) == '>') {
            return new Link(v.substring(0, v.length()-1), Direction.OUT);
        } else if (v.charAt(v.length()-1) == '<') {
            return new Link(v.substring(0, v.length()-1), Direction.IN);
        } else if (v.charAt(v.length()-1) == '*') {
            return new Link(v.substring(0, v.length()-1), Direction.ALL);
        } else {
            return new Link(v, Direction.BOTH);
        }
    }
    
    @Override
    public Object clone() {
        Link l = new Link(this.name, this.direction, this.attributes);
        
        return l;
    }

    /**
     * Links are equals if their name and direction are equals
     * do not test * and Direction.ALL, use match method for this usage
     * @param obj
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + (this.direction != null ? this.direction.hashCode() : 0);
        return hash;
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
            case IN: return "<";
            case OUT: return ">";
            case BOTH: return "=";
            case ALL: return "*";
            default: return "";
        }
    }
        
    /**
     * Links match if:
     * - their names are equals, or name = "*"
     * - their direction are equals or direction = Direction.ALL
     * - all this.attributes match l.attributes (oriented)
     * @param o
     * @return 
     */
    public boolean match(Link l) {
        // name
        if (!(l.name.equals(this.name) || l.name.equals("*") || this.name.equals("*"))) {
            return false;
        }
        
        // direction
        if (!(l.direction.equals(this.direction) || l.direction.equals(Direction.ALL) || this.direction.equals(Direction.ALL))) {
            return false;
        }

        // attributes
        if (l.attributes != null) {
            for (String key : l.attributes.keySet()) {
                if (!this.attributes.containsKey(key) || !this.attributes.get(key).equals(l.attributes.get(key))) {
                    return false;
                }
            }
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
    
    @Override
    public String toString() {
        return this.name + "." + Link.label(this.direction);
    }
}
