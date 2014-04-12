package tftp.packets;

import tftp.TFTPException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author freaxmind
 */
public class RequestPacket extends BasePacket {
    private String filename;
    private String mode;
    private Boolean read;
    
    public RequestPacket(String filename, boolean read) {
        super((short) (read ? 1 : 2));
        this.filename = filename;
        this.mode = "binary";         // only in binary mode
        this.read = read;
    }
    
    /**
     * Decode a datagram packet
     * @param packet
     * @throws TFTPException 
     */
    public RequestPacket(DatagramPacket packet) throws TFTPException {
        byte[] data = packet.getData();
        if (data.length < 2) {
            throw new TFTPException("Packet REQ trop court: " + data.length);
        }
        
        // operation code
        ByteBuffer opcodeField = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 2));
        this.opcode = opcodeField.getShort();
        if (this.opcode != 1 && this.opcode != 2) {
            throw new TFTPException("Ce n'est pas un packet REQ !");
        }
        
        this.read = (this.opcode == 1); 
        
        // filename and mode
        String line = new String(Arrays.copyOfRange(data, 2, data.length-1));
        String[] words = line.split("\000");
        if (words.length != 2) {
            throw new TFTPException("Erreur d'échappement sur le paquet REQ: " + line);
        }

        this.filename = words[0];
        this.mode = words[1];
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public String getMode() {
        return this.mode;
    }
    
    public boolean isRead() {
        return this.read;
    }
    
    @Override
    public DatagramPacket toDatagram() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ByteBuffer opcodeField = ByteBuffer.allocate(2);
        
        opcodeField.putShort(this.opcode); 
        
        try {
            data.write(opcodeField.array());
            data.write(this.filename.getBytes());
            data.write('\000');
            data.write(this.mode.getBytes());
            data.write('\000');
        } catch (IOException ex) {}
        
        DatagramPacket packet = new DatagramPacket(data.toByteArray(), data.size());
        
        return packet;
    }
}
