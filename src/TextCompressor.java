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

import jdk.incubator.vector.VectorOperators;

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
        int R = 256;
        int L = 4096;
        int W = 12;

        TST tst = new TST();

        for (int i = 0; i < R; i++) {
            tst.insert("" + (char) i, i);
        }

        int code = R + 1;
        int index = 0;

        while (index < length) {
            // Get the longest coded word that matches text at index
            String prefix = tst.getLongestPrefix(s);

            // Write the prefix out
            BinaryStdOut.write(tst.lookup(prefix), W);

            // Look ahead to the next character
            int prefixLength = prefix.length();
            if (index + prefixLength < length) {
                char nextChar = s.charAt(prefixLength+index);

                // Append that character to the prefix
                String newPrefix = prefix + nextChar;

                // Associate that new prefix with the next available code (if available)
                if (code < L) {
                    tst.insert(newPrefix, code++);
                }
            }
            index += prefixLength;
        }
        BinaryStdOut.write(R,W);
        BinaryStdOut.close();
    }



    private static void expand() {
        int R = 256;
        int L = 4096;
        int W = 12;

        String[] st = new String[L];

        for (int i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }

        int prevCode = BinaryStdIn.readInt(W);
        if (prevCode == R) {
            // EOF
            return;
        }

        String prevString = st[prevCode];

        BinaryStdOut.write(prevString);


        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
