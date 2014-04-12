package models;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import models.Link.Direction;

/**
 * Graph node
 * @author freaxmind
 */
public class Node {
    private String name;                        // user friendly name and ID (case sensitive)
    private Set<Relation> relations;            // relation with other nodes
    private boolean explored;                   // flag for traversing (visited/not visited)
    
    public Node(String name) {
        this.name = name;
        this.relations = new LinkedHashSet<>();
        this.explored = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    public String getName() {
       return this.name; 
    }
    
    public Set<Relation> getRelations() {
        return this.relations;
    }
    
    /**
     * Returns all distinct node childs
     * @return 
     */
    public Set<Node> getChildren() {
        Set<Node> children = new LinkedHashSet<>();
        
        for (Relation r : this.relations) {
            if (!children.contains(r.getTarget())) {
                children.add(r.getTarget());
            }
        }
        
        return children;
    }
    
    public boolean isExplored() {
        return this.explored;
    }
    
    public void setExplored(boolean explored) {
        this.explored = explored;
    }
    
    /**
     * Return the mirror relation of a node
     * @note a node MUST have a mirror relation
     * @todo create it if it's not the case ?
     * @param mirror
     * @return 
     */
    public Relation getMirror(Relation mirror) {
        for (Relation r : this.relations) {
            if (r.getTarget() == mirror.getSource() && r.getLink().getOpposite() != null && r.getLink().getOpposite().equals(mirror.getLink())) {
                return r;
            }
        }
        
        return null;
    }
    
    /**
     * Test if this node contains the parameter node name
     * @param name node name
     * @return 
     */
    public boolean contains(String name) {
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns a child by his name
     * @note a node can contain the same child on different relation, but it's the same reference
     * @param node node name
     * @return 
     */
    public Node getByChildName(String name) {
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(name)) {
                return r.getTarget();
            }
        }
        
        return null;
    }
    
    /**
     * Return all relations that have the parameter node name in target
     * @param name
     * @return 
     */
    public Set<Relation> findByTargetName(String name) {
        Set<Relation> res = new LinkedHashSet<>();
        
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(name)) {
                res.add(r);
            }
        }
        
        return res;
    }
    
    public Set<Relation> findByLink(Link link) {
        return this.findByLink(link.getName(), link.getDirection(), link.getAttributes());
    }
    
    public Set<Relation> findByLink(String name, Direction direction) {
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
    public Set<Relation> findByLink(String name, Direction direction, Map<String, String> attributes) {
        Set<Relation> res = new LinkedHashSet<>();
        Link test = new Link(name, direction, attributes);
        
        for (Relation r : this.relations) {
            Link l = r.getLink();
            
            if (!l.match(test)) {
                continue;
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
    
    /**
     * Call to addRelation and addMirrorRelation
     * @param link
     * @param target 
     */
    public void addDirectAndMirrorRelation(Link link, Node target) {
        this.addRelation(new Relation(link, target));
        this.addMirrorRelation(new Relation(link, target));
    }

    /**
     * Remove all explored flag (on node and relation)
     */
    public void clean() {
        this.setExplored(false);
        
        for (Relation r : this.relations) {
            if (!r.isExplored()) {
                continue;
            }
            
            r.setExplored(false, true);
            r.getTarget().clean();
        }
    }

    /**
     * Print a node
     * debug and test purpose only !
     */
    public void print() {
        this.print(this, 0);
        this.clean();
    }
    
    private void print(Node node, int level) {
        if (node.isExplored()) {
            return;
        }
        node.setExplored(true);
        
        // for root node only
        if (level == 0) {
            System.out.println(node);
        }
        
        for (Relation r : node.relations) {
            if (r.isExplored()) {
                continue;
            }
            r.setExplored(true, true);
            
            System.out.println(String.format("%-"+ ((level+1)*2) + "s %s", "", r));
            this.print(r.getTarget(), ++level);
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
