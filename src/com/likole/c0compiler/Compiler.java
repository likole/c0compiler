package com.likole.c0compiler;

import com.likole.c0compiler.compiler.Scanner;
import com.likole.c0compiler.compiler.impl.GeneratorImpl;
import com.likole.c0compiler.compiler.Parser;
import com.likole.c0compiler.compiler.impl.ScannerImpl;
import com.likole.c0compiler.compiler.utils.Error;
import com.likole.c0compiler.compiler.utils.SymbolTable;
import com.likole.c0compiler.interpreter.impl.InterpreterImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.StringReader;


/**
 * @author likole
 */
public class Compiler {


    public static SymbolTable symbolTable;
    public static InterpreterImpl interpreter;
    public static ScannerImpl scanner;
    public static Parser parser;
    public static GeneratorImpl generator;
    public static String cur_func="NULL";

    //todo:rename when finished
    public static PrintStream fa;				// 输出虚拟机代码
    public static PrintStream fa1;				// 输出源文件及其各行对应的首地址
    public static PrintStream fa2;				// 输出结果
    public static PrintStream fas;				// 输出名字表
    public static java.util.Scanner stdin=new java.util.Scanner(System.in);         // 命令行输入
    public static boolean listswitch;			// 显示虚拟机代码与否
    public static boolean tableswitch;			// 显示名字表与否

    public Compiler(String code,boolean showObjectCode,boolean showSymbolTable) {
        symbolTable = new SymbolTable();
        interpreter = new InterpreterImpl();
        scanner = new ScannerImpl(new BufferedReader(new StringReader(code)));
        parser = new Parser();
        generator=new GeneratorImpl();
        Compiler.listswitch=showObjectCode;
        Compiler.tableswitch=showSymbolTable;
    }

    int compile() throws FileNotFoundException {
        Error.errorCount=0;
        fa=new PrintStream("objectCode");
        fa1=new PrintStream("sourceCode");
        fa2=new PrintStream("result");
        fas=new PrintStream("symbolTable");
        try {
            parser.loadNextSymbol();
            parser.prepare();
            generator.listcode(0);
            symbolTable.listTable();
            interpreter.interpret();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return Error.errorCount;
    }

}
