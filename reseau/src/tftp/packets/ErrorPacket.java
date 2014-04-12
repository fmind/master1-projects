package tftp.packets;

import tftp.TFTPException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Error TFTP packet
 * @author freaxmind
 */
public class ErrorPacket extends BasePacket {
    private Code errCode;
    private String errMsg;
    
    // List of supported code
    public enum Code {
      NOT_DEFINED,
      FILE_NOT_FOUND,
      ACCESS_VIOLATION,
      DISK_FULL,
      ILLEGAL_OPERATION,
      UNKNOWN_TID,
      FILE_ALREADY_EXISTS,
      NO_SUCH_USER
    };
    
    public ErrorPacket(Code code, String message) {
        super((short) 5);
        this.errCode = code;
        this.errMsg = message;
    }
    
    /**
     * Decode a datagram packet
     * @param packet
     * @throws TFTPException 
     */
    public ErrorPacket(DatagramPacket packet) throws TFTPException {
        // global length
        byte[] data = packet.getData();
        if (data.length < 5) {
            throw new TFTPException("Packet ERR trop court: " + data.length);
        }
        
        // operation code
        ByteBuffer opcodeField = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 2));
        this.opcode = opcodeField.getShort();
        if (this.opcode != 5) {
            throw new TFTPException("Ce n'est pas un packet ERR !");
        }
        
        // error code
        ByteBuffer errCodeField = ByteBuffer.wrap(Arrays.copyOfRange(data, 2, 4));
        short err = errCodeField.getShort();
        if (err <= Code.values().length) {
            this.errCode = Code.values()[err];
        } else {
            Logger.getLogger("").log(Level.INFO, "Code d'erreur non reconnu: " + err);
            this.errCode = Code.NOT_DEFINED;
        }
        
        // error message
        if (data.length > 5) {
            this.errMsg = new String(Arrays.copyOfRange(data, 4, data.length-1));
        } else {
            this.errMsg = "";
        }
    }
    
    public Code getCode() {
        return this.errCode;
    }
    
    public String getMessage() {
        return this.errMsg;
    }
    
    @Override
    public DatagramPacket toDatagram() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ByteBuffer opcodeField = ByteBuffer.allocate(2);
        ByteBuffer errCodeField = ByteBuffer.allocate(2);
        
        opcodeField.putShort(this.opcode);
        errCodeField.putShort((short) this.errCode.ordinal());
        
        try {
            data.write(opcodeField.array());
            data.write(errCodeField.array());
            data.write(this.errMsg.getBytes());
            data.write('\000');
        } catch (IOException ex) {}      
        
        DatagramPacket packet = new DatagramPacket(data.toByteArray(), data.size());
        
        return packet;
    }
}
