/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elogeel.speller;

import elogeel.utilities.Log;
import elogeel.utilities.Utility;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.SortedSet;

/**
 * @author Abdelrahman Elogeel
 */
public class RMIClient {
    /*
     * List of words to be checked.
     */
    String[] words;
    
    /*
     * The speller instance.
     */
    RemoteSpelling server;
    
    public RMIClient(String hostname, int port, String name, String[] words) 
            throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        server = (RemoteSpelling) registry.lookup(name);
        this.words = words;
    }
    
    public void checkWords() throws RemoteException {
        for (String word : this.words) {
            SortedSet<String> results = this.server.check(word);
            Log.out("words found for " + word + " are: " + Utility.Flatten(results, " "));
        }
    }
    
    public void addWord(String word) throws RemoteException {
        this.server.add(word);
    }
    
    public void removeWord(String word) throws RemoteException {
        this.server.remove(word);
    }
}
