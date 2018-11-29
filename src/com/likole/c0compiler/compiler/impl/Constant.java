package com.likole.c0compiler.compiler.impl;

import com.likole.c0compiler.compiler.Parser;
import com.likole.c0compiler.compiler.Scanner;
import com.likole.c0compiler.compiler.impl.GeneratorImpl;
import com.likole.c0compiler.compiler.utils.SymbolTable;
import com.likole.c0compiler.entity.Symbol;
import com.likole.c0compiler.interpreter.Interpreter;
import com.likole.c0compiler.interpreter.impl.InterpreterImpl;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * @author kanghao
 * @date 18-11-22 下午4:55
 */
public class Constant {
    // 编译程序的常数
    public static final int al = 10;			// 符号的最大长度
    public static final int amax = 2047;		// 最大允许的数值
    public static final int cxmax = 500;		// 最多的虚拟机代码数
    public static final int levmax = 3;			// 最大允许过程嵌套声明层数 [0, levmax]
    public static final int nmax = 14;			// number的最大位数
    public static final int norw = 32;			// 关键字个数
    public static final int txmax = 100;		// 名字表容量
    public static final int symnum = Symbol.values().length;
    // 一些全局变量，其他关键的变量分布如下：
    // cx, code : src.org.zkh.c0.Interpreter
    // dx : src.org.zkh.c0.Parser
    // tx, table : src.org.zkh.c0.Table
}
