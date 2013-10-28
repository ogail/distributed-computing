/*
 * The main spelling client class.
 */
package elogeel.speller;

import elogeel.networking.Client;
import elogeel.networking.Constants;
import elogeel.networking.NetworkProtocolType;
import elogeel.networking.TCPClient;
import elogeel.networking.UDPClient;
import elogeel.utilities.Log;
import elogeel.utilities.Utility;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Abdelrahman Elogeel
 */
public class SpellingClient {
    
    public static void SpellChecker(
            NetworkProtocolType type,
            InetAddress ipAddress,
            int port,
            String[] words) throws SocketException, IOException
    {
        Client client;
        
        for (int i = 0; i < words.length; i++) {
            if (type == NetworkProtocolType.UDP) {
                client = new UDPClient(port, ipAddress);
            }
            else {
                client = new TCPClient(port, ipAddress);
            }
            String word = new String(words[i].getBytes(), Charset.forName("US-ASCII"));
            Log.out("Client is looking for word: " + word);
            client.send(word.getBytes());
            byte[] data = null;
            try {
                data = client.receive(Constants.PACKET_SIZE);
            } catch (IOException ex) {
                Log.err("received unsolicited response to query for word: " + word);
                continue;
            }
            
            if (Utility.CanReadWord(data, word.length())) {
                byte[] closestData = Arrays.copyOfRange(
                        data,
                        // Add extra one to eat the null-terminate for the string
                        word.length() + 1,
                        data.length - word.length());
                if (Utility.CanReadWords(closestData)) {
                    String[] closest = Utility.ReadWords(closestData);
                    String output = "Server response: " + word;
                    for (int j = 0; j < closest.length; j++) {
                        output += " " + closest[j];
                    }
                    Log.out(output);
                    
                } else {
                    Log.err("received unsolicited response to query for word: " + word);
                }
            }
            else {
                Log.err("received unsolicited response to query for word: " + word);
            }
            
            client.close();
        }
    }
}
