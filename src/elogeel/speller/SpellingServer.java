/*
 * The main spelling server class.
 */
package elogeel.speller;

import elogeel.networking.Constants;
import elogeel.networking.NetworkProtocolType;
import elogeel.networking.IServer;
import elogeel.networking.TCPServer;
import elogeel.networking.UDPServer;
import elogeel.utilities.Utility;
import elogeel.utilities.WordLookup;
import java.io.IOException;
import java.net.SocketException;

/**
 * @author Abdelrahman Elogeel
 */
public class SpellingServer {
    
    public static void SpellChecker(
            NetworkProtocolType type,
            int port,
            String databaseFileName,
            int sessions) throws SocketException, IOException {
        IServer server;
        if (type == NetworkProtocolType.UDP) {
            server = new UDPServer(port);
        } else {
            server = new TCPServer(port);
        }
        
        while (true) {
            // Receive the word
            byte[] wordData = server.receive(Constants.PACKET_SIZE);
            String word = Utility.ReadWord(wordData);

            // Constructs the response data
            WordLookup lookupInstance = new WordLookup(databaseFileName);
            byte[] response = lookupInstance.lookup(word);
            
            // Send back the response
            server.send(response);
        }
    }
}