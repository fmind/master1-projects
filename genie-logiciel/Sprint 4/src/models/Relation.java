package models;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Source[Node] + Link[Link] + Target[Node]
 * @author freaxmind
 */
public class Relation {
    private Node source;
    private Link link;
    private Node target;
    private boolean explored;
    
    /**
     * Constructor this no source node
     * @param link
     * @param target 
     */
    public Relation(Link link, Node target) {
        this(null, link, target);
    }
    
    public Relation(Node source, Link link, Node target) {
        this.source = source;
        this.link = link;
        this.target = target;
        this.explored = false;
    }
    
    @Override
    public Object clone() {
        Relation r = new Relation(this.source, this.link, this.target);
        
        return r;
    }
    
    public Node getSource() {
        return this.source;
    }
    
    public Link getLink() {
        return this.link;
    }
    
    public Node getTarget() {
        return this.target;
    }
    
    public boolean isExplored() {
        return this.explored;
    }
    
    public void setSource(Node source) {
        this.source = source;
    }
    
    /**
     * Set this relation to explored
     * @param explored 
     * @param withMirror set the mirror relation of the target too
     */
    public void setExplored(boolean explored, boolean withMirror) {
        this.explored = explored;
        
        if (withMirror) {
            Relation mirror = this.target.getMirror(this);
        
            if (mirror == null) {
                Logger.getLogger("sg").log(Level.SEVERE, "Aucune relation mirroir trouv\u00e9 pour la relation {0} du noeud {1}", new Object[]{this, this.getSource()});
            } else {
                mirror.setExplored(explored, false);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.link + " " + this.target;
    }
}
