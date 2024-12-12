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

import java.io.*;


/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, Lucas Ying
 */
public class TextCompressor {


    private static void compress() {

        String s = BinaryStdIn.readString();
        int length = s.length();
        // Number of ASCII characters
        int R = 256;
        // Maximum number of codewords for 12 bit codes
        int L = 4096;
        // Codeword size of 12 bits
        int W = 12;

        TST tst = new TST();

        // Initialize TST with the ASCII characters
        for (int i = 0; i < R; i++) {
            tst.insert("" + (char) i, i);
        }

        // Start assigning codes after the ASCII codes
        int code = R + 1;
        int index = 0;

        while (index < length) {
            // Get the longest prefix in the TST starting at index
            String prefix = tst.getLongestPrefix(s.substring(index));

            // Write the code for the prefix to the output
            BinaryStdOut.write(tst.lookup(prefix), W);

            // Look ahead to the next character after the prefix
            int prefixLength = prefix.length();
            if (index + prefixLength < length) {
                char nextChar = s.charAt(prefixLength+index);

                // Append that character to the prefix to create the new prefix
                String newPrefix = prefix + nextChar;

                // Associate that new prefix with the next available code (if available) and add it in the TST
                if (code < L) {
                    tst.insert(newPrefix, code++);
                }
            }
            // Advance the index by the length of the prefix
            index += prefixLength;
        }
        // Write end of file marker to indicate the end of the compressed data
        BinaryStdOut.write(R,W);
        BinaryStdOut.close();
    }



    private static void expand() {
        // Number of ASCII characters
        int R = 256;
        // Maximum number of codewords for 12 bit codes
        int L = 4096;
        // Codeword size of 12 bits
        int W = 12;

        // Array for the codes
        String[] st = new String[L];

        // Initialize the table with the ASCII characters
        for (int i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }

        int code = R + 1;

        // Read the first codeword
        int prevCode = BinaryStdIn.readInt(W);
        if (prevCode == R) {
            // EOF
            return;
        }
        // Retrieve corresponding string
        String prevString = st[prevCode];

        // Write the first string to the output
        BinaryStdOut.write(prevString);

        while(true) {
            // Read the next codeword
            int currCode = BinaryStdIn.readInt(W);
            if (currCode == R) {
                // EOF
                break;
            }

            String currString;
            // Retrieve the string for the current codeword
            if (currCode < code) {
                currString = st[currCode];
            }
            else if (currCode == code) {
                // Handle lookahead case
                currString = prevString + prevString.charAt(0);
            }
            else {
                // Throw exception if the code does not exist within the array
                throw new IllegalArgumentException("Invalid compressed codeword");
            }
            // Write the current string to the output
            BinaryStdOut.write(currString);

            // Add the new sequence to the array of codes
            if (code < L) {
                st[code++] = prevString + currString.charAt(0);
            }

            // Update prevString for the next iteration
            prevString = currString;
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
