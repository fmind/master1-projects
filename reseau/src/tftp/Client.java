package tftp;

import common.Command;
import common.ConsoleInterface;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import tftp.packets.AcknowledgmentPacket;
import tftp.packets.BasePacket;
import tftp.packets.DataPacket;
import tftp.packets.ErrorPacket;
import tftp.packets.PacketFactory;
import tftp.packets.RequestPacket;

/**
 * TFTP Client
 * @author freaxmind
 */
public class Client {
    private ConsoleInterface console;       // user interface
    private DatagramSocket socket;
    private SocketAddress address;
    private Integer tid;                    // = client source port
    
    private static final int MAX_TRY = 100;     // max try before fatal exception (timeout and retransmission)
    private static final int TIMEOUT = 2000;    // timeout for socket
    private static final String GET_PREPATH = "get/";
    private static final String PUT_PREPATH = "put/";
    
    // TFTP commands
    public final static ArrayList<Pattern> PATTERNS = new ArrayList<Pattern>() {
        {
            add(Pattern.compile("^help$"));
            add(Pattern.compile("^connect (25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}(\\s\\d+)?"));
            add(Pattern.compile("^get [a-zA-Z0-9-_./]+$"));
            add(Pattern.compile("^put [a-zA-Z0-9-_./]+$"));
            add(Pattern.compile("^quit$"));
        }
    };
    
    public Client() {
        this.console = new ConsoleInterface("tftp > ", PATTERNS);
        this.address = null;
        this.socket = null;
        this.tid = 0;
    }

