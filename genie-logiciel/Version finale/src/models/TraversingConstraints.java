package models;

/**
 * Constraints to traverse a graph
 * @author freaxmind
 */
public class TraversingConstraints {
    private Mode mode;
    private int depth;                  // < 0 if no depth limit
    private Uniqueness nodeUniqueness;  // unique visit for nodes (NO or TOTAL)
    private Uniqueness linkUniqueness;  // unique visit for links (NO, PARTIAL or TOTAL)
    
    public static enum Mode {
        DEPTH,
        BREADTH
    }
    
    public static enum Uniqueness {
        NO,         // don't set an explored flag (link or node)
        PARTIAL,    // set an explored flag on one direction (link)
        TOTAL       // set an explored flag on both direction (link) or on the element (node)
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraversingConstraints other = (TraversingConstraints) obj;
        if (this.mode != other.mode) {
            return false;
        }
        if (this.depth != other.depth) {
            return false;
        }
        if (this.nodeUniqueness != other.nodeUniqueness) {
            return false;
        }
        if (this.linkUniqueness != other.linkUniqueness) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.mode != null ? this.mode.hashCode() : 0);
        hash = 47 * hash + this.depth;
        hash = 47 * hash + (this.nodeUniqueness != null ? this.nodeUniqueness.hashCode() : 0);
        hash = 47 * hash + (this.linkUniqueness != null ? this.linkUniqueness.hashCode() : 0);
        return hash;
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
