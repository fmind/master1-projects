
import components.CSVParser;
import components.CustomSQLInterpreter;
import components.RequestExecuter;
import controllers.ActionController;
import controllers.Executer;
import controllers.Parser;
import controllers.Interpreter;
import controllers.UserInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import views.GraphicalUserInterface;

/**
 * Program launcher (main)
 * @author freaxmind
 */
public class SocialGraph {
    // Fixed configurations
    private static String logFilepath = "logs/dev.txt";
    private static Level logLevel = Level.INFO;

    /**
     * Initialize loggers (console or file)
     * logger name is "sg"
     */
    private static void initializeLoggers() {
        // Application logger
        Logger log = Logger.getLogger("sg");
        log.setUseParentHandlers(false);
        log.setLevel(SocialGraph.logLevel);
        
        // Error only console handler
        StreamHandler errorCh = new StreamHandler(System.err, new SimpleFormatter());
        errorCh.setLevel(Level.WARNING);
        log.addHandler(errorCh);
        
        // Information only console handler
        StreamHandler infoCh = new StreamHandler(System.out, new SimpleFormatter());
        infoCh.setLevel(Level.INFO);
        infoCh.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return (record.getLevel().intValue() <= Level.INFO.intValue());
            }
        });
        log.addHandler(infoCh);
        
        // Information file handler
        FileHandler fh= null;
        try {
            fh = new FileHandler(SocialGraph.logFilepath, false);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException|SecurityException ex) {
            System.err.println("Impossible d'ouvrir le fichier de log " + logFilepath + ": " + ex.getMessage());
            System.exit(1);
        }
        log.addHandler(fh);

        log.log(Level.INFO, "Répertoire de travail: {0}\n", System.getProperty("user.dir"));
    }
    
    /**
     * Display an help to use the program
     * @todo to review
     */
    private static void help() {
        System.out.println("Utilisation: socialgraph FICHIER");
        System.out.println("Parcours de graphe de réseau social.");
        System.out.println("");
        System.out.println("Arguments:");
        System.out.println("- FICHIER: fichier d'entrée au format CSV");
        System.out.println("");
        System.out.println("Syntaxe:");  
        System.out.println("SELECT <link>, <link>");
        System.out.println("FROM <node>");
        System.out.println("[WHERE <filter> AND <filter>]");
        System.out.println("");
        System.out.println("<link> (Il faut imaginer X au bout de la flèche)");
        System.out.println("- employe< : lien entrant “Les employées de X”");
        System.out.println("- author> : lien sortant “Les oeuvres dont X est l’auteur”");
        System.out.println("- friends : bi-directionnel “les amis de X”");
        System.out.println("");
        System.out.println("<node> nom du noeud (ex: Bob, Société Corp …)");
        System.out.println("");
        System.out.println("<filter>");
        System.out.println("- opérator: rien pour la condition normale ou NOT pour inverser la condition");
        System.out.println("- type: ~ pour nom, @ pour attribut, # pour lien");
        System.out.println("- clé: sujet du filtre");
        System.out.println("- valeur: valeur du filtre. Laisser vide pour simplement vérifier l'existence de la clé");
        System.out.println("- portée: niveau du lien sur lequel s'applique le filtre ou rien pour le dernier lien (ON 1)");
        System.out.println("- exemple: WHERE #employee_of ON 1 AND ~Bob ON 2 AND NOT @to=21/05/2012");
        System.out.println("- noeud avec des liens employées pour le niveau 1, noeud dont les nom contient Bob pour le niveau 2, lien avec un attribut to = 21/05/2012");
    }
    
    /**
     * Main method
     * @param args 
     */
    public static void main(String[] args) {
        SocialGraph.initializeLoggers();

        Logger.getLogger("sg").info("Vérification des arguments du programme ...");
        if (args.length !=1) {
            Logger.getLogger("sg").severe("socialgraph: Argument manquant");
            SocialGraph.help();
            System.exit(1);
        }
        String filepath = args[0];
        
        Logger.getLogger("sg").info("Ouverture du flux d'entrée ...");
        File file = new File(filepath);
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("sg").log(Level.SEVERE, "Le fichier d''entrée {0} n''existe pas: {1}", new Object[]{file.getAbsolutePath(), ex.getMessage()});
            System.exit(1);
        }
        
        Logger.getLogger("sg").info("Création des composants ...");
        Parser parser = new CSVParser();
        Executer executer = new RequestExecuter();
        Interpreter interpreter = new CustomSQLInterpreter();
        UserInterface ui = new GraphicalUserInterface();
        
        try {
            parser.load(input);
        } catch (IOException ex) {
            Logger.getLogger("sg").log(Level.SEVERE, "Erreur lors de l'ouverture du fichier d'entrée {0}: {1}", new Object[]{file.getAbsolutePath(), ex.getMessage()});
            System.exit(1);
        }
        
        Logger.getLogger("sg").info("Démarrage du contrôleur ..");
        ActionController ctrl = new ActionController();
        ctrl.setParser(parser);
        ctrl.setInterpreter(interpreter);
        ctrl.setExecuter(executer);
        ctrl.setUI(ui);
        
        ctrl.run();
    }
}
