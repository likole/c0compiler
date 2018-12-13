package com.likole.c0compiler.compiler.utils;

import com.likole.c0compiler.Compiler;

public class Error extends Throwable {

    public static int errorCount=0;

    public static void print(int errorcode){
        char[] s = new char[Compiler.scanner.column-1];
        java.util.Arrays.fill(s, ' ');
        String space = new String(s);
        System.out.println("****" + space + "!" + errorcode);
        Compiler.fa1.println("****" + space + "!" + errorcode);
        errorCount++;
    }
}
