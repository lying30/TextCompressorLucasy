/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

import java.util.HashMap;
import java.util.Map;
import java.io.*;


/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Lucas Ying
 */
public class TextCompressor {

    private static Map<String, Integer> wordToCode = new HashMap<>();
    private static Map<Integer, String> codeToWord = new HashMap<>();

    private static void commonWords(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int code = 0;

        while ((line = reader.readLine()) != null){
            wordToCode.put(line, code);
            codeToWord.put(code, line);
            code++;
        }
        reader.close();
    }

    private static void compress() {

        String s = BinaryStdIn.readString();
        int length = s.length();



        // read in the string
        // utilize 10 bit codes that represent 1000 most common words in english dictionary
        // to pull from when seen in the text. Map these to 10 bit codes

        // when seen in the read file write out the 10 bit code for that word (include the space after the coded word)
        // if the word is not seen switch to writing out 8 bit chars through the ascii value of that char
        // i also need to account for metadata to let the expanded file understand when to expand it into a word or a individual link of chars




        BinaryStdOut.close();
    }

    private static void compressWordOrSwitch(String word) {

    }



    private static void expand() {

        while (!BinaryStdIn.isEmpty()) {
            boolean isCommonWord = BinaryStdIn.readBoolean();

            if (isCommonWord) {
                int code = BinaryStdIn. readInt(10);
                BinaryStdOut.write(codeToWord.get(code));
            } else {
                char c = BinaryStdIn.readChar(8);
                BinaryStdOut.write(c);
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
