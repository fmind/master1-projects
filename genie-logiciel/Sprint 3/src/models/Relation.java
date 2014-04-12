package models;

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
     * and the mirror relation of the target too
     * @param explored 
     */
    public void setExplored(boolean explored) {
        if (this.explored == explored) {
            return;
        }
        
        this.explored = explored;
        Relation mirror = this.target.getMirror(this);
        if (mirror == null) {
            Logger.getLogger("sg").severe("Aucune relation mirroir trouvé pour la relation " + this + " du noeud " + this.getSource());
        } else {
            mirror.setExplored(explored);
        }
    }
    
    @Override
    public String toString() {
        return this.link + " " + this.target;
    }
}
