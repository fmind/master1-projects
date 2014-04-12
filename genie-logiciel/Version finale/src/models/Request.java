package models;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Interpreted request from a String query
 * @author freaxmind
 */
public class Request {
    private Collection<Link> select;
    private Node from;
    private TraversingConstraints constraints;
    private Set<Filter> filters;
    
    /**
     * Request with default constraints
     * @param links
     * @param source 
     */
    public Request(Collection<Link> links, Node source) {
        this(links, source, new TraversingConstraints());
    }
    
    public Request(Collection<Link> links, Node source, TraversingConstraints constraints) {
        this(links, source, constraints, new LinkedHashSet<Filter>());
    }
    
    public Request(Collection<Link> links, Node source, TraversingConstraints constraints, Set<Filter> filters) {
        this.select = links;
        this.from = source;
        this.constraints = constraints;
        this.filters = filters;
    }
    
    public Collection<Link> getSelect() {
        return this.select;
    }
    
    public Node getFrom() {
        return this.from;
    }

    public TraversingConstraints getConstraints() {
        return this.constraints;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Request other = (Request) obj;
        if (!Objects.equals(this.select, other.select)) {
            return false;
        }
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        if (!Objects.equals(this.constraints, other.constraints)) {
            return false;
        }
        if (!Objects.equals(this.filters, other.filters)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.select);
        hash = 53 * hash + Objects.hashCode(this.from);
        hash = 53 * hash + Objects.hashCode(this.constraints);
        hash = 53 * hash + Objects.hashCode(this.filters);
        return hash;
    }
    
    public Set<Filter> getFilters() {
        return this.filters;
    }
    
    public void setFrom(Node from) {
        this.from = from;
    }
    
    /**
     * Returns all filters available for a level (including the last level = -1)
     * @param level
     * @return 
     */
    public Set<Filter> getFiltersByLevel(int level) {
        Set<Filter> res = new LinkedHashSet<>();
        
        for (Filter filter : this.filters) {
            if (filter.getScope() == (level+1) || (filter.getScope() == -1 && this.select.size() == (level+1))) {
                res.add(filter);
            }
        }
        
        return res;
    }
    
    public void setConstraints(TraversingConstraints constraints) {
        this.constraints = constraints;
    }
    
    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        
        builder.append("SELECT ").append(this.select).append(" ");
        builder.append("FROM ").append(this.from).append(" ");
        builder.append("SET ").append(this.constraints).append(" ");
        builder.append("WHERE ").append(this.filters);
        
        return builder.toString();
    }
}
