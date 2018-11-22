package com.likole.c0compiler.compiler;

/**
 * @author kanghao
 * @date 18-11-22 下午2:41
 */
public interface Parser {

    /**
     * 启动语法分析
     */
    void prepare();

    /**
     * 语法分析部分
     */
    void parse();

    /**
     * 变量声明部分
     */
    void varDeclaration();

    /**
     * 自定义函数定义部分
     */
    void funcDeclaration();

    /**
     * 分程序部分（void main（）后面的部分）
     */
    void block();
    /**
     * 语句序列分析
     */
    void statementSeq();
    /**
     * 语句分析
     */
    void singleStatement();

    /**
     * 条件语句分析
     */
    void condStatement();

    /**
     * 循环语句分析
     */
    void cycStatement();

    /**
     * 函数调用语句
     */
    void callStatement();

    /**
     * 赋值语句
     */
    void assignmentStatement();

    /**
     * 返回语句
     */
    void retStatement();

    /**
     * 读语句
     */
    void readStatement();

    /**
     * 写语句
     */
    void writeStatement();

    /**
     * 函数调用
     */
    void callFunction();

    /**
     * 表达式
     */
    void expression();

    /**
     * 项分析
     */
    void term();

    /**
     * 因子
     */
    void factor();
}
