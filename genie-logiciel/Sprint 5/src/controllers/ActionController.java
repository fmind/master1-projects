package controllers;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.ExecutionException;
import models.Link;
import models.Link.Direction;
import models.Node;
import models.ParseException;
import models.QueryException;
import models.Request;
import models.TraversingConstraints;

/**
 * Only controller of the application (MVC2).
 * handle the workflow
 * @author freaxmind
 */
public class ActionController implements Runnable {
    // components
    private Parser parser;
    private Interpreter interpreter;
    private Executer executer;
    private UserInterface ui;
    // model
    private Node root;
    
    /**
     * Empty constructor
     */
    public ActionController() {
        this.parser = null;
        this.interpreter = null;
        this.executer = null;
        this.ui = null;
        this.root = null;
    }
    
    public Node getRoot() {
        return root;
    }
    
    public void setParser(Parser parser) {
        this.parser = parser;
        this.generateGraph();
    }
    
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }
    
    public void setExecuter(Executer executer) {
        this.executer = executer;
    }
    
    public void setUI(UserInterface ui) {
        this.ui = ui;
    }
    
    /**
     * Check that all components are set
     */
    public void checkRequirements() {
        if (this.parser == null) {
            Logger.getLogger("sg").log(Level.SEVERE, "Parseur non initialisé");
            System.exit(1);
        } else if (this.interpreter == null) {
            Logger.getLogger("sg").log(Level.SEVERE, "Interpreteur non initialisé");
            System.exit(1);
        } else if (this.executer == null) {
            Logger.getLogger("sg").log(Level.SEVERE, "Executeur non initialisé");
            System.exit(1);
        } else if (this.ui == null) {
            Logger.getLogger("sg").log(Level.SEVERE, "Interface utilisateur non initialisé");
            System.exit(1);
        }
    }
    
    /**
     * Generate a graph using the parser
     * after the programm launcher, before the query handler
     */
    private void generateGraph() {
        // root node and relation
        this.root = new Node("SocialGraph");
        Link in = new Link("in", Direction.IN);
        
        try {
            this.parser.parse(this.root, in);
        } catch (ParseException ex) {
            Logger.getLogger("sg").log(Level.SEVERE, ex.getMessage());
            // if the file has not a proper format, quit to avoid strange behaviour
            System.exit(1);
        }
    }
    
    /**
     * Resolve the source node from the request to a node from the graph
     * @todo design can be improve, although it may be too simple for refactorisation
     * @param request
     */
    private void resolve(Request request) throws ExecutionException {
        String name = request.getFrom().getName();
        Node from;
        
        // get the graph node from the request node
        if (this.root.equals(request.getFrom())) {
            from = this.root;
        } else {
            from = this.root.getByChildName(name);
        }

        // set the node if it is found, or throw an exception
        if (from == null) {
            throw new ExecutionException("Le noeud " + name + " n'existe pas dans le graphe");
        } else {
            request.setFrom(from);
        }
    }
    
    /**
     * Handle a query and returns the result after execution
     * @param query
     * @return
     * @throws QueryException
     * @throws ExecutionException 
     */
    public Set<Node> handleQuery(String query) throws QueryException, ExecutionException {
        Request request = this.interpreter.interpret(query);
        this.resolve(request);
        Set<Node> result = this.executer.execute(request);
        
        return result;
    }
    
    /**
     * Handle a query with constraints and returns the result after execution
     * @param query
     * @param constraints
     * @return
     * @throws QueryException
     * @throws ExecutionException 
     */
    public Set<Node> handleQueryWithConstraints(String query, TraversingConstraints constraints) throws QueryException, ExecutionException {
        Request request = this.interpreter.interpret(query);
        request.setConstraints(constraints);
        this.resolve(request);
        Set<Node> result = this.executer.execute(request);
        
        return result;
    }
    
    /**
     * Call after initialization to start the user interface
     */
    @Override
    public void run() {
        this.checkRequirements();
        
        this.ui.setController(this);
        this.ui.run();
    } 
}
