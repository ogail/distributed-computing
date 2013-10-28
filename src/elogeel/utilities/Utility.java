/*
 * General utility class.
 */
package elogeel.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 *
 * @author Abdelrahman Elogeel
 */
public class Utility {
    public static boolean CanReadWord(byte[] data, int length) {
        if (data.length <= length) {
            return false;
        }
        String word = "";
        try {
            word = ReadWord(data);
        } catch(Exception e) {
            return false;
        }
        
        if (word.length() == length) {
            return true;
        } else {
            return false;
        }
    }
    
    @SuppressWarnings("empty-statement")
    public static String ReadWord(byte[] data, int startIndex) {
        int i;
        for (i = startIndex; data[i] != 0 ; i++);
        
        return new String(Arrays.copyOfRange(data, startIndex, i), Charset.forName("US-ASCII"));
    }
    
    @SuppressWarnings("empty-statement")
    public static String ReadWord(byte[] data) {
        return ReadWord(data, 0);
    }

    public static boolean CanReadWords(byte[] wordsData) {
        try { ReadWords(wordsData); return true; }
        catch (Exception e) { return false; }
    }

    public static String[] ReadWords(byte[] wordsData) {
        byte wordsCount = wordsData[0];
        String[] words = new String[wordsCount];

        if (wordsCount > 0) {
            int i;
            int nextWordIndex = 1;
            for (i = 0; i < wordsCount; i++) {
                words[i] = ReadWord(wordsData, nextWordIndex);
                
                // Add extra one to eat the null-terminate for the string
                nextWordIndex += words[i].length() + 1;
            }
        }
        
        return words;
    }
    
    public static boolean AddByteRange(List<Byte> list, byte[] array) {
        for(byte B : array)
        {
            list.add(Byte.valueOf(B));
        }
        
        return true;
    }
    
    public static void AddCString(List<Byte> list, byte[] array) {
        AddByteRange(list, array);
        list.add(Byte.valueOf((byte)0));
    }
    
    public static byte[] ToByteArray(List<Byte> list) {
        byte[] array = new byte[list.size()];
        
        for (int i = 0; i < list.size(); i++) {
            array[i] = (byte)list.get(i);
        }
        
        return array;
    }
    
    public static byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
          byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
    
    public static String Flatten(SortedSet<String> set, String separator) {
        String result = "";
        for (final Iterator it = set.iterator(); it.hasNext();) {
            result += (String)it.next() + separator;
        }
        
        return result;
    }
}
