package com.likole.c0compiler.entity;

/**
 * @author kanghao
 * @date 18-11-22 下午3:25
 */
public class Instruction {

    /**
     * 虚拟机代码指令
     */
    public Fct f;

    /**
     * 引用层与声明层的层次差
     */
    public int l;

    /**
     * 指令参数
     */
    public int a;

    public Fct getF() {
        return f;
    }

    public void setF(Fct f) {
        this.f = f;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
