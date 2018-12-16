package com.likole.c0compiler.interpreter.impl;

import com.likole.c0compiler.Compiler;
import com.likole.c0compiler.entity.Instruction;
import com.likole.c0compiler.interpreter.Interpreter;

import java.util.ArrayList;

/**
 * Created by likole on 11/22/18.
 */
public class InterpreterImpl implements Interpreter {


    @Override
    public void interpret() {
        int ins_num, base_addr, stack_top;                        // 指令指针(序号)，指令基址，栈顶指针
        Instruction instruction;                            // 存放当前指令
        ArrayList<Instruction> codes = Compiler.generator.codes;
        int[] stack = new int[stacksize];        // 栈

        System.out.println("start c0");
        stack_top = base_addr = ins_num = 0;
        stack[0] = stack[1] = stack[2] = 0;
        do {
            instruction = codes.get(ins_num);         // 读当前指令
            ins_num++;
            switch (instruction.action) {
                case LIT:                // 将a的值取到栈顶
                    stack[stack_top] = instruction.param;
                    stack_top++;
                    break;
                case LOD:                // 取相对当前过程的数据基地址为a的内存的值到栈顶
                    stack[stack_top] = stack[base(instruction.l, stack, base_addr) + instruction.param];
                    stack_top++;
                    break;
                case STO:
                    stack_top--;
                    stack[base(instruction.l, stack, base_addr) + instruction.param] = stack[stack_top];
                    break;
                case CAL:
                    stack[stack_top] = base(instruction.l, stack, base_addr);    // 将静态作用域基地址入栈
                    stack[stack_top+1] = base_addr;                   // 将动态作用域基地址入栈
                    stack[stack_top + 2] = ins_num;                     // 将当前指令指针入栈
                    base_addr = stack_top;                              // 改变基地址指针值为新过程的基地址
                    ins_num = instruction.param;                        // 跳转
                    break;
                case INT:            // 分配内存
                    stack_top += instruction.param;
                    break;
                case JMP:
                    ins_num = instruction.param;
                    //TODO 要将main函数地址传入吗?
                    break;
                case JPC:                // 条件跳转（当栈顶为0的时候跳转）
                    stack_top--;
                    if (stack[stack_top] == 0)
                        ins_num = instruction.param;
                    break;
                case ADD:
                    stack_top--;
                    stack[stack_top - 1] = stack[stack_top - 1] + stack[stack_top];
                    break;
                case SUB:
                    stack_top--;
                    stack[stack_top - 1] = stack[stack_top - 1] - stack[stack_top];
                    break;
                case MUL:
                    stack_top--;
                    stack[stack_top - 1] = stack[stack_top - 1] * stack[stack_top];
                    break;
                case DIV:
                    stack_top--;
                    stack[stack_top - 1] = stack[stack_top - 1] / stack[stack_top];
                    break;
                case RED:
                    System.out.print("?");
                    Compiler.fa2.print("?");
                    stack[stack_top] = 0;
                    try {
                        stack[stack_top] = Integer.parseInt(Compiler.stdin.readLine());
                    } catch (Exception e) {
                    }
                    Compiler.fa2.println(stack[stack_top]);
                    stack_top++;
                    break;
                case WRT:
                    Compiler.fa2.print(stack[--stack_top]);
                    System.out.println(stack[stack_top]);
                    break;
                case RET:
                    stack[stack[base_addr+1]]=stack[stack_top-1];
                    stack_top = base_addr;
                    ins_num = stack[stack_top+2];
                    base_addr = stack[stack_top+1];
                    break;
            }
        } while (ins_num != 0);
        System.out.println("解释执行完毕");
    }

    @Override
    public void print() {

    }

    @Override
    public int base(int l, int[] s, int b) {
        int tmp = b;
        while (l > 0) {
            tmp = s[tmp];
            l--;
        }
        return tmp;
    }
}
