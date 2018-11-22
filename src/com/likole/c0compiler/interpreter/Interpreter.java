package com.likole.c0compiler.interpreter;

/**
 * @author kanghao
 * @date 18-11-22 下午3:21
 */
public interface Interpreter {

    /**
     * 输出代码清单
     * @param start 开始输出的位置
     */
    void listCode(int start);


    /**
     * 解释程序
     */
    void interpret();

    /**
     * 通过给定的层次差来获得该层的堆栈帧基地址
     * @param l 目标层次与当前层次的层次差
     * @param s 运行栈
     * @param b 当前层堆栈帧基地址
     * @return 目标层次的堆栈帧基地址
     */
    int base(int l, int[] s, int b);
}
