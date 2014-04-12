package tftp.packets;

import java.net.DatagramPacket;

/**
 * Parent class for all TFTP packets
 * @author freaxmind
 */
public abstract class BasePacket {
    protected Short opcode;
    
    protected BasePacket() {}
    
    protected BasePacket(short opcode) {
        this.opcode = opcode;
    }
    
    /**
     * Convert a packet (java) to a datagram
     * @return 
     */
    public abstract DatagramPacket toDatagram();
}
