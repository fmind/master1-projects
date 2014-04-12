package pop3;

import common.Command;
import common.ConsoleInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * POP3 Client
 * @author freaxmind
 */
public class Client {
    private ConsoleInterface console;               // user interface
    private Socket socket;                          // TCP socket
    private SocketAddress address;                  // address + port
    private BufferedReader netin;                   // socket input
    private BufferedWriter netout;                  // socket output
    private HashMap<Integer, Message> messages;     // messages stored localy
    
    private static final int TIMEOUT = 2000;        // socket timeout
    private static final int REPONSE_MAX_SIZE = 512;
    
    // POP3 commands
    public final static ArrayList<Pattern> PATTERNS = new ArrayList<Pattern>() {
        {
            add(Pattern.compile("^help$"));
            add(Pattern.compile("^connect (25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}(\\s\\d+)?$"));
            add(Pattern.compile("^auth \\w+ \\w+$"));
            add(Pattern.compile("^stat$"));
            add(Pattern.compile("^collect$"));
            add(Pattern.compile("^list$"));
            add(Pattern.compile("^read \\d+$"));
            add(Pattern.compile("^quit$"));
        }
    };
    
    public Client() {
        this.console = new ConsoleInterface("pop3 > ", PATTERNS);
        this.address = null;
        this.socket = null;
        this.netin = null;
        this.netout = null;
        this.messages = new HashMap<>();
        
        // Shortcut for debugging
        /*
        try {
            String[] paramsConnect = {"127.0.0.1"};
            String[] paramsAuth = {"bob", "azerty"};
            String[] paramsRead = {"5"};
            String[] params = {};
            this.connect(paramsConnect);
            this.auth(paramsAuth);
            this.stat(params);
            this.collect(params);
            this.list(params);
            this.read(paramsRead);
            this.quit(params);
        } catch (IOException|POP3Exception ex) {
            System.err.println("ERREUR INIT");
            System.err.println(ex);
        }
        */
    }
    
    /**
     * Wait for a response from the POP3 server
     * @return
     * @throws IOException
     * @throws POP3Exception 
     */
    private Response getResponse() throws IOException, POP3Exception {
        char[] raw = new char[REPONSE_MAX_SIZE];
        int length = 0;
        
        try {
            length = this.netin.read(raw);
        } catch(IOException ex) {
            throw new IOException("Impossible de récupérer la réponse du serveur: " + ex);
        }
        
        Response response = new Response(raw, length);
        
        return response;
    }
    
    /**
     * Shortcut. Raise an exception if the status of the response is not OK
     * @return
     * @throws IOException
     * @throws POP3Exception 
     */
    private Response getResponseOrException() throws IOException, POP3Exception {
        Response response = this.getResponse();
        
        if (!response.isOK()) {
            throw new POP3Exception(response);
        }
        
        return response;
    }
    
    /**
     * Send a command to a pop3 server
     * TCP connect has to be established
     * @param command
     * @throws IOException
     * @throws POP3Exception 
     */
    private void sendCommand(String command) throws IOException, POP3Exception {
        if (!this.isConnected()) {
            throw new POP3Exception("Vous n'êtes pas connecté au serveur");
        }
        
        this.netout.write(command + "\n");
        this.netout.flush();
    }
    
    /**
     * Main method (infinite)
     * @note too lazy to do a command pattern ... so I write a decorator pattern instead
     */
    public void run() {
        System.out.println("                        BIENVENUE !\n\n");
        System.out.println("tapez \"help\" pour afficher la liste des commandes");
        
        while (true) {
            Command command = this.console.getCommand();
            String action = command.getAction();
            String[] params = command.getParams();

            // dispatcher and decorator
            switch (action) {
                case "help": this.help(params); break;
                case "connect": this.runCommand(action, params, false, "Erreur lors de la connection au serveur"); break;
                case "auth": this.runCommand(action, params, true, "Erreur lors de l'authentification"); break;
                case "stat": this.runCommand(action, params, true, "Erreur lors de la récupération des statistiques du serveur"); break;
                case "collect": this.runCommand(action, params, true, "Erreur lors de la récupération de vos emails"); break;
                case "list": this.runCommand(action, params, true, "Erreur lors de l'affichage de vos messages"); break;
                case "read": this.runCommand(action, params, true, "Impossible de la lecture de l'un de vos messages"); break;
                case "quit": this.runCommand(action, params, false, "Erreur lors de la terminaison du client"); break;
                default: System.out.println("Commande non reconnue");
            }
        }
    }
    
