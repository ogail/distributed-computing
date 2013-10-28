/*
 * Word loopup code that looks for correctness and spelling of words.
 */
package elogeel.utilities;

import elogeel.networking.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * @author Abdelrahman Elogeel
 */
public class WordLookup {
    /*
     * The absolute path to the database.
     */
    String databaseFileName;
    
    /*
     * The class constructor.
     */
    public WordLookup(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }
    
    /*
     * Looks for a word.
     */
    public byte[] lookup(String word) throws IOException {
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

        return Utility.ToByteArray(responseList);
    }
}
