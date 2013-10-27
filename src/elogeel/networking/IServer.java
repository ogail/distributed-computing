/*
 * Interface that holds essential networking APIs
 */
package elogeel.networking;

import java.net.SocketException;

/**
 * @author Abdelrahman Elogeel
 */
public interface IServer {    
    /**
     * Sends given data from a server to the client.
     *
     * @param data      The data to send
     */
    abstract public void send(byte[] data) throws SocketException;
    
    /**
     * Receives expected data from the client.
     *
     * @param data      The data to send
     */
    abstract public byte[] receive(int length) throws SocketException;
    
    /*
     * Closes and ends the server session.
     */
    abstract public void close();
}