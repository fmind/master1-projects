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
    
    /**
     * Returns all distinct node childs
     * @return 
     */
    public Collection<Node> getChilds() {
        Collection<Node> childs = new LinkedList<>();
        
        for (Relation r : this.relations) {
            if (!childs.contains(r.getTarget())) {
                childs.add(r.getTarget());
            }
        }
        
        return childs;
    }
    
    public boolean isExplored() {
        return this.explored;
    }
    
    public void setExplored(boolean explored) {
        this.explored = explored;
    }
    
    /**
     * Return the mirror relation of a node
     * @note a node MUST have a mirror relation (cause a strange behaviour, not a bug)
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
     * Test if a related node is contained in this node
     * @param node
     * @return 
     */
    public boolean contains(Node node) {
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(node.getName())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Test if a node is contains in this
     * @param node
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
     * Test if a node is contains in this
     * @param nodeName
     * @return 
     */
    public int contains(String nodeName) {
        int index = 0;
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(nodeName)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    
    
    /**
     * Return all relations that have the parameter node in target
     * @param node
     * @return 
     */
    public Collection<Relation> findByTarget(Node node) {
        LinkedList<Relation> res = new LinkedList<>();
        
        for (Relation r : this.relations) {
            if (r.getTarget().getName().equals(node.name)) {
                res.add(r);
            }
        }
        
        return res;
    }
    
    public Collection<Relation> findByLink(Link link) {
        return this.findByLink(link.getName(), link.getDirection(), link.getAttributes());
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
     * debug and test purpose
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
