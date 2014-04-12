package tftp.packets;

import tftp.TFTPException;
import java.net.DatagramPacket;

/**
 * Convert a datagram to a TFTP packet (java)
 * @author freaxmind
 */
public class PacketFactory {
    
    /**
     * Factory method. Datagram => BasePacket
     * @param packet
     * @return
     * @throws TFTPException 
     */
    public static BasePacket fromDatagram(DatagramPacket packet) throws TFTPException {
        byte[] data = packet.getData();
        
        // minimum length for TFTP packet
        if (data.length < 2) {
            throw new TFTPException("La longueur du paquet est insuffisante");
        }
        
        short opcode = data[1];
        switch (opcode) {
            case 1: return new RequestPacket(packet);
            case 2: return new RequestPacket(packet);
            case 3: return new DataPacket(packet);
            case 4: return new AcknowledgmentPacket(packet);
            case 5: return new ErrorPacket(packet);
            default: throw new TFTPException("Code d'opération inconnu: " + opcode);
        }
    }
}
