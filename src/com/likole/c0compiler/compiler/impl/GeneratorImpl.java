package com.likole.c0compiler.compiler.impl;

import com.likole.c0compiler.Compiler;
import com.likole.c0compiler.compiler.Generator;
import com.likole.c0compiler.entity.Fct;
import com.likole.c0compiler.entity.Instruction;

import java.util.ArrayList;

/**
 * @author kanghao
 * @date 18-11-22 下午5:06
 */
public class GeneratorImpl implements Generator{

    /**
     * cx虚拟机代码指针,取值范围[0,cxmax-1]
     */
    public int cx;

    public ArrayList<Instruction> codes=new ArrayList<>();

    @Override
    public void generate(Fct fct, int level, int param) {
        Instruction instruction=new Instruction();
        instruction.action =fct;
        instruction.l=level;
        instruction.param =param;
        codes.add(instruction);
        cx++;
    }

    public int getSize(){
        return cx;
    }

    public Instruction getLast(){
        return codes.get(cx-1);
    }

    @Override
    public void listcode(int start) {
        Compiler.fa.println("-\t-\t-\t-\t");
        for (int i=0;i<codes.size();i++){
            Compiler.fa.println(i+"\t"+codes.get(i).toString());
        }
    }
}
