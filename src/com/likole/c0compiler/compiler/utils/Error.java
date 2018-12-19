package com.likole.c0compiler.compiler.utils;

import com.likole.c0compiler.Compiler;

public class Error extends Throwable {

    public static int errorCount=0;

    public static final String[] content={"未知错误","缺少;","缺少(","缺少)","应是标识符","缺少{","缺少}","应是变量","缺少赋值符号","变量未定义","数字溢出","超前使用未定义"};

    public static void print(int errorcode){
        if(errorCount>100){
            throw new IllegalStateException("错误过多，停止编译");
        }
        char[] s = new char[Compiler.scanner.column-1];
        java.util.Arrays.fill(s, ' ');
        String space = new String(s);
        System.out.println("****" + space + "!" + errorcode+" "+content[errorcode]);
        Compiler.fa1.println("****" + space + "!" + errorcode+" "+content[errorcode]);
        errorCount++;
    }



}
