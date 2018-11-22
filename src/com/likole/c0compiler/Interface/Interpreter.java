package com.likole.c0compiler.Interface;

import com.likole.c0compiler.Fct;

/**
 * @author kanghao
 * @date 18-11-22 下午3:21
 */
public interface Interpreter {

    /**
     * 生成虚拟机代码
     * @param fct 虚拟机代码指令
     * @param level 引用层与声明层的层差
     * @param param 指令参数
     */
    void generate(Fct fct, int level, int param);

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
