package tftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Test;
import tftp.packets.AcknowledgmentPacket;
import tftp.packets.DataPacket;
import tftp.packets.ErrorPacket;
import tftp.packets.RequestPacket;

/**
 * Tests for the class PacketFactory
 * @author freaxmind
 */
public class PacketTest {

    @Test
    public void testReadRequest() {
        System.out.println("Request Packet READ");
        
        RequestPacket readPacket = new RequestPacket("toto.txt", true);
        DatagramPacket packet = readPacket.toDatagram();
        
        byte[] expected = {0x0, 0x1, 't', 'o', 't', 'o', '.', 't', 'x', 't', 0x0, 'b', 'i', 'n', 'a', 'r', 'y', 0x0};
        byte[] actual = packet.getData();
        
        assertTrue(Arrays.equals(expected, actual));        
    }
    
    @Test
    public void testWriteRequest() {
        System.out.println("Request Packet WRITE");
        
        RequestPacket readPacket = new RequestPacket("toto.txt", false);
        DatagramPacket packet = readPacket.toDatagram();
        
        byte[] expected = {0x0, 0x2, 't', 'o', 't', 'o', '.', 't', 'x', 't', 0x0, 'b', 'i', 'n', 'a', 'r', 'y', 0x0};
        byte[] actual = packet.getData();
        
        assertTrue(Arrays.equals(expected, actual));         
    }
    
    @Test
    public void testData() {
        System.out.println("Data Packet");
        
        byte[] data = "coucou".getBytes();
        DataPacket errorPacket = new DataPacket((short) 356, data);
        DatagramPacket packet = errorPacket.toDatagram();
        
        // Ox64 = 356
        byte[] expected = {0x0, 0x3, 0x1, 0x64, 'c', 'o', 'u', 'c', 'o', 'u'};
        byte[] actual = packet.getData();
        
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @Test
    public void testShortFileToData() throws FileNotFoundException, IOException {
        System.out.println("File To Data: test/data/short-test.txt");
        
        File file = new File("test/data/short-test.txt");
        DataPacket filePacket = new DataPacket(file);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        
        short i = 0;
        Iterator<DataPacket> it = filePacket.iterator();
        while (it.hasNext()) {
            DataPacket packet = it.next();
            content.write(packet.getBlock());
            assertEquals(++i, packet.getBlockNumber());
        }
        
        byte[] expected = Files.readAllBytes(file.toPath());
        byte[] actual = content.toByteArray();
        
        assertTrue(Arrays.equals(expected, actual));
        assertEquals(i, 1);
    }
    
    @Test
    public void testLongFileToData() throws FileNotFoundException, IOException {
        System.out.println("File To Data: test/data/long-test.txt");
        
        File file = new File("test/data/long-test.txt");
        DataPacket filePacket = new DataPacket(file);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        
        short i = 0;
        Iterator<DataPacket> it = filePacket.iterator();
        while (it.hasNext()) {
            DataPacket packet = it.next();
            content.write(packet.getBlock());
            assertEquals(++i, packet.getBlockNumber());
        }
        
        byte[] expected = Files.readAllBytes(file.toPath());
        byte[] actual = content.toByteArray();
        
        assertTrue(Arrays.equals(expected, actual));
        assertEquals(i, 3);
    }
    
    @Test
    public void testFileToDataLimit() throws FileNotFoundException, IOException {
        System.out.println("File To Data Packet: test/data/limit-test.txt");
        
        File file = new File("test/data/limit-test.txt");
        DataPacket filePacket = new DataPacket(file);
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        
        short i = 0;
        Iterator<DataPacket> it = filePacket.iterator();
        while (it.hasNext()) {
            DataPacket packet = it.next();
            content.write(packet.getBlock());
            assertEquals(++i, packet.getBlockNumber());
        }
        
        byte[] expected = Files.readAllBytes(file.toPath());
        byte[] actual = content.toByteArray();
        
        assertTrue(Arrays.equals(expected, actual));
        assertEquals(i, 3);
    }
        
    @Test
    public void testAcknowledgment() {
        System.out.println("Acknowledgment");
        
        AcknowledgmentPacket errorPacket = new AcknowledgmentPacket((short) 300);
        DatagramPacket packet = errorPacket.toDatagram();
        
        // 0x2c = 300
        byte[] expected = {0x0, 0x4, 0x1, 0x2c};
        byte[] actual = packet.getData();
      
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @Test
    public void testErrorNotDefined() {
        System.out.println("Error Packet NOT DEFINED");
        
        ErrorPacket errorPacket = new ErrorPacket(ErrorPacket.Code.NOT_DEFINED, "NOT");
        DatagramPacket packet = errorPacket.toDatagram();
        
        byte[] expected = {0x0, 0x5, 0x0, 0x0, 'N', 'O', 'T', 0x0};
        byte[] actual = packet.getData();
        
        assertTrue(Arrays.equals(expected, actual));
    }
    
    @Test
    public void testErrorIllegalOperation() {
        System.out.println("Error Packet ILLEGAL OPERATION");
        
        ErrorPacket errorPacket = new ErrorPacket(ErrorPacket.Code.ILLEGAL_OPERATION, "ILL OP");
        DatagramPacket packet = errorPacket.toDatagram();
        
        byte[] expected = {0x0, 0x5, 0x0, 0x4, 'I', 'L', 'L', ' ', 'O', 'P', 0x0};
        byte[] actual = packet.getData();
        
        assertTrue(Arrays.equals(expected, actual));
    }
}
