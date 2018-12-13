package com.likole.c0compiler.entity;

/**
 * @author kanghao
 * @date 18-11-22 下午3:25
 */
public class Instruction {

    /**
     * 虚拟机代码指令
     */
    public Fct action;

    /**
     * 引用层与声明层的层次差
     */
    public int l;

    /**
     * 指令参数
     */
    public int param;

    public Fct getAction() {
        return action;
    }

    public void setAction(Fct action) {
        this.action = action;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }
}
