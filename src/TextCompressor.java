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
        int R = 256;
        int L = 4096;
        int W = 12;

        TST tst = new TST();

        for (int i = 0; i < R; i++) {
            tst.insert("" + (char) i, i);
        }

        int code = R + 1;

        String prefix = "";




        // find the longest string s in the symbol table that is a prefix of the unscanned input



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
