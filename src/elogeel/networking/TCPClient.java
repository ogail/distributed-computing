/*
 * This class encapsulates basic functionality for UDP network protocol
 */
package elogeel.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdelrahman Elogeel
 */
public class TCPClient extends Client {
    
    private Socket clientSocket;
    
    public static int TIMEOUT = 10000;
            
    public TCPClient(int serverPort, InetAddress ipAddress)
    throws SocketException, IOException {
        super(serverPort, ipAddress);
        clientSocket = new Socket(ipAddress, serverPort);
        clientSocket.setSoTimeout(TIMEOUT);
    }
    
    /**
     * Sends given data from a client to the server.
     *
     * @param data The data to send
     */
    @Override
    public void send(byte[] data) throws SocketException {
        try {
            clientSocket.getOutputStream().write(data, 0, data.length);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
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
        clientSocket.getInputStream().read(data);
        
        return data;
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
