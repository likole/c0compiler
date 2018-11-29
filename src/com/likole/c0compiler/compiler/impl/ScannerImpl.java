package com.likole.c0compiler.compiler.impl;

import com.likole.c0compiler.compiler.Scanner;
import com.likole.c0compiler.compiler.utils.Error;
import com.likole.c0compiler.entity.Symbol;
import com.likole.c0compiler.Compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class ScannerImpl implements Scanner{

    /**
     * 当前字符
     */
    private char ch = ' ';

    /**
     * 当前行内容
     */
    private String content=null;

    /**
     * 当前行的长度
     */
    public int length = 0;

    /**
     * 当前行所在的位置
     */
    public int column = 0;

    /**
     * 当前读入的符号
     */
    public Symbol symbol;

    /**
     * 保留字列表（注意保留字的存放顺序）
     */
    private String[] word;

    /**
     * 保留字对应的符号值
     */
    private Symbol[] word_num;

    /**
     * 单字符的符号值
     */
    private Symbol[] single_num;

    // 输入流
    private BufferedReader in;

    /**
     * 标识符名字（如果当前符号是标识符的话）
//     * @see Parser
//     * @see Table#enter
     */
    public String id;

    /**
     * 数值大小（如果当前符号是数字的话）
//     * @see Parser
//     * @see Table#enter
     */
    public int num;

    public ScannerImpl(BufferedReader in) {
        this.in = in;


        single_num=new Symbol[256];
        Arrays.fill(single_num,Symbol.nul);
        single_num['+'] = Symbol.plus;
        single_num['-'] = Symbol.minus;
        single_num['*'] = Symbol.times;
        single_num['/'] = Symbol.slash;
        single_num['('] = Symbol.lparen;
        single_num[')'] = Symbol.rparen;
        single_num['='] = Symbol.becomes;
        single_num[','] = Symbol.comma;
        single_num['.'] = Symbol.period;
        single_num['#'] = Symbol.neq;
        single_num[';'] = Symbol.semicolon;
        single_num['{'] = Symbol.lbrace;
        single_num['}'] = Symbol.rbrace;


        word=new String[]{"break","case","const","continue","default","do",
                "else","for","if","int","main","switch","void","while"};

        word_num=new Symbol[15];
        word_num[0]=Symbol.breaksym;
        word_num[1]=Symbol.constsym;
        word_num[2]=Symbol.continuesym;
        word_num[3]=Symbol.defaultsym;
        word_num[4]=Symbol.dosym;
        word_num[5]=Symbol.elsesym;
        word_num[6]=Symbol.forsym;
        word_num[7]=Symbol.ifsym;
        word_num[8]=Symbol.intsym;
        word_num[9]=Symbol.mainsym;
        word_num[10]=Symbol.printfsym;
        word_num[11]=Symbol.retsym;
        word_num[12]=Symbol.scanfsym;
        word_num[13]=Symbol.voidsym;
        word_num[14]=Symbol.whilesym;
    }


    /**
     * 获取单个字符，
     */
    @Override
    public void getch() {
        String line = "";
        try {
            if (column == length) {
                while (line.equals(""))
                    line = in.readLine() + "\n";
                length = line.length();
                column = 0;
                content = line;
                System.out.println(Compiler.generator.cx + " " + line);
                Compiler.fa1.println(Compiler.generator.cx + " " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ch = content.charAt(column);
        column ++;
    }

    @Override
    public void getsym() {
        // Wirth 的 PL/0 编译器使用一系列的if...else...来处理
        // 但是你的助教认为下面的写法能够更加清楚地看出这个函数的处理逻辑
        while (Character.isWhitespace(ch))		// 跳过所有空白字符
            getch();
        if (ch >= 'a' && ch <= 'z' || ch =='_'|| ch >= 'A' && ch <= 'Z') {
            // 关键字或者一般标识符
            matchKeywordOrIdentifier();
        } else if (ch >= '0' && ch <= '9') {
            // 数字
            matchNumber();
        } else {
            // 操作符
            matchOperator();
        }
    }

    @Override
    public void matchKeywordOrIdentifier() {
        int i;
        StringBuilder sb = new StringBuilder(Constant.al);
        // 首先把整个单词读出来
        do {
            sb.append(ch);
            getch();
        } while (ch>='A' && ch<='Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9' || ch=='_');
        id = sb.toString();

        // 然后搜索是不是保留字（请注意使用的是什么搜索方法）
        i = java.util.Arrays.binarySearch(word, id);

        // 最后形成符号信息
        if (i < 0) {
            // 一般标识符
            symbol = Symbol.ident;
        } else {
            // 关键字
            symbol = word_num[i];
        }
    }

    @Override
    public void matchNumber() {
        int k = 0;
        symbol = Symbol.number;
        num = 0;
        // 获取数字的值
        do {
            num = 10*num + Character.digit(ch, 10);
            k++;
            getch();
        } while (ch>='0' && ch<='9');
        k--;
        if (k > Constant.nmax)
            Error.print(30);
    }

    @Override
    public void matchOperator() {
        // 请注意这里的写法跟Wirth的有点不同
        switch (ch) {
            case '=':		// 赋值符号
                getch();
                if (ch == '=') {
                    symbol = Symbol.eql;
                    getch();
                } else {
                    symbol = Symbol.becomes;
                }
                break;
            case '<':		// 小于或者小于等于
                getch();
                if (ch == '='){
                    symbol = Symbol.leq;
                    getch();
                } else {
                    symbol = Symbol.lss;
                }
                break;
            case '>':		// 大于或者大于等于
                getch();
                if (ch == '=') {
                    symbol = Symbol.geq;
                    getch();
                } else {
                    symbol = Symbol.gtr;
                }
                break;
            default:		// 其他为单字符操作符（如果符号非法则返回nil）
                symbol = single_num[ch];
                if (symbol != Symbol.period)
                    getch();
                break;
        }
    }
}
