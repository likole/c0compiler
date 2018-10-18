package com.likole.c0compiler;

import java.io.BufferedReader;
import java.util.Arrays;

public class Scanner {

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

    public Scanner(BufferedReader in) {
        this.in = in;
        single_num=new Symbol[256];
        Arrays.fill(single_num,Symbol.nul);
        single_num['+'] = Symbol.plus;
        single_num['-'] = Symbol.minus;
        single_num['*'] = Symbol.times;
        single_num['/'] = Symbol.slash;
        single_num['('] = Symbol.lparen;
        single_num[')'] = Symbol.rparen;
        single_num['='] = Symbol.eql;
        single_num[','] = Symbol.comma;
        single_num['.'] = Symbol.period;
        single_num['#'] = Symbol.neq;
        single_num[';'] = Symbol.semicolon;


        word=new String[]{"break","case","const","continue","default","do",
                "else","for","if","int","main","switch","void","while"};

        word_num=new Symbol[15];


    }


}
