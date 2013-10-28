/*
 * This class encapsulates basic functionality for TCP network protocol
 */
package elogeel.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdelrahman Elogeel
 */

public class TCPServer implements IServer {
    /*
     * The connection socket
     */
    Socket connectionSocket;
    
    /*
     * The TCP socket connection
     */
    private ServerSocket serverSocket;
    
    /*
     * The connection socket port.
     */
    private int port;
            
    public TCPServer(int port) 
    throws SocketException, IOException {
        this.port = port;
        serverSocket = new ServerSocket(this.port);
        serverSocket.setReuseAddress(true);
    }
    
    /**
     * Sends given data from a server to the client.
     *
     * @param data The data to send
     */
    @Override
    public void send(byte[] data) throws SocketException {
        try {
            connectionSocket.getOutputStream().write(data);
            connectionSocket.getOutputStream().flush();
            connectionSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives expected data from the client.
     *
     * @return The received data.
     */
    @Override
    public byte[] receive(int length) throws SocketException {
        byte[] data = new byte[length];
        
        try {
            connectionSocket = serverSocket.accept();
            connectionSocket.getInputStream().read(data, 0, length);
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }    

    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}