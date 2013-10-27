/*
 * Interface that holds essential networking APIs
 */
package elogeel.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Abdelrahman Elogeel
 */
public abstract class Client {    
    protected int serverPort;
    
    protected InetAddress ipAddress;
    
    public Client(int serverPort, InetAddress ipAddress) {
        this.serverPort = serverPort;
        this.ipAddress = ipAddress;
    }
    
    /**
     * Sends given data from a client to the server.
     *
     * @param data      The data to send
     */
    abstract public void send(byte[] data) throws SocketException;
    
    /**
     * Receives expected data from the server.
     *
     * @param data      The data to send
     */
    abstract public byte[] receive(int length) throws SocketException, IOException;
    
    /*
     * Closes the client socket.
     */
    abstract public void close();
}