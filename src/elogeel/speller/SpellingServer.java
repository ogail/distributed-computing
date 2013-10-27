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
import elogeel.utilities.WordList;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * @author Abdelrahman Elogeel
 */
public class SpellingServer {
    
    public static void SpellChecker(
            NetworkProtocolType type,
            int port,
            String databaseFileName,
            int sessions) throws SocketException, IOException
    {
        IServer server;
        if (type == NetworkProtocolType.UDP) {
            server = new UDPServer(port);
        } else {
            server = new TCPServer(port);
        }
        
        while (true) {
            byte[] wordData = server.receive(Constants.PACKET_SIZE);
            String word = Utility.ReadWord(wordData);
            WordList wordList = new WordList(databaseFileName);
            ArrayList<Byte> responseList = new ArrayList<>();
            Utility.AddCString(responseList, word.getBytes());
            
            if (wordList.isInList(word)) {
                // Add two null bytes indicating success
                responseList.add(Byte.valueOf((byte)0));
                responseList.add(Byte.valueOf((byte)0));
            } else {
                // Find closest words
                SortedSet<String> words = wordList.getCloseWords(word);
                // Add extra two one for the null.
                int wordsCountIndex = word.getBytes().length + 1;
                byte wordsCount = 0;
                for (final Iterator it = words.iterator(); it.hasNext(); wordsCount++) {
                    byte[] closeWordData = ((String) it.next()).getBytes();
                    int newListSize = responseList.size() + closeWordData.length + 1;

                    if (newListSize >= Constants.PACKET_SIZE || 
                        wordsCount > 255) {
                        break;
                    }
                    
                    Utility.AddCString(responseList, closeWordData);
                }
                responseList.add(wordsCountIndex, wordsCount);
            }
            
            server.send(Utility.ToByteArray(responseList));
        }
    }
}