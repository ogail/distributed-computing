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
public class UDPServer implements IServer {
    /*
     * Holds port of last client successful receive.
     */
    private int lastClientPort;
    
    /*
     * Holds IP Address of last client successful receive.
     */
    private InetAddress lastClientIPAddress;
    
    /*
     * The UDP socket connection
     */
    private DatagramSocket serverSocket;
    
    /*
     * The connection port number.
     */
    private int port;
            
    public UDPServer(int port) 
    throws SocketException {
        this.port = port;
        serverSocket = new DatagramSocket(this.port);
        serverSocket.setReuseAddress(true);
    }
    
    /**
     * Sends given data from a server to the client.
     *
     * @param data The data to send
     */
    @Override
    public void send(byte[] data) throws SocketException {
        DatagramPacket sendPacket = new DatagramPacket(
                data,
                data.length,
                this.lastClientIPAddress,
                this.lastClientPort);
        try {
            serverSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives expected data from the client.
     *
     * @return The received data.
     */
    public byte[] receive(int length) throws SocketException {
        byte[] data = new byte[length];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        
        try {
            serverSocket.receive(receivePacket);
            data = receivePacket.getData();
            this.lastClientIPAddress = receivePacket.getAddress();
            this.lastClientPort = receivePacket.getPort();
        } catch (Exception e) {
            Log.err("received malformed request of length n from " + 
                    receivePacket.getAddress() + " : " + port);
        }
        
        return data;
    }
    
    @Override
    public void close() {
        serverSocket.close();
    }
}
