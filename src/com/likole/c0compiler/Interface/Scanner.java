package com.likole.c0compiler.Interface;

/**
 * @author kanghao
 * @date 18-11-22 下午3:09
 */
public interface Scanner {

    /**
     * 得到一个字符
     */
    void getch();

    /**
     * 词法分析，获取一个词法符号
     */
    void getsym();

    /**
     * 匹配关键字和标识符
     */
    void matchKeywordOrIdentifier();

    /**
     * 分析数字
     */
    void matchNumber();

    /**
     * 匹配操作符，分配不同操作
     */
    void matchOperator();

}

