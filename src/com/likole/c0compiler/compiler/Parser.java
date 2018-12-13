package com.likole.c0compiler.compiler;


import com.likole.c0compiler.compiler.Generator;
import com.likole.c0compiler.compiler.impl.Constant;
import com.likole.c0compiler.compiler.utils.Error;
import com.likole.c0compiler.compiler.utils.SymbolTable;
import com.likole.c0compiler.entity.Fct;
import com.likole.c0compiler.entity.SymSet;
import com.likole.c0compiler.entity.Symbol;
import com.likole.c0compiler.Compiler;

/**
 * Created by likole on 11/22/18.
 */
public class Parser {

    // 表示声明开始的符号集合、表示语句开始的符号集合、表示因子开始的符号集合
    // 实际上这就是声明、语句和因子的FIRST集合
    private SymSet declbegsys, statbegsys, facbegsys;

    /**
     * 当前符号，由nextsym()读入
     * @see #loadNextSymbol()
     */
    private Symbol symbol;


    /**
     * 当前作用域的堆栈帧大小，或者说数据大小（data size）
     */
    private int dx = 0;

    public Parser() {

        // 设置声明开始符号集
        declbegsys = new SymSet(Constant.symnum);
        declbegsys.set(Symbol.intsym);
        declbegsys.set(Symbol.ident);
        declbegsys.set(Symbol.voidsym);

        // 设置语句开始符号集
        statbegsys = new SymSet(Constant.symnum);
        statbegsys.set(Symbol.lbrace);
        statbegsys.set(Symbol.ident);
        statbegsys.set(Symbol.ifsym);
        statbegsys.set(Symbol.whilesym);
        statbegsys.set(Symbol.retsym);			// thanks to elu
        statbegsys.set(Symbol.scanfsym);
        statbegsys.set(Symbol.printfsym);

        // 设置因子开始符号集
        facbegsys = new SymSet(Constant.symnum);
        facbegsys.set(Symbol.ident);
        facbegsys.set(Symbol.number);
        facbegsys.set(Symbol.lparen);


    }

    
    public void test(SymSet s1, SymSet s2, int errorcode) {
        if (!s1.get(symbol)) {
            Error.print(errorcode);
            // 当检测不通过时，不停获取符号，直到它属于需要的集合或补救的集合
            while (!s1.get(symbol) && !s2.get(symbol)) {
                loadNextSymbol();
            }
        }
    }

    
    public void loadNextSymbol() {
        Compiler.scanner.getsym();
        symbol= Compiler.scanner.symbol;
    }


    
    public void prepare() {
        SymSet nxtlev = new SymSet(Constant.symnum);
        nxtlev.or(declbegsys);
        nxtlev.or(statbegsys);
        nxtlev.set(Symbol.period);
        parse();
    }
    
    public void parse() {
        dx=3;

        SymSet nxtlev = new SymSet(Constant.symnum);

        Compiler.generator.generate(Fct.JMP,0,0);

        //分析[<变量定义部分>]
        if(symbol==Symbol.intsym){
            loadNextSymbol();
            varDeclaration(0);
            while (symbol==Symbol.comma){
                loadNextSymbol();
                varDeclaration(0);
            }
            if(symbol==Symbol.semicolon){
                loadNextSymbol();
            }else {
                //漏掉了逗号或分号
                Error.print(5);
            }
        }

        //分析{<自定义函数定义部分>}
        while (symbol==Symbol.intsym||symbol==Symbol.voidsym){
            loadNextSymbol();
            if(symbol==Symbol.ident){
                Compiler.symbolTable.add(SymbolTable.Type.procedure,0,dx);
                loadNextSymbol();
            }else {
                //int或void后应为标识符
                Error.print(4);
            }

            if(symbol==Symbol.lparen){
                loadNextSymbol();
                if(symbol==Symbol.rparen){
                    loadNextSymbol();
                }else{
                    //)
                    Error.print(5);
                }
            }else {
                //(
                Error.print(5);
            }

            //<fen cheng xu>
            block();
        }

        //<主函数>
        if(symbol==Symbol.voidsym){
            if(symbol==Symbol.mainsym){
                if(symbol==Symbol.lparen){
                    loadNextSymbol();
                    if(symbol==Symbol.rparen){
                        loadNextSymbol();
                    }else{
                        //)
                        Error.print(5);
                    }
                }else {
                    //(
                    Error.print(5);
                }
            }else{
                //main
                Error.print(4);
            }
        }else{
            //void
            Error.print(4);
        }
        block();

    }

    /**
     * 变量声明
     * 分析 id
     */
     void varDeclaration(int level) {
        if(symbol==Symbol.ident){
            Compiler.symbolTable.add(SymbolTable.Type.variable,level,dx++);
            loadNextSymbol();
        }else{
            //int 应是标识符
            Error.print(4);
        }
    }

    /**
     * <分程序> := '{' [<变量定义部分>] <语句序列> '}'
     */
    public void block() {
        if(symbol!=Symbol.lbrace){
            //{
            Error.print(5);
        }

        loadNextSymbol();
        //分析[<变量定义部分>]
        if(symbol==Symbol.intsym){
            loadNextSymbol();
            varDeclaration(1);
            while (symbol==Symbol.comma){
                loadNextSymbol();
                varDeclaration(0);
            }
            if(symbol==Symbol.semicolon){
                loadNextSymbol();
            }else {
                //漏掉了逗号或分号
                Error.print(5);
            }
        }
        //分析<语句序列>
        statementSeq();

        if(symbol!=Symbol.rbrace){
            //}
            Error.print(5);
        }
    }