    /**
     * Main method (infinite)
     */
    public void run() {
        System.out.println("                        BIENVENUE !\n\n");
        System.out.println("tapez \"help\" pour afficher la liste des commandes");
        
        while (true) {
            Command command = this.console.getCommand();
            String action = command.getAction();
            String[] params = command.getParams();
            
            switch (action) {
                case "help": this.help(params); break;
                case "connect": this.connect(params); break;
                case "get": this.get(params); break;
                case "put": this.put(params); break;
                case "quit": this.quit(params); break;
                default: System.out.println("Commande non reconnue");
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
        System.out.println("- get fichier: récupère un fichier sur le serveur");
        System.out.println("- put fichier: envoie un fichier sur le serveur");
        System.out.println("- quit: quitte le programme");
    }
    
    /**
     * Connect the client to a server
     * ex: connect 127.0.0.1 or connect 192.168.0.1 1069
     * careful ! udp does not check if the host is alive and the port available
     * @param params 
     */
    public void connect(String[] params) {
        System.out.println("connexion en cours ...\n");
        
        String host = (params.length == 0) ? "127.0.0.1" : params[0];
        short port = (params.length == 2) ?  Short.valueOf(params[1]) : (short) 1069;
        this.address = new InetSocketAddress(host, port);
        this.tid = (int) (1024 + Math.random() * (65535-1024));
        
        System.out.println("Host: " + host + ", Port: " + port);
        System.out.println("Identifiant de transfert: " + this.tid);
        
        try {
            this.disconnect(); // close the previous socket
            
            // connect the new one
            this.socket = new DatagramSocket(this.tid);
            this.socket.setSoTimeout(TIMEOUT);
            this.socket.connect(this.address);
            
            System.out.println("\nconnexion renseignée !");
        } catch (SocketException ex) {
            System.err.println("Impossible de se connecter au serveur");
            System.err.println(ex);
        }
    }
    
    /**
     * Get a file from a remote server
     * ex: get short-test.txt
     * @param params 
     */
    public void get(String[] params) {
        File file = new File(params[0]);
        
        // Verifications
        if (params.length != 1) {
            System.err.println("Paramètre manquant: nom du fichier à télécharger");
            return;
        }
        if (!this.isConnected()) {
            System.err.println("Vous n'êtes pas connecté au serveur");
            return;
        }
        
        // Read request
        try {
            RequestPacket read = new RequestPacket(file.getName(), true);
            this.socket.send(read.toDatagram());
        } catch (IOException ex) {
            System.err.println("Aucun serveur TFTP détecté sur l'hôte " + this.address);
            System.err.println(ex);
            return;
        }
        
        // Host is up, get the file
        try {
            this.getFile(file);
            System.out.println("Téléchargement effectué avec succès !");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Put a file to a remote server
     * @param params 
     */
    public void put(String[] params) {
        File file = new File(PUT_PREPATH + params[0]);

        // Verifications
        if (params.length != 1) {
            System.err.println("Paramètre manquant: nom du fichier à envoyer");
            return;
        }
        if (!this.isConnected()) {
            System.err.println("Vous n'êtes pas connecté au serveur");
            return;
        }
        if (!file.exists()) {
            System.err.println("Le fichier " + file.getAbsolutePath() + " n'existe pas");
            return;
        }
        
        // Write request
        try {
            RequestPacket write = new RequestPacket(file.getName(), false);
            this.socket.send(write.toDatagram());
            
            AcknowledgmentPacket ack = this.waitAcknowledgment();
            if (ack.getBlockNumber() != 0) {
                throw new TFTPException("Requête WRITE non acceptée (ack)");
            }
        } catch (IOException| TFTPException ex) {
            System.err.println("Aucun serveur TFTP détecté sur l'hôte " + this.address);
            System.err.println(ex);
            return;
        }
        
        // Host is up, send the file
        try {
            this.sendFile(file);
            System.out.println("Transfert effectué avec succès !");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Handle the file reception from a server
     * @param file
     * @throws Exception 
     */
    private void getFile(File file) throws Exception {
        RandomAccessFile output = new RandomAccessFile(GET_PREPATH + file, "rw");
        DataPacket packet = new DataPacket();
        short blockNumber = (short) 1;
        int tries = 0;      // limit the number of try

        try {   // close the output file in all case
            do {    // while is not the last packet
                SocketTimeoutException error = null;
                boolean isAck = false;

                do {    // while the data packet is not ack
                    try {
                        // receive data packet + send ack packet
                        packet = this.waitData();
                        isAck = packet.getBlockNumber() == blockNumber;
                        AcknowledgmentPacket ack = new AcknowledgmentPacket(packet.getBlockNumber());
                        this.socket.send(ack.toDatagram());
                    } catch (SocketTimeoutException ex) {
                        error = ex;
                        tries += 10;
                    }

                    if (!isAck) {
                        tries++;
                        System.err.println("Essai " + tries + " => Packet n°" + packet.getBlockNumber() + " non accepté (ack)");
                        System.err.println("retransmission ...\n");
                        if (error != null) {
                            System.err.println(error);
                        }
                    } else {
                        output.write(packet.getBlock());
                        blockNumber++;
                    }

                    // stop the transfer
                    if (tries >= MAX_TRY) {
                        throw new Exception("Nombre d'essai maximal atteint: " + tries + "\n" + "Interruption du transfert");
                    }
                } while (!isAck);
            } while (packet.getBlock().length >= 512);
        } catch (Exception ex) {
            file.delete();
            throw ex;
        } finally{
            output.close();
        }
    }
    
    /**
     * Handle the file send
     * @param file
     * @throws Exception 
     */
    private void sendFile(File file) throws Exception {
        DataPacket fileData = new DataPacket(file);
        Iterator<DataPacket> it = fileData.iterator();
        int tries = 0;      // limit the number of try

        while (it.hasNext()) {  // all file packet
            DataPacket packet = it.next();
            boolean isAck = false;
            
            do {    // while the data packet is not ack
                SocketTimeoutException error = null;
                
                try {
                    // send a data packet + wait its ack
                    this.socket.send(packet.toDatagram());
                    AcknowledgmentPacket ack = this.waitAcknowledgment();
                    isAck = ack.getBlockNumber() == packet.getBlockNumber();
                } catch (SocketTimeoutException ex) {
                    error = ex;
                }
                    
                if (!isAck) {
                    tries++;
                    System.err.println("Essai " + tries + " => Packet n°" + packet.getBlockNumber() + " non accepté (ack)");
                    System.err.println("retransmission ...\n");
                    
                    if (error != null) {
                        System.err.println(error);
                    }
                }
                
                // stop the transfer
                if (tries >= MAX_TRY) {
                    throw new Exception("Nombre d'essai maximal atteint: " + tries + "\n" + "Interruption du transfert");
                }
            } while (!isAck);
        }
    }
    
    /**
     * Wait an acknowledgment packet from a remove server (put command)
     * @return
     * @throws TFTPException
     * @throws IOException 
     */
    private AcknowledgmentPacket waitAcknowledgment() throws TFTPException, IOException {
        DatagramPacket raw = AcknowledgmentPacket.emptyPacket();
        this.socket.receive(raw);
        BasePacket response = PacketFactory.fromDatagram(raw);
        
        if (response instanceof ErrorPacket) {
            throw new TFTPException((ErrorPacket) response);
        } else if (!(response instanceof AcknowledgmentPacket)) {
            throw new TFTPException("Type d'opération non reconnu: " + response.getClass().getName());
        }

        AcknowledgmentPacket ack = (AcknowledgmentPacket) response;

        return ack;
    }
    
    /**
     * Wait a data packet from a remove server (get command)
     * @return
     * @throws TFTPException
     * @throws IOException 
     */
    private DataPacket waitData() throws TFTPException, IOException {
        DatagramPacket raw = DataPacket.emptyPacket();
        this.socket.receive(raw);
        BasePacket response = PacketFactory.fromDatagram(raw);
        
        if (response instanceof ErrorPacket) {
            throw new TFTPException((ErrorPacket) response);
        } else if (!(response instanceof DataPacket)) {
            throw new TFTPException("Type d'opération non reconnu: " + response.getClass().getName());
        }

        DataPacket data = (DataPacket) response;

        return data;
    }
    
    /**
     * Quit the programm
     * @param params 
     */
    public void quit(String[] params) {
        if (this.isConnected()) {
            System.out.println("Fermeture de la connexion ...");
            this.disconnect();
        }
        
        System.out.println("\nAu revoir !");
        System.exit(0);
    }
    
    /**
     * socket.disconnect() + test if socket is not null
     */
    public void disconnect() {
        if (this.isConnected()) {
            this.socket.disconnect();
        }
    }
    
    /**
     * socket.isConnect() + test if socket is not null
     * @return 
     */
    public boolean isConnected() {
        if (this.socket != null && this.socket.isConnected()) {
            return true;
        }
        
        return false;
    }
}
