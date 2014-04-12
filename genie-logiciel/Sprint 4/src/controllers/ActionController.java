package controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import models.Link;
import models.Link.Direction;
import models.Node;
import models.ParseException;

/**
 * Only controller of the application (MVC2).
 * handle all main actions
 * @author freaxmind
 */
public class ActionController {
    private Parser parser;
    private Interpreter interpreter;
    private Executer executer;
    private UserInterface ui;
    private Node root;
    
    /**
     * Empty controller
     */
    public ActionController() {
        this.parser = null;
        this.interpreter = null;
        this.executer = null;
        this.ui = null;
        this.root = null;
    }
    
    public void setParser(Parser parser) {
        this.parser = parser;
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
        }
    }
    
    private void generateGraph() {
        this.root = new Node("Social Graph");
        Link in = new Link("in", Direction.IN);
        
        try {
            this.parser.parse(root, in);
        } catch (ParseException ex) {
            Logger.getLogger("sg").log(Level.SEVERE, ex.getMessage());
            System.exit(1);
        }
    }
    
    private boolean handleQuery() {
        return false;
    }
    
    public void run() {
        this.checkRequirements();
        this.generateGraph();
        
        boolean follow = true;
        while (follow) {
            follow = this.handleQuery();
        }
    }
}