    /**
     * Decorator to invoke client command
     * @param methodName
     * @param params
     * @param needConnection raise an exception if the client need to be connected
     * @param errorMessage 
     */
    private void runCommand(String methodName, String[] params, boolean needConnection, String errorMessage) {
        // check connection
        if (needConnection && !this.isConnected()) {
            System.err.println("Vous devez vous connecter à un serveur pour exécuter cette commande");
            return;
        }
        
        Method method = null;
        
        // get method
        try {
            method = getClass().getDeclaredMethod(methodName, String[].class);
        } catch (NoSuchMethodException|SecurityException ex) {
            System.err.println("Impossible d'invoquer la méthode: " + methodName);
        }

        // call method
        try {
            method.invoke(this, (Object) params);
        } catch (Exception ex) {
            if (ex.getCause() instanceof IOException || ex.getCause() instanceof POP3Exception) {
                System.err.println(errorMessage + ": " + ex.getCause());
            } else {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    /**
     * Display help for the user
     * @param params 
     */
    public void help(String[] params) {
        System.out.println("\nCommandes disponibles:");
        
        System.out.println("- help: affiche l'aide");
        System.out.println("- connect host <port>: connexion à un hôte/port");
        System.out.println("- auth user pass: authentification d'un utilisateur");
        System.out.println("- stat: statistique sur le stockage des mails de votre compte");
        System.out.println("- collect: récupère les emails du compte (suppression en cas de succès)");
        System.out.println("- list: liste les messages stockés localement");
        System.out.println("- read id: lis le contenu d'un mail");
        System.out.println("- quit: quitte le programme");
    }
    
    /**
     * Connect the client to a POP3 server
     * (TCP connection only)
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void connect(String[] params) throws IOException, POP3Exception {
        System.out.println("connexion en cours ...\n");
        
        String host = (params.length == 0) ? "127.0.0.1" : params[0];
        short port = (params.length == 2) ?  Short.valueOf(params[1]) : (short) 1100;
        this.address = new InetSocketAddress(host, port);
        
        System.out.println("Host: " + host + ", Port: " + port);
        
        // close the previous socket
        this.disconnect(); 

        // connect the new one
        this.socket = new Socket();
        this.socket.setSoTimeout(TIMEOUT);
        this.socket.connect(this.address);

        // set streams
        this.netout = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.netin = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        // wait for confirmation
        this.getResponseOrException();

        System.out.println("\nConnexion réussie !");
    }
    
    /**
     * Authentification for a user/pass
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void auth(String[] params) throws IOException, POP3Exception {
        if (params.length < 2) {
            System.err.println("Vous devez fournir un nom d'utilisateur et un mot de passe (voir la commande help)");
            return;
        }
        
        String user = params[0];
        String pass = params[1];
        
        this.sendCommand("USER " + user);
        this.getResponseOrException();

        this.sendCommand("PASS " + pass);
        this.getResponseOrException();

        System.out.println("Authentification réussie !");
    }
    
    /**
     * Display informations about space usage and messages
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void stat(String[] params) throws IOException, POP3Exception {
        this.sendCommand("STAT");
        Response response = this.getResponseOrException();
        
        String[] infos = response.getMessage().split(" ");
        if (infos.length != 2) {
            throw new POP3Exception("Réponse statistique mal formaté: " + response.getMessage());
        }

        String nb = infos[0];
        String space = infos[1];

        System.out.println("Statistiques sur le stockage des messages:");
        System.out.println("- nombre de message: " + nb);
        System.out.println("- espace disque: " + space + " octets");
    }
    
    /**
     * Fetch emails from the server
     * @note delete emails after they are all received
     * @use list to display the message
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void collect(String[] params) throws IOException, POP3Exception {
        System.out.println("Récupération ...\n");
        
        // retrieve all emails
        this.sendCommand("LIST");
        Response responseList = this.getResponseOrException();
        this.messages = new HashMap<>();
        for (String line : responseList.getBodyLines()) {
            String[] words = line.split(" ");
            if (words.length != 2) {
                throw new POP3Exception("Une ligne de la liste de mail est mal formaté: " + line);
            }
            
            int id = Integer.valueOf(words[0]);
            int size = Integer.valueOf(words[1]);
            
            // retrieve the content of an email
            this.sendCommand("RETR " + id);
            Response responseRead = this.getResponseOrException();
            String body = responseRead.getBody();
            
            Message mess = new Message(id, size, body);
            this.messages.put(id, mess);
        }
        
        System.out.println("Suppression ...\n");
        
        // delete all messages
        for (Message m : this.messages.values()) {
            this.sendCommand("DELE " + m.getId());
            Response responseDel = this.getResponseOrException();
        }
        
        System.out.println("Nombre de message récupéré: " + responseList.getBodyLines().length);
    }
    
    /**
     * List all the messages stored in local
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void list(String[] params) throws IOException, POP3Exception {
        if (this.messages.isEmpty()) {
            System.out.println("Vous n'avez aucun message");
            System.out.println("Avez vous récupérez vos messages avec la commande \"collect\" ?");
        } else {
            String line = String.format("%-4s |  %-55s |  %-30s", "ID", "Émetteur", "Sujet");
            System.out.println(line);
        }
        
        // nice output
        for (Message m : this.messages.values()) {
            String line = String.format("%-4s |  %-55s |  %-30s", m.getId(), m.getFrom(), m.getSubject());
            System.out.println(line);
        }
    }
    
    /**
     * Display the content of an email based on its ID
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void read(String[] params) throws IOException, POP3Exception {
        if (params.length != 1) {
            throw new POP3Exception("Vous devez fournir le numéro du message à afficher");
        }
        
        int id = Integer.valueOf(params[0]);
        Message m = this.messages.get(id);
        
        if (m != null) {
            System.out.println("Message: n°" + m.getId() + " (" + m.getSize() + " octets)");
            System.out.println(m.getRaw());
        } else {
            System.err.println("Le message n°" + id + " n'est pas disponible dans la base client");
            System.out.println("Avez vous récupérez vos messages avec la commande \"collect\" ?");
        }
    }
    
    /**
     * Quit the POP3 client.
     * terminate POP3 and TCP connection
     * @param params
     * @throws IOException
     * @throws POP3Exception 
     */
    public void quit(String[] params) throws IOException, POP3Exception {
        System.out.println("Fermeture de la connexion ...");
        
        if (this.isConnected()) {
            System.out.println("terminaison POP3 ...");

            this.sendCommand("QUIT");
            Response response = this.getResponseOrException();

            System.out.println("terminaison TCP ...");
            this.disconnect();
        }
        
        System.out.println("\nAu revoir !");
        System.exit(0);
    }
    
    /**
     * socket.disconnect() + test if socket is not null
     */
    public void disconnect() throws IOException {
        if (this.isConnected()) {
            this.socket.close();
        }
    }
    
    /**
     * socket.isConnect() + test if socket is not null
     * @return 
     */
    public boolean isConnected() {
        if (this.socket != null && this.socket.isConnected() && this.netin != null && this.netout != null) {
            return true;
        }
        
        return false;
    }
}
