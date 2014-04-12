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
    private boolean explored;
    
    public Node(String name) {
        this.name = name;
        this.relations = new LinkedList<>();
        this.explored = false;
    }

    public String getName() {
       return this.name; 
    }
    
    public Collection<Relation> getRelations() {
        return this.relations;
    }
    
    public boolean isExplored() {
        return this.explored;
    }
    
    /**
     * Return the mirror relation of a node
     * a node MUST have a mirror relation
     * or else, it's a bug in the parser
     * @param mirror
     * @return 
     */
    public Relation getMirror(Relation mirror) {
        for (Relation r : this.relations) {
            if (r.getTarget() == mirror.getSource() && r.getLink().getOpposite().equals(mirror.getLink())) {
                return r;
            }
        }
        
        return null;
    }
    
    /**
     * Return alls relation that have the parameter node in target
     * @param node
     * @return 
     */
    public Collection<Relation> findByTarget(Node node) {
        return this.findByTargetName(node.getName());
    }
    
    /**
     * Return a list of relation based on a node name
     * @param name
     * @return 
     */
    public Collection<Relation> findByTargetName(String name) {
        LinkedList<Relation> res = new LinkedList<>();
        
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(name)) {
                res.add(r);
            }
        }
        
        return res;
    }
    
    public Collection<Relation> findByLink(String name) {
        return this.findByLink(name, null);
    }
    
    public Collection<Relation> findByLink(String name, Direction direction) {
        return this.findByLink(name, direction, null);
    }
    
    /**
     * Return a list of relation, filter by their link name, direction and attributes
     * for attributes, relation must satified all key/value of the parameter
     * @param name
     * @param direction
     * @param attributes
     * @return 
     */
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
     * and set the relation source to this node
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
    
    /**
     * Call to addRelation and addMirrorRelation
     * @param link
     * @param target 
     */
    public void addDirectAndMirrorRelation(Link link, Node target) {
        this.addRelation(link, target);
        this.addMirrorRelation(link, target);
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }
    
    /**
     * Remove all explored flag (node and relation)
     */
    public void clean() {
        if (!this.isExplored()) {
            return;
        }
        this.setExplored(false);
        
        for (Relation r : this.relations) {
            if (!r.isExplored()) {
                continue;
            }
            r.setExplored(false);
            r.getTarget().clean();
        }
    }

    /**
     * Print a node
     * debug and test purpose
     */
    public void print() {
        this.print(this, 0);
        this.clean();
    }
    
    private void print(Node node, int indent) {
        if (node.isExplored()) {
            return;
        }
        node.setExplored(true);
        
        // for root node only
        if (indent <= 0) {
            System.out.println(node);
            indent += 2;
        }
        
        for (Relation r : node.relations) {
            if (r.isExplored()) {
                continue;
            }
            r.setExplored(true);
            
            System.out.println(String.format("%-"+ (indent+2) + "s %s", "", r));
            this.print(r.getTarget(), indent+2);
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
