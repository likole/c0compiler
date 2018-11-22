package com.likole.c0compiler.compiler.impl;

import com.likole.c0compiler.compiler.Generator;
import com.likole.c0compiler.entity.Fct;

/**
 * @author kanghao
 * @date 18-11-22 下午5:06
 */
public class GeneratorImpl implements Generator{

    /**
     * cx虚拟机代码指针,取值范围[0,cxmax-1]
     */
    public int cx;


    @Override
    public void generate(Fct fct, int level, int param) {

    }

    @Override
    public void listcode(int start) {

    }
}
