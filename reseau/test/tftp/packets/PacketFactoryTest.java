package tftp.packets;

import tftp.TFTPException;
import java.net.DatagramPacket;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the class PacketFactory
 * @author freaxmind
 */
public class PacketFactoryTest {

    @Test
    public void testReadRequest() throws TFTPException {
        System.out.println("PacketFactory Read");
        
        byte[] data = {0x0, 0x1, 't', 'o', 't', 'o', '.', 't', 'x', 't', 0x0, 'n', 'e', 't', 'a', 's', 'c', 'i', 'i', 0x0};
        DatagramPacket raw = new DatagramPacket(data, data.length);
        
        BasePacket packet = PacketFactory.fromDatagram(raw);
        
        assertTrue(packet instanceof RequestPacket);
        RequestPacket read = (RequestPacket) packet;
        assertEquals("toto.txt", read.getFilename());
        assertEquals("netascii", read.getMode());
        assertTrue(read.isRead());
    }
    
    @Test
    public void testWriteRequest() throws TFTPException {
        System.out.println("PacketFactory Write");
        
        byte[] data = {0x0, 0x2, 't', 'a', 't', 'a', '.', 't', 'x', 't', 0x0, 'b', 'i', 'n', 'a', 'r', 'y', 0x0};
        DatagramPacket raw = new DatagramPacket(data, data.length);
        
        BasePacket packet = PacketFactory.fromDatagram(raw);
        
        assertTrue(packet instanceof RequestPacket);
        RequestPacket write = (RequestPacket) packet;
        assertEquals("tata.txt", write.getFilename());
        assertEquals("binary", write.getMode());
        assertFalse(write.isRead());
    }
    
    @Test
    public void testData() throws TFTPException {
        System.out.println("PacketFactory Data");
        
        byte[] data = {0x0, 0x3, 0x1, 0x64, 'c', 'o', 'u', 'c', 'o', 'u'};
        DatagramPacket raw = new DatagramPacket(data, data.length);
        
        BasePacket packet = PacketFactory.fromDatagram(raw);
        
        assertTrue(packet instanceof DataPacket);
        DataPacket dat = (DataPacket) packet;
        assertEquals(356, dat.getBlockNumber());
        assertEquals("coucou", new String(dat.getBlock()));
        assertEquals(6, dat.getBlock().length);
    }
    
    @Test
    public void testAcknowledgment() throws TFTPException {
        System.out.println("PacketFactory Acknowledgment");
        
        // 0x2c = 300
        byte[] data = {0x0, 0x4, 0x1, 0x2c};
        DatagramPacket raw = new DatagramPacket(data, data.length);
        
        BasePacket packet = PacketFactory.fromDatagram(raw);
        
        assertTrue(packet instanceof AcknowledgmentPacket);
        AcknowledgmentPacket ack = (AcknowledgmentPacket) packet;
        assertEquals(300, ack.getBlockNumber());
    }
    
    @Test
    public void testError() throws TFTPException {
        System.out.println("PacketFactory Error");
        
        byte[] data = {0x0, 0x5, 0x0, 0x4, 'I', 'L', 'L', ' ', 'O', 'P', 0x0};
        DatagramPacket raw = new DatagramPacket(data, data.length);
        
        BasePacket packet = PacketFactory.fromDatagram(raw);
        
        assertTrue(packet instanceof ErrorPacket);
        ErrorPacket err = (ErrorPacket) packet;
        assertEquals(ErrorPacket.Code.ILLEGAL_OPERATION, err.getCode());
        assertEquals("ILL OP", err.getMessage());
    }
}
