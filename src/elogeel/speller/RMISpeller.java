/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elogeel.speller;

import elogeel.utilities.Log;
import elogeel.utilities.WordList;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.SortedSet;

/**
 *
 * @author Abdelrahman Elogeel
 */
public class RMISpeller implements RemoteSpelling {
    /*
     * The word list instance.
     */
    private WordList wordList;
    
    /*
     * The port number on which it is to create an RMI registry.
     */
    private int port;
    
    /*
     * The name to register a spelling service with (on the RMI registry it creates).
     */
    private String name;
    
    /*
     * The class constructor
     */
    public RMISpeller(int port, String name, String databaseFileName) 
            throws IOException, RemoteException, AlreadyBoundException {
        wordList = new WordList(databaseFileName);
        this.port = port;
        this.name = name;
        
        // bound the server
        Remote stub = (Remote)UnicastRemoteObject.exportObject(this, 0);
        Registry registry = LocateRegistry.createRegistry(this.port);
        registry.bind(this.name, stub);
        Log.out("RMI Server bound!");
    }
    
    @Override
    public SortedSet<String> check(String the_word) throws RemoteException {
        return wordList.getCloseWords(the_word);
    }

    @Override
    public void add(String the_word) throws RemoteException {
        wordList.add(the_word);
    }

    @Override
    public void remove(String the_word) throws RemoteException {
        wordList.remove(the_word);
    }
}
