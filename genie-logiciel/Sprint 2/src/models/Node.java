package models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import models.Link.Direction;

/**
 * Graph node
 * @author freaxmind
 */
public class Node {
    private String name;                        // user friendly name
    private Collection<Relation> relations;     // relation with other nodes
    
    public Node(String name) {
        this.name = name;
        this.relations = new LinkedList<>();
    }

    public String getName() {
       return this.name; 
    }
    
    public Collection<Relation> getRelations() {
        return this.relations;
    }
    
    public Relation findByTarget(Node node) {
        return this.findByTargetName(node.getName());
    }
    
    public Relation findByTargetName(String name) {
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(name)) {
                return r;
            }
        }
        
        return null;
    }
    
    public Collection<Relation> findByLink(String name) {
        return this.findByLink(name, null);
    }
    
    public Collection<Relation> findByLink(String name, Direction direction) {
        return this.findByLink(name, direction, null);
    }
    
    public Collection<Relation> findByLink(String name, Direction direction, Map<String, String> attributes) {
        LinkedList<Relation> res = new LinkedList<>();
        
        for (Relation r : this.relations) {
            Link l = r.getLink();
            
            // name and direction
            if (!l.getName().equals(name)) {
                continue;
            } else if (direction != null && !l.getDirection().equals(direction)) {
                continue;
            }
            
            // attributes
            if (attributes != null) {
                boolean pass = false;
                
                for (String key : attributes.keySet()) {
                    if (!l.getAttributes().containsKey(key) || !l.getAttributes().get(key).equals(attributes.get(key))) {
                        pass = true;
                        break;
                    }
                }
                
                if (pass == true) {
                    continue;
                }
            }
            
            res.add(r);
        }
        
        return res;
    }
    
    /**
     * Add a new relation to this node
     * set the source to this node
     * @param relation 
     */
    public void addRelation(Relation relation) {
        relation.setSource(this);
        this.relations.add(relation);
    }
    
    public void addRelation(Link link, Node target) {
        Relation r = new Relation(link, target);
        
        this.addRelation(r);
    }
    
    /**
     * Add a mirror relation
     * reverse = opposite link + swap(target, source)
     * @param relation 
     */
    public void addMirrorRelation(Relation relation) {
        Link opposite = relation.getLink().getOpposite();
        Relation mirror = new Relation(relation.getTarget(), opposite, this);
        relation.getTarget().addRelation(mirror);
    }
    
    public void addMirrorRelation(Link link, Node target) {
        Relation r = new Relation(link, target);
        
        this.addMirrorRelation(r);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
