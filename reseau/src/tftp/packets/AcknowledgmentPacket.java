package tftp.packets;

import tftp.TFTPException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Acknowledgment TFTP packet (ACK)
 * confirmation from a host that a packet is well received
 * @author freaxmind
 */
public class AcknowledgmentPacket extends BasePacket {
    private Short blockNumber;
    
    public AcknowledgmentPacket(short blockNumber) {
        super((short) 4);
        this.blockNumber = blockNumber;
    }
    
    /**
     * Decode a datagram packet
     * @param packet
     * @throws TFTPException 
     */
    public AcknowledgmentPacket(DatagramPacket packet) throws TFTPException {
        byte[] data = packet.getData();
        if (data.length < 4) {
            throw new TFTPException("Packet ACK trop court: " + data.length);
        }
        
        // operation code
        ByteBuffer opcodeField = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 2));
        this.opcode = opcodeField.getShort();
        if (this.opcode != 4) {
            throw new TFTPException("Ce n'est pas un packet ACK !");
        }
        
        // block number
        ByteBuffer blockNumberField = ByteBuffer.wrap(Arrays.copyOfRange(data, 2, 4));
        this.blockNumber = blockNumberField.getShort();
    }
    
    public short getBlockNumber() {
        return this.blockNumber;
    }
    
    @Override
    public DatagramPacket toDatagram() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ByteBuffer opcodeField = ByteBuffer.allocate(2);
        ByteBuffer blockNumberField = ByteBuffer.allocate(2);
        
        opcodeField.putShort(this.opcode);
        blockNumberField.putShort(this.blockNumber);
        
        try {
            data.write(opcodeField.array());
            data.write(blockNumberField.array());
        } catch (IOException ex) {}      
        
        DatagramPacket packet = new DatagramPacket(data.toByteArray(), data.size());
        
        return packet;
    }
    
    /**
     * Empty packet (get command)
     * @return 
     */
    public static DatagramPacket emptyPacket() {
        byte[] data = new byte[4]; 
        DatagramPacket packet = new DatagramPacket(data, data.length);
        
        return packet;
    }
}
