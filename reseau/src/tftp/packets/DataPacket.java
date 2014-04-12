package tftp.packets;

import tftp.TFTPException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Data TFTP packet
 * @author freaxmind
 */
public class DataPacket extends BasePacket implements Iterable<DataPacket> {
    private RandomAccessFile file;                  // read/write access to file
    private byte[] block;
    private Short blockNumber;
    
    public static final int MAX_BLOCK_SIZE = 512;   // see the rfc
    
    /**
     * Empty data packet
     * DataPacket() => empty data packet
     * emptyPacket() => empty datagram
     */
    public DataPacket() {
        super((short) 3);
        this.block = new byte[0];
        this.blockNumber = 0;
    }
    
    public DataPacket(short blockNumber, byte[] block) {
        super((short) 3);
        this.block = block;
        this.blockNumber = blockNumber;
    }
    
    /**
     * Usefull for the put command
     * @param file
     * @throws FileNotFoundException 
     */
    public DataPacket(File file) throws FileNotFoundException {
        super((short) 3);
        this.file = new RandomAccessFile(file, "r");
    }
    
    /**
     * Decode from datagram
     * @param packet
     * @throws TFTPException 
     */
    public DataPacket(DatagramPacket packet) throws TFTPException {
        byte[] data = packet.getData();
        
        // global length
        if (data.length < 4) {
            throw new TFTPException("Packet DATA trop court: " + data.length);
        }
        
        // operation code
        ByteBuffer opcodeField = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 2));
        this.opcode = opcodeField.getShort();
        if (this.opcode != 3) {
            throw new TFTPException("Ce n'est pas un packet DATA !");
        }
        
        // block number
        ByteBuffer blockNumberField = ByteBuffer.wrap(Arrays.copyOfRange(data, 2, 4));
        this.blockNumber = blockNumberField.getShort();
        
        // block
        this.block = Arrays.copyOfRange(data, 4, packet.getLength());
    }
    
    public short getBlockNumber() {
        return this.blockNumber;
    }
    
    public byte[] getBlock() {
        return this.block;
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
            data.write(this.block);
        } catch (IOException ex) {}
        
        DatagramPacket packet = new DatagramPacket(data.toByteArray(), data.size());
        
        return packet;
    }
    
    /**
     * Return all the data packet from a file (put command)
     * @return 
     */
    @Override
    public Iterator<DataPacket> iterator() {
        ArrayList<DataPacket> list = new ArrayList<>();
        int off = 0;    // bytes already read
        int i = 1;      // block number
        
        try {
            for (off=0, i=1; off <= this.file.length(); off+=MAX_BLOCK_SIZE, i++) {
                // size for the next block (max if it's not EOF)
                int size = (int) ((this.file.length() - off >= MAX_BLOCK_SIZE) ? MAX_BLOCK_SIZE : this.file.length() - off);
                byte[] block = new byte[size];
                
                this.file.read(block, 0, size);
                DataPacket packet = new DataPacket((short) i, block);
                list.add(packet);
            }
        } catch (IOException ex) {
            System.err.println("Impossible d'accéder au fichier local: " + ex);
            System.exit(1);
        }
        
        return list.iterator();
    }
    
    /**
     * Empty packet for receive a datagram
     * @return 
     */
    public static DatagramPacket emptyPacket() {
        byte[] data = new byte[516]; 
        DatagramPacket packet = new DatagramPacket(data, data.length);
        
        return packet;
    }
}