    /**
     * <语句序列> := <语句> {<语句>}
     */
    public void statementSeq() {
        singleStatement();
        while (statbegsys.get(symbol)||symbol==Symbol.semicolon){

        }

    }

    
    public void singleStatement() {
        switch (symbol){
            case ident:
                assignmentStatement();
                break;
            case scanfsym:
                readStatement();
                break;
            case printfsym:
                writeStatement();
                break;
                //函数调用语句
//            case :
//                parseCallStatement(fsys, lev);
//                break;
            case ifsym:
                condStatement();
                break;
            case lbrace:
                statementSeq();
                break;
            case whilesym:
                cycStatement();
                break;
            default:
//                nxtlev = new SymSet(symnum);
//                test(fsys, nxtlev, 19);
                break;
        }

    }

    
    public void condStatement() {

    }

    
    public void cycStatement() {

    }

    
    public void callStatement() {

    }

    
    public void assignmentStatement() {

    }

    
    public void retStatement() {

    }

    
    public void readStatement() {

    }

    
    public void writeStatement() {

    }

    
    public void callFunction(SymSet fsys, int lev) {

    }

    
    public void expression(SymSet fsys,int lev) {
        Symbol addop;
        SymSet nxtlev;

        // 分析[+|-]<项>
        if (symbol == Symbol.plus || symbol == Symbol.minus) {
            addop = symbol;
            loadNextSymbol();
            nxtlev = (SymSet) fsys.clone();
            nxtlev.set(Symbol.plus);
            nxtlev.set(Symbol.minus);
            term(nxtlev, lev);
            if (addop == Symbol.minus) {
                Compiler.generator.generate(Fct.OPR, 0, 1);//TODO 改指令
            }
        } else {
            nxtlev = (SymSet) fsys.clone();
            nxtlev.set(Symbol.plus);
            nxtlev.set(Symbol.minus);
            term(nxtlev, lev);
        }

        // 分析{<加法运算符><项>}
        while (symbol == Symbol.plus || symbol == Symbol.minus) {
            addop = symbol;
            loadNextSymbol();
            nxtlev = (SymSet) fsys.clone();
            nxtlev.set(Symbol.plus);
            nxtlev.set(Symbol.minus);
            term(nxtlev, lev);
            if (addop == Symbol.plus) {
                Compiler.generator.generate(Fct.OPR, 0, 2);//TODO 改指令
            } else {
                Compiler.generator.generate(Fct.OPR, 0, 3);//TODO 改指令
            }
        }
    }

    
    public void term(SymSet fsys,int lev) {
        Symbol mulop;
        SymSet nxtlev;

        // 分析<因子>
        nxtlev = (SymSet) fsys.clone();
        nxtlev.set(Symbol.times);
        nxtlev.set(Symbol.slash);
        factor(nxtlev, lev);

        // 分析{<乘法运算符><因子>}
        while (symbol == Symbol.times || symbol == Symbol.slash) {
            mulop = symbol;
            loadNextSymbol();
            factor(nxtlev, lev);
            if (mulop == Symbol.times) {
                Compiler.generator.generate(Fct.OPR, 0, 4);//TODO 改指令
            } else {
                Compiler.generator.generate(Fct.OPR, 0, 5);//TODO 该指令
            }
        }
    }

    
    public void factor(SymSet fsys,int lev) {
        SymSet nxtlev;

        test(facbegsys, fsys, 24);			// 检测因子的开始符号
        if (facbegsys.get(symbol)) {
            if (symbol == Symbol.ident) {            // 因子为变量
                SymbolTable.Item item = Compiler.symbolTable.getByName(Compiler.scanner.id);
                switch (item.getType()) {
                    case variable:			// 名字为变量
                        Compiler.generator.generate(Fct.LOD, lev - item.getLevel(), item.getAddress());
                        break;
                    case procedure:			// 名字为过程
                        Compiler.generator.generate(Fct.CAL, lev - item.getLevel(), item.getAddress());//TODO 改指令
                        break;
                }
//                loadNextSymbol();
                //后一个是(,则可能是函数调用
//                if (symbol == Symbol.lparen) {
//                    loadNextSymbol();
//                    if (symbol != Symbol.rparen) {
//                        Error.print(100);
//                        test(fsys, facbegsys, 101);
//                    }
//                    Compiler.generator.generate(Fct.CAL,0,2);//TODO 改指令
//                }else {//变量
//                    if (item!=null) {
//                        Compiler.generator.generate(Fct.LOD, lev - item.getLevel(), item.getAddress());
//                    } else {
//                        Error.print(11);					// 标识符未声明
//                    }
//                }
                loadNextSymbol();
            }else if (symbol == Symbol.number) {	// 因子为数
                int num = Compiler.scanner.num;
                if (num > Constant.amax) {
                    Error.print(31);
                    num = 0;
                }
                Compiler.generator.generate(Fct.LIT, 0, num);//TODO 改指令
                loadNextSymbol();
            } else if (symbol == Symbol.lparen) {	// 因子为表达式
                loadNextSymbol();
                nxtlev = (SymSet) fsys.clone();
                nxtlev.set(Symbol.rparen);
                expression(nxtlev, lev);
                if (symbol == Symbol.rparen) {
                    loadNextSymbol();
                } else {
                    Error.print(22);                    // 缺少右括号
                }
            }else {
                // 做补救措施
                test(fsys, facbegsys, 23);
            }
        }
    }
}
