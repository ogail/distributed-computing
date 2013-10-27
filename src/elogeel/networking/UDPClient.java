/*
 * This class encapsulates basic functionality for UDP network protocol
 */
package elogeel.networking;

import elogeel.utilities.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdelrahman Elogeel
 */
public class UDPClient extends Client {
    
    private DatagramSocket clientSocket;
    
    public static int TIMEOUT = 10000;
            
    public UDPClient(int serverPort, InetAddress ipAddress) 
    throws SocketException {
        super(serverPort, ipAddress);
        clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(0);
    }
    
    /**
     * Sends given data from a client to the server.
     *
     * @param data      The data to send
     */
    @Override
    public void send(byte[] data) throws SocketException {
        DatagramPacket sendPacket = new DatagramPacket(
                data,
                data.length,
                this.ipAddress,
                this.serverPort);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives expected data from the server.
     *
     * @return The received data.
     */
    @Override
    public byte[] receive(int length) throws SocketException, IOException {
        byte[] data = new byte[length];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        
        clientSocket.receive(receivePacket);
        data = receivePacket.getData();
        
        return data;
    }
    
    @Override
    public void close() {
        clientSocket.close();
    }
}
