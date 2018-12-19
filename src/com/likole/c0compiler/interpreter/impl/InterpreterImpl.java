package com.likole.c0compiler.interpreter.impl;

import com.likole.c0compiler.Compiler;
import com.likole.c0compiler.entity.Instruction;
import com.likole.c0compiler.interpreter.Interpreter;
import com.likole.c0compiler.interpreter.InterpreterListener;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Created by likole on 11/22/18.
 */
public class InterpreterImpl implements Interpreter {

    int ins_num, base_addr, stack_top;       // 指令指针(序号)，指令基址，栈顶指针
    int[] stack = new int[stacksize];        // 栈
    Instruction instruction;                            // 存放当前指令
    ArrayList<Instruction> codes;
    int flag,prevIns,position;
    InterpreterListener listener;

    public void setInterpreterListener(InterpreterListener listener){
        this.listener=listener;
    }

    @Override
    public void init() {
        codes = Compiler.generator.codes;
        System.out.println("start c0");
        stack_top = base_addr = ins_num = 0;
        stack[0] = stack[1] = stack[2] = 0;
        flag=0;
    }

    public void run(){
        while (interpret()==1) {
        }
    }

    @Override
    public int interpret() {

        if (ins_num != 0||flag==0) {
            prevIns=ins_num;
            instruction = codes.get(ins_num);         // 读当前指令
            listener.now(ins_num,instruction);
            ins_num++;
            switch (instruction.action) {
                case LIT:                // 将a的值取到栈顶
                    stack[stack_top] = instruction.param;
                    stack_top++;
                    break;
                case LOD:                // 取相对当前过程的数据基地址为a的内存的值到栈顶
                    position=(instruction.l==0)?instruction.param:base_addr+instruction.param;
                    stack[stack_top] = stack[position];
                    stack_top++;
                    break;
                case STO:
                    stack_top--;
                    position=(instruction.l==0)?instruction.param:base_addr+instruction.param;
                    stack[position] = stack[stack_top];
                    break;
                case CAL:
                    stack[stack_top] = base(instruction.l, stack, base_addr);    // 将静态作用域基地址入栈
                    stack[stack_top + 1] = base_addr;                   // 将动态作用域基地址入栈
                    stack[stack_top + 2] = ins_num;                     // 将当前指令指针入栈
                    base_addr = stack_top;                              // 改变基地址指针值为新过程的基地址
                    ins_num = instruction.param;                        // 跳转
                    break;
                case INT:            // 分配内存
                    stack_top += instruction.param;
                    break;
                case JMP:
                    ins_num = instruction.param;
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
                    try{
                        stack[stack_top - 1] = stack[stack_top - 1] / stack[stack_top];
                    }catch (ArithmeticException e){
                        listener.divideByZero();
                    }
                    break;
                case RED:
                    System.out.println("?");
                    Compiler.fa2.print("?");
                    stack[stack_top] = 0;
                    try {
                        stack[stack_top] = Integer.parseInt(listener.read()) ;
//                        stack[stack_top] = Integer.parseInt(Compiler.stdin.next());
                    } catch (Exception e) {
                        listener.readError();
                        System.out.println("readline error");
                    }
                    Compiler.fa2.println(stack[stack_top]);
                    stack_top++;
                    break;
                case WRT:
                    Compiler.fa2.print(stack[stack_top - 1]);
                    System.out.println(stack[stack_top - 1]);
                    listener.print(stack[stack_top-1]);
                    break;
                case RET:
                    stack[stack[base_addr + 1]] = stack[stack_top - 1];
                    stack_top = base_addr;
                    ins_num = stack[stack_top + 2];
                    base_addr = stack[stack_top + 1];
                    break;
            }
            flag++;
            return 1;
        }
        else {
            System.out.println("解释执行完毕");
            listener.finished();
            return 0;
        }
    }

    @Override
    public void print() {
        PrintStream stackOut= null;
        try {
            stackOut = new PrintStream("stackOut");
            stackOut.println(codes.get(prevIns).toString());
            for (int i=stack_top;i>=0;i--){
                if(i==stack_top)
                    stackOut.printf("%-4d|%-4s|\n",i,"px");
                else stackOut.printf("%-4d|%-4d|\n",i,stack[i]);
                if(i==0) stackOut.println("    ------");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
