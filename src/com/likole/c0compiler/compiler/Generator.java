package com.likole.c0compiler.compiler;

import com.likole.c0compiler.entity.Fct;

/**
 * @author kanghao
 * @date 18-11-22 下午3:38
 */
public interface Generator {

    /**
     * 生成虚拟机代码
     * @param fct 虚拟机代码指令
     * @param level 引用层与声明层的层差
     * @param param 指令参数
     */
    void generate(Fct fct, int level, int param);
}
