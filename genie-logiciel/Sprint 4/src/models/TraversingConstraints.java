package models;

/**
 * Constraints to traverse a graph
 * @author freaxmind
 */
public class TraversingConstraints {
    private Mode mode;
    private int depth;                  // < 0 if no depth limit
    private Uniqueness nodeUniqueness;  // unique visit (NO or TOTAL)
    private Uniqueness linkUniqueness;  // unique visit (NO, PARTIAL or TOTAL)
    
    public static enum Mode {           // mode of traversing
        DEPTH,
        BREADTH
    }
    
    public static enum Uniqueness {     // uniqueness of visit
        NO,         // set no explored flag
        PARTIAL,    // set explored flag on one direction
        TOTAL       // set explored flag on both direction or on an element
    }
    
    /**
     * Default parameters
     */
    public TraversingConstraints() {
        this.mode = Mode.DEPTH;
        this.depth = -1;
        this.nodeUniqueness = Uniqueness.NO;
        this.linkUniqueness = Uniqueness.PARTIAL;
    }
    
    public TraversingConstraints(Mode mode, int depth, Uniqueness node, Uniqueness link) {
        this.mode = mode;
        this.depth = depth;
        this.nodeUniqueness = node;
        this.linkUniqueness = link;
    }
    
    public Mode getMode() {
        return this.mode;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public Uniqueness getNodeUniqueness() {
        return this.nodeUniqueness;
    }
    
    public Uniqueness getLinkUniqueness() {
        return this.linkUniqueness;
    }
    
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    /**
     * Uniqueness.PARTIAL = Uniqueness.TOTAL for node
     * @param uniqueness 
     */
    public void setNodeUniqueness(Uniqueness uniqueness) {
        // switch uniqueness automaticaly (no impact)
        if (uniqueness == Uniqueness.PARTIAL) {
            uniqueness = Uniqueness.TOTAL;
        }
        
        this.nodeUniqueness = uniqueness;
    }
    
    public void setLinkUniqueness(Uniqueness uniqueness) {
        this.linkUniqueness = uniqueness;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("mode=").append(this.mode).append(", ");
        builder.append("depth").append(this.depth <= 0 ? '\u221e' : this.depth).append(", ");
        builder.append("node-uniqueness=").append(this.nodeUniqueness).append(", ");
        builder.append("link-uniqueness=").append(this.linkUniqueness);
        
        return builder.toString();
    }
}
