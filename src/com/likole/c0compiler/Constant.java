package com.likole.c0compiler;

import com.likole.c0compiler.compiler.Parser;
import com.likole.c0compiler.compiler.Scanner;
import com.likole.c0compiler.compiler.impl.GeneratorImpl;
import com.likole.c0compiler.compiler.utils.SymbolTable;
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

    // 一些全局变量，其他关键的变量分布如下：
    // cx, code : src.org.zkh.c0.Interpreter
    // dx : src.org.zkh.c0.Parser
    // tx, table : src.org.zkh.c0.Table
    public static PrintStream fa;				// 输出虚拟机代码
    public static PrintStream fa1;				// 输出源文件及其各行对应的首地址
    public static PrintStream fa2;				// 输出结果
    public static PrintStream fas;				// 输出名字表
    public static boolean listswitch;			// 显示虚拟机代码与否
    public static boolean tableswitch;			// 显示名字表与否

    // 一个典型的编译器的组成部分
    public static Scanner lex;					// 词法分析器
    public static Parser parser;				// 语法分析器
    public static GeneratorImpl generator;      // 代码生成器
    public static InterpreterImpl interp;		// 类P-Code解释器（及目标代码生成工具）
    public static SymbolTable table;			// 名字表

    // 为避免多次创建BufferedReader，我们使用全局统一的Reader
    public static BufferedReader stdin;			// 标准输入
}
