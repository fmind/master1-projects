
import components.CVSParser;
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
import views.ConsoleUserInterface;

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
        
        // Error console handler
        StreamHandler errorCh = new StreamHandler(System.err, new SimpleFormatter());
        errorCh.setLevel(Level.WARNING);
        log.addHandler(errorCh);
        
        // Information console handler
        StreamHandler infoCh = new StreamHandler(System.out, new SimpleFormatter());
        infoCh.setLevel(Level.INFO);
        infoCh.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return (record.getLevel().intValue() <= Level.INFO.intValue());
            }
        });
        log.addHandler(infoCh);
        
        // All level file handler
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
        System.out.println("[SET mode=<mode>, niveau=<niveau>, unicite=<unicite>]");
        System.out.println("[WHERE <filter> AND <filter>]");
        System.out.println("");
        System.out.println("<link> (Il faut imaginer X au bout de la flèche)");
        System.out.println("- employe> : lien entrant “Les employées de X”");
        System.out.println("- author< : lien sortant “Les oeuvres dont X est l’auteur”");
        System.out.println("- friends : bi-directionnel “les amis de X”");
        System.out.println("");
        System.out.println("<node> nom du noeud (ex: Bob, Société Corp …)");
        System.out.println("");
        System.out.println("<mode>");
        System.out.println("- L: largeur d’abord");
        System.out.println("- P: profondeur d’abord (défaut)");
        System.out.println("");
        System.out.println("<niveau> 1, 2 ou rien pour tout parcourir le graphe");
        System.out.println("");
        System.out.println("<unicité>");
        System.out.println("- O: visite unique des noeuds (défaut)");
        System.out.println("- N: autorise la visite à plusieurs reprises des noeuds");
        System.out.println("");
        System.out.println("<filter>");
        System.out.println("- opération: =, and, not");
        System.out.println("- portée: @attribut, noeud");
        System.out.println("- exemple: WHERE @since = “1999” AND NOT @to = “21/05/2012” AND NODE = “Bob”");
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
            input = new FileInputStream(filepath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("sg").log(Level.SEVERE, "Le fichier d''entrée {0} n''existe pas: {1}", new Object[]{file.getAbsolutePath(), ex.getMessage()});
            System.exit(1);
        }
        
        Logger.getLogger("sg").info("Création des composants ...");
        Parser parser = new CVSParser();
        Executer executer = new RequestExecuter();
        Interpreter interpreter = new CustomSQLInterpreter();
        UserInterface ui = new ConsoleUserInterface();
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
