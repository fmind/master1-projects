package models;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Filter to a set of relation
 * @author freaxmind
 */
public class Filter {
    /**
     * Result of a filter operation
     * success = case where the test was true
     * fail    = case where the test was false
     * omit    = case where the test was irrelevant (e.g.: test on attribute value if attribute key not exists)
     */
    private static class FilterResult {
        public Set<Relation> success;
        public Set<Relation> fail;
        public Set<Relation> omit;
        
        public FilterResult() {
            this.success = new LinkedHashSet<>();
            this.fail = new LinkedHashSet<>();
            this.omit = new LinkedHashSet<>();
        }
    }
    
    private Type type;
    private int scope;              // -1 for the last level
    private String key;
    private String value;           // optionnal
    private Operator operator;
    
    public static enum Type {
        NAME,                       // wildcart
        ATTRIBUTE,                  // exists or value
        LINK                        // exists of value
    }
    
    public static enum Operator {
        PLUS,                       // returns the success set
        MINUS                       // returns the fail set
    }
    
    public Filter(Type type, String key, String value) {
        this(type, key, value, Operator.PLUS);
    }
    
    public Filter(Type type, String key, String value, Operator operator) {
        this(type, key, value, operator, -1);
    }
    
    public Filter(Type type, String key, String value, Operator operator, int scope) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.operator = operator;
        this.scope = scope;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Filter other = (Filter) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.scope != other.scope) {
            return false;
        }
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (this.operator != other.operator) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 83 * hash + this.scope;
        hash = 83 * hash + Objects.hashCode(this.key);
        hash = 83 * hash + Objects.hashCode(this.value);
        hash = 83 * hash + (this.operator != null ? this.operator.hashCode() : 0);
        return hash;
    }

    public Type getType() {
        return this.type;
    }
    
    public int getScope() {
        return this.scope;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public Operator getOperator() {
        return this.operator;
    }
    
    /**
     * Apply a this filter to a set of node
     * @note dispatcher
     * @param children
     * @return an other set of filtered node
     */
    public Set<Relation> apply(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        if (this.type == Type.NAME) {
            res = this.applyNameFilter(children);
        } else if (this.type == Type.ATTRIBUTE) {
            if (this.value.isEmpty()) {
                res = this.applyAttributeExistsFilter(children);
            } else {
                res = this.applyAttributeValueFilter(children);
            }
        } else if (this.type == Type.LINK) {
            if (this.value.isEmpty()) {
                res = this.applyLinkExistsFilter(children);
            } else {
                res = this.applyLinkValueFilter(children);
            }
        } 

        return (this.operator == Operator.PLUS) ? res.success : res.fail;
    }
    
    /**
     * Name filter
     * the target node name must contain the filter key substring
     * @param children
     * @return 
     */
    private FilterResult applyNameFilter(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        for (Relation rel : children) {
            if (rel.getTarget().getName().contains(this.key)) {
                res.success.add(rel);
            } else {
                res.fail.add(rel);
            }
        }
        
        return res;
    }
    
    /**
     * Attribute exists filter
     * the link must contain an attribute key with the filter key
     * @param children
     * @return 
     */
    private FilterResult applyAttributeExistsFilter(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        for (Relation rel : children) {
            if (rel.getLink().getAttributes().keySet().contains(this.key)) {
                res.success.add(rel);
            } else {
                res.fail.add(rel);
            }
        }
        
        return res;
    }
    
    /**
     * Attribute value filter
     * the link must contain an attribute key with the filter key, else it will be ommited
     * then the link attribute value must be equal to the filter value, else it will failed
     * @param children
     * @return 
     */
    private FilterResult applyAttributeValueFilter(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        for (Relation rel : children) {
            if (rel.getLink().getAttributes().keySet().contains(this.key)) {
                if (rel.getLink().getAttributes().get(this.key).equals(this.value)) {   // attribute exists, values are the same
                    res.success.add(rel);
                } else {        // attribute exists, but the attribute value is not the same
                    res.fail.add(rel);
                }
            } else {            // attribute doesn't exist
                res.omit.add(rel);
            }
        }
        
        return res;
    }
    
    /**
     * Link exists filter
     * the target must contain a node name and direction with the filter key
     * @param children
     * @return 
     */
    private FilterResult applyLinkExistsFilter(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        for (Relation rel : children) {
            Link link = Link.fromString(this.key);
            
            if (!rel.getTarget().findByLink(link).isEmpty()) {
                res.success.add(rel);
            } else {
                res.fail.add(rel);
            }
        }
        
        return res;
    }
    
    /**
     * Link value filter
     * the link must exist, else it will be ommited
     * then the link target name must be equals to the filter value, else if will failed
     * @param children
     * @return 
     */
    private FilterResult applyLinkValueFilter(Set<Relation> children) {
        FilterResult res = new FilterResult();
        
        for (Relation rel : children) {
            Link link = Link.fromString(this.key);
            
            if (!rel.getTarget().findByLink(link).isEmpty()) {
                boolean test= false;
                
                // test if the link exists
                for (Relation subRel : rel.getTarget().findByLink(link)) {
                    if (subRel.getTarget().getName().equals(this.value)) {  // attribute exists and values are the same
                        test = true;
                        break;
                    }
                }
                
                if (test) {  // attribute exists and values are the same
                    res.success.add(rel);
                } else {        // link exists but the target name is not equal to the filter value
                    res.fail.add(rel);
                }
            } else {                // link doesn't exist
                res.omit.add(rel);
            }
        }
        
        return res;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        
        if (this.operator == Operator.MINUS) {
            builder.append("NOT ");
        }
        
        switch (this.type) {
            case NAME: builder.append("~"); break;
            case ATTRIBUTE: builder.append("@"); break;
            case LINK: builder.append("#"); break;
        }
        
        builder.append(this.key);
        
        if (!this.value.isEmpty()) {
            builder.append("=").append(this.value);
        }
        
        if (this.scope != -1) {
            builder.append(" ON ").append(this.scope);
        }
        
        return builder.toString();
    }
}
