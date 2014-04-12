package tftp;

import tftp.packets.ErrorPacket;

/**
 * Exception during encoding/decoding or putting/getting a packet
 * @author freaxmind
 */
public class TFTPException extends Exception {

    public TFTPException(String message) {
        super(message);
    }
    
    public TFTPException(ErrorPacket packet)  {
        super(packet.getMessage() + "(Code " + packet.getCode().ordinal() + ": " + packet.getCode() + ")");
    }
}
