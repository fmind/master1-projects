package components;

import controllers.Interpreter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import models.Filter;
import models.Link;
import models.Node;
import models.QueryException;
import models.Request;

/**
 * Concrete interpreter of custom SQL queries
 *
 * @author Phongphet
 */
public class CustomSQLInterpreter implements Interpreter {
    /**
     * Structure for request clauses
     */
    private static class Clauses {
        public String select;
        public String from;
        public String where;
        
        public Clauses() {
            this.select = null;
            this.from = null;
            this.where = null;
        }
    }
    
    private static final String SELECT_KEYWORD = "SELECT";
    private static final String FROM_KEYWORD = "FROM";
    private static final String WHERE_KEYWORD = "WHERE";
    
    /**
     * Slices the query and checks if all clauses are present
     * clauses are tested in other methods
     * @param query
     * @return clauses of the request
     */
    private Clauses slice(String query) throws QueryException {
        // keyword indexes
        int indexSelect = query.indexOf("SELECT");
        int indexFrom = query.indexOf("FROM");
        int indexWhere = query.indexOf("WHERE");
        int indexEnd = query.length();
        
        // required keywords
        if (indexSelect == -1) {
            throw new QueryException("Le mot clé SELECT n'est pas présent dans la requête.");
        } else if (indexFrom == -1) {
            throw new QueryException("Le mot clé FROM n'est pas présent dans la requête.");
        } else if (indexSelect > indexFrom || (indexWhere != -1 && (indexSelect > indexWhere || indexFrom > indexFrom))) {
            throw new QueryException("L'ordre \"SELECT ... FROM ... WHERE\" n'est pas respecté");
        }
        
        // duplicated keyword
        if (indexSelect != query.lastIndexOf("SELECT")) {
            throw new QueryException("Mot clé SELECT dupliqué");
        } else if (indexFrom != query.lastIndexOf("FROM")) {
            throw new QueryException("Mot clé FROM dupliqué");
        } else if (indexWhere != -1 && indexWhere != query.lastIndexOf("WHERE")) {
            throw new QueryException("Mot clé WHERE dupliqué");
        }

        // compose request clauses
        Clauses clauses = new Clauses();
        clauses.select = query.substring(indexSelect+SELECT_KEYWORD.length(), indexFrom);
        if (indexWhere != -1) { // with WHERE clause
            clauses.from = query.substring(indexFrom+FROM_KEYWORD.length(), indexWhere);
            clauses.where = query.substring(indexWhere+WHERE_KEYWORD.length(), indexEnd);
        } else { // without WHERE clause
            clauses.from =query.substring(indexFrom+FROM_KEYWORD.length(), indexEnd);
        }
        
        return clauses;
    }
    
    /**
     * Extract a list of link from the select clause
     * @param selectClause
     * @return
     * @throws QueryException
     */
    private Collection<Link> extractSelect(String selectClause) throws QueryException {
        Collection<Link> select = new LinkedList<>();
        String[] links = selectClause.split("[,]");
        
        for (String link : links) {
            link = link.trim();
            
            if (link.isEmpty()) {
                continue;
            }
            select.add(Link.fromString(link));
        }
        
        if (select.isEmpty()) {
            throw new QueryException("Aucun lien dans la clause sélection: " + selectClause);
        }
        
        return select;
    }
    
    /**
     * Extract a unique node from the from clause
     * @param fromClause
     * @return
     */
    private Node extractFrom(String fromClause) throws QueryException {
        String name = fromClause.trim();
        
        if (name.isEmpty()) {
            throw new QueryException("Aucun noeud dans la clause FROM: " + fromClause);
        }
        
        return new Node(name);
    }
    
    /**
     * Extract a list of distinct filter from the where clause
     * @param whereClause
     * @return
     */
    private Set<Filter> extractWhere(String whereClause) throws QueryException {
        Set<Filter> where = new LinkedHashSet<>();
        String[] filters = whereClause.split("AND");
        
        if (filters.length == 0) {
            throw new QueryException("Aucun filtre dans la clause WHERE");
        }
        
        for (String filter : filters) {
            filter = filter.trim();
            
            if (filter.isEmpty()) {
                throw new QueryException("Filtre vide");
            }
            
            where.add(this.extractWhereFilter(filter));
        }
            
        return where;
    }
    
    /**
     * Create a new filter from a filter clause
     * @param filterClause
     * @return
     */
    private Filter extractWhereFilter(String filterClause) throws QueryException {
        String[] elements = filterClause.split("\\s+");
        int index = 0;
   
        // trim all elements
        for (String element : elements) {
            element = element.trim();
        }
        
        // default parameters
        Filter.Type type;
        String key = "";
        String value = "";
        Filter.Operator operator = Filter.Operator.PLUS;
        int scope = -1;
        
        // operator
        if (elements[index].equals("NOT")) {
            operator = Filter.Operator.MINUS;
            index++;
        }
        
        // type
        type = this.extractType(elements[index]);
        
        // key/value
        String argsStr = elements[index].substring(1);
        String[] args = argsStr.split("=");
        
        if (args.length == 0 || (args.length > 1 && args[0].isEmpty())) {
            throw new QueryException("Clé de filtre vide");
        } else if (args.length == 1) {
            key = args[0];
        } else if (args.length == 2) {
            key = args[0];
            value = args[1];
            
            // value with space
            while (index != elements.length-1) { // has a next element
                index++;
                
                // if it's the ON part, let it handle after
                if (elements[index].equals("ON")) {
                    index--;
                    break;
                }
                
                value += " " + elements[index].trim();
            }
        } else {
            throw new QueryException("Clé/valeur du filtre non conforme: " + argsStr);
        }
        index++;
        
        // scope
        if (elements.length != index) { // has an over elements (index++ was done one line before)
            if (elements.length-1 == index+1) { // has one element (the scope value)
                try {
                    scope = Integer.valueOf(elements[index+1]);
                } catch (NumberFormatException e) {
                    throw new QueryException("La portée doit être un nombre");
                }
                
                if (scope <= 0) {
                    throw new QueryException("La portée du filtre ne peut être inférieure ou égale à 0");
                }
            } else {
                throw new QueryException("Nombre d'élement non conforme: " + elements + " (" + elements.length + " élements)");
            }
        }
        
        return new Filter(type, key, value, operator, scope);
    }
    
    /**
     * Returns a filter type from a filter element
     *
     * @param element
     * @return filter type
     * @throws QueryException
     */
    private Filter.Type extractType(String element) throws QueryException {
        if ('~' == element.charAt(0)) {
            return Filter.Type.NAME;
        } else if ('#' == element.charAt(0)) {
            return Filter.Type.LINK;
        } else if ('@' == element.charAt(0)) {
            return Filter.Type.ATTRIBUTE;
        } else {
            throw new QueryException("Type de requête non reconnue: " + element);
        }
    }

    @Override
    public Request interpret(String query) throws QueryException {
        // get clauses
        Clauses clauses = this.slice(query);
        
        // required clauses
        Collection<Link> select = this.extractSelect(clauses.select);
        Node from = this.extractFrom(clauses.from);
        Request request = new Request(select, from);
        
        // optional clauses
        if (clauses.where != null) {
            Set<Filter> where = this.extractWhere(clauses.where);
            request.setFilters(where);
        }

        return request;
    }
}