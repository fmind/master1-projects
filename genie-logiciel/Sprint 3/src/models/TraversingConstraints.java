package models;

/**
 * Constraints for traversing a graph
 * @author freaxmind
 */
public class TraversingConstraints {
    private Mode mode;
    private int depth;              // < 0 for no limit
    private boolean uniqueness;     // unique visit or not
    
    public static enum Mode {
        DEPTH,
        BREADTH
    }
    
    /**
     * Default parameters
     */
    public TraversingConstraints() {
        this.mode = Mode.DEPTH;
        this.depth = -1;
        this.uniqueness = true;
    }
    
    public TraversingConstraints(Mode mode, int depth, boolean uniqueness) {
        this.mode = mode;
        this.depth = depth;
        this.uniqueness = uniqueness;
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
        return "mode=" + this.mode + ", depth=" + (this.depth <= 0 ? '\u221e' : this.depth) + ", uniqueness=" + this.uniqueness;
    }
}
