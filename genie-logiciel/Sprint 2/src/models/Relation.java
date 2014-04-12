package models;

/**
 * Source[Node] + Link[Link] + Target[Node]
 * @author freaxmind
 */
public class Relation {
    private Node source;
    private Link link;
    private Node target;
    
    public Relation(Link link, Node target) {
        this(null, link, target);
    }
    
    public Relation(Node source, Link link, Node target) {
        this.source = source;
        this.link = link;
        this.target = target;
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
    
    public void setSource(Node source) {
        this.source = source;
    }    
    
    @Override
    public String toString() {
        return this.source + " " + this.link + " " + this.target;
    }
}
