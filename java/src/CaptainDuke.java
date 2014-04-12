
import components.ConnectionHandler;
import components.GameException;
import config.GameConfig;
import controllers.GameController;
import controllers.LauncherController;
import controllers.MusicController;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import views.GameFrame;
import views.LauncherFrame;

/**
 * Program Launcher
 *
 * @author freaxmind
 */
public class CaptainDuke {

    /**
     * Initialize files and directories
     *
     * * @note can't throw exception, because we need the logger ...
     */
    private static void initializeFileSystem() {
        File saveDir = new File(GameConfig.USER_DIR);
        saveDir.mkdir();

        if (!new File(GameConfig.APP_DIR).exists()) {
            System.err.println("Le répertoire d'installation n'existe pas");
            System.exit(1);
        }
    }

    /**
     * Initialize a file logger
     *
     * @note can't throw exception, because we need the logger ...
     *
     * @throws IOException
     */
    private static void initializeLogger() {
        Logger log = Logger.getLogger("");
        log.setLevel(Level.INFO);

        try {
            FileHandler fh = new FileHandler(GameConfig.LOG_PATH, false);
            fh.setFormatter(new SimpleFormatter());
            log.addHandler(fh);
        } catch (IOException | SecurityException ex) {
            System.err.println("Impossible d'ouvrir le fichier de log: " + ex);
            System.exit(1);
        }

        log.log(Level.INFO, "Répertoire de travail: {0}", System.getProperty("user.dir"));
    }

    /**
     * Initialize a connection handler
     *
     * @note use JDBC sqlite, but we don't have to know it
     */
    private static void initializeConnection() throws GameException {
        ConnectionHandler.start();
    }

    /**
     * Initialize the look and feel SWING interface
     *
     * @note Look = Nimbus
     */
    private static void initializeLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // if Nimbus is not set, use default
            Logger.getLogger("").log(Level.WARNING, "Le look Nimbus n'est pas disponible");
        }
    }

    /**
     * @param args the command line arguments
     * @note use -nomusic flag to disable music
     */
    public static void main(String[] args) {
        // initialize components
        try {
            initializeFileSystem();
            initializeLogger();
            initializeConnection();
            initializeLookAndFeel();
        } catch (GameException ex) {
            Logger.getLogger("").log(Level.SEVERE, "Erreur lors de l''initialisation des composants: {0}", ex);
            System.exit(1);
        }

        // Set windows and controllers
        LauncherController.getInstance().setFrame(new LauncherFrame());
        GameController.getInstance().setFrame(new GameFrame());


        // run the controllers
        LauncherController.getInstance().run();
        // play music unless flag nomusic is set
        if (!Arrays.asList(args).contains("-nomusic")) {
            MusicController.getInstance().run();
        }
    }
}
