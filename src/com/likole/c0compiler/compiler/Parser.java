package com.likole.c0compiler.compiler;


import com.likole.c0compiler.Compiler;
import com.likole.c0compiler.compiler.impl.Constant;
import com.likole.c0compiler.compiler.utils.Error;
import com.likole.c0compiler.compiler.utils.SymbolTable;
import com.likole.c0compiler.entity.Fct;
import com.likole.c0compiler.entity.Instruction;
import com.likole.c0compiler.entity.SymSet;
import com.likole.c0compiler.entity.Symbol;

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
        parse(nxtlev);
    }

    /**
     * <程序> := [<变量定义部分>] {<自定义函数定义部分>} <主函数>
     */
    public void parse(SymSet fsys) {
        dx = 3;

        SymSet nxtlev = new SymSet(Constant.symnum);

        Compiler.generator.generate(Fct.JMP, 0, 0);

        //分析[<变量定义部分>]{<自定义函数定义部分>}
        if (symbol == Symbol.intsym) {
            loadNextSymbol();
            String temp=Compiler.scanner.id;
            if(symbol==Symbol.ident){
                loadNextSymbol();
                switch (symbol){
                    case comma:
                        //[<变量定义部分>]
                        Compiler.scanner.id=temp;
                        Compiler.symbolTable.add(SymbolTable.Type.variable,0,dx++,Compiler.cur_func);
                        while (symbol == Symbol.comma) {
                            loadNextSymbol();
                            varDeclaration(0);
                        }
                        if(symbol==Symbol.semicolon){
                            loadNextSymbol();
                            Compiler.generator.generate(Fct.INT,0,0);
//                            code[0].param = dx;
                        }else{
                            //漏掉了分号
                            Error.print(5);
                        }
                        break;
                    case semicolon:
                        Compiler.scanner.id=temp;
                        Compiler.symbolTable.add(SymbolTable.Type.variable,0,dx++);
                        loadNextSymbol();
                        Compiler.generator.generate(Fct.INT,0,0);
//                            code[0].param = dx;
                        break;
                    case lparen:
                        //todo:function index
                        loadNextSymbol();
                        if(symbol==Symbol.rparen){
//                            returnType = intsymbol;
                            Compiler.symbolTable.add(SymbolTable.Type.procedure,0,Compiler.generator.cx-1);
                            //记录当前分析函数
                            Compiler.cur_func=Compiler.scanner.id;
                            loadNextSymbol();
                            //分程序
                            block(fsys,1);
                        }else {
                            //漏掉了）
                            Error.print(4);
                        }
                        break;
                    default:
                        //未知错误
                        Error.print(0);
                        break;
                }
            }
        }


        //xxx
        //<主函数>
        if (symbol == Symbol.voidsym)
        {
            loadNextSymbol();
            if (symbol == Symbol.mainsym) {
                if (symbol == Symbol.lparen) {
                    loadNextSymbol();
                    if (symbol == Symbol.rparen) {
                        loadNextSymbol();
                    } else {
                        //)
                        Error.print(5);
                    }
                } else {
                    //(
                    Error.print(5);
                }
            } else {
                //main
                Error.print(4);
            }
        } else

        {
            //void
            Error.print(4);
        }

        block(fsys,1);

    }

    /**
     * 变量声明
     * 分析 id
     */
    void varDeclaration(int level) {
        if (symbol == Symbol.ident) {
            Compiler.symbolTable.add(SymbolTable.Type.variable, level, dx++);
            loadNextSymbol();
        } else {
            //int 应是标识符
            Error.print(4);
        }
    }

    /**
     * <分程序> := '{' [<变量定义部分>] <语句序列> '}'
     */
    public void block(SymSet fsys, int lev) {
        if (symbol != Symbol.lbrace) {
            //{
            Error.print(5);
        }
        SymbolTable.Item previousItem= Compiler.symbolTable.getLast();
        Instruction previousInstruction=Compiler.generator.getLast();
        int cx0=Compiler.generator.cx;

        previousItem.setAddress(Compiler.generator.cx);
        Compiler.generator.generate(Fct.INT,0,0);
        dx=3;
        loadNextSymbol();
        //分析[<变量定义部分>]
        if (symbol == Symbol.intsym) {
            loadNextSymbol();
            varDeclaration(1);
            while (symbol == Symbol.comma) {
                loadNextSymbol();
                varDeclaration(0);
            }
            if (symbol == Symbol.semicolon) {
                loadNextSymbol();
            } else {
                //漏掉了逗号或分号
                Error.print(5);
            }
        }
        //分析<语句序列>
        statementSeq(fsys,lev);
        if (symbol != Symbol.rbrace) {
            //}
            Error.print(5);
        }
        previousItem.setSize(dx);
        previousInstruction.setParam(dx);
    }

    /**
     * <语句序列> := <语句> {<语句>}
     */
    public void statementSeq(SymSet fsys, int lev) {
        singleStatement(fsys,lev);
        while (statbegsys.get(symbol)) {
            singleStatement(fsys,lev);
        }

    }
    
    public void singleStatement(SymSet fsys, int lev) {
        switch (symbol){
            case ident:
                assignmentStatement(fsys,lev);
                break;
            case scanfsym:
                readStatement(fsys,lev);
                break;
            case printfsym:
                writeStatement(fsys,lev);
                break;
                //函数调用语句
//            case :
//                parseCallStatement(fsys, lev);
//                break;
            case ifsym:
                condStatement(fsys,lev);
                break;
            case lbrace:
                statementSeq(fsys,lev);
                break;
            case whilesym:
                cycStatement(fsys, lev);
                break;
            default:
//                nxtlev = new SymSet(symnum);
//                test(fsys, nxtlev, 19);
                break;
        }

    }

    
    public void condStatement(SymSet fsys, int lev) {
        SymSet nxtlev;
        loadNextSymbol();
        if(symbol==Symbol.lparen){
            loadNextSymbol();
            nxtlev= (SymSet) fsys.clone();
            nxtlev.set(Symbol.rparen);
            expression(nxtlev,lev);
            if(symbol!=Symbol.rparen) Error.print(118);
            loadNextSymbol();
            singleStatement(fsys,lev);
            if(symbol==Symbol.elsesym){
                loadNextSymbol();
                singleStatement(fsys, lev);
            }
        }else Error.print(117);
    }

    
    public void cycStatement(SymSet fsys, int lev) {
        SymSet nxtlev;
        loadNextSymbol();
        if(symbol==Symbol.lparen){
            loadNextSymbol();
            nxtlev= (SymSet) fsys.clone();
            nxtlev.set(Symbol.rparen);
            expression(nxtlev,lev);
            if(symbol!=Symbol.rparen) Error.print(119);
            loadNextSymbol();
            singleStatement(fsys,lev);
        }else Error.print(119);
    }

    
    public void callStatement() {

    }

    
    public void assignmentStatement(SymSet fsys, int lev) {
        SymbolTable.Item item;
        SymSet nxtlev;
        item = Compiler.symbolTable.getByNameScope(Compiler.scanner.id,Compiler.cur_func);
        if (item!=null) {
            if (item.getType()== SymbolTable.Type.variable) {
                loadNextSymbol();
                if (symbol == Symbol.becomes)
                    loadNextSymbol();
                else
                    Error.print(13);					// 没有检测到赋值符号
                nxtlev = (SymSet) fsys.clone();
                expression(nxtlev, lev);
                // parseExpression将产生一系列指令，但最终结果将会保存在栈顶，执行sto命令完成赋值
                Compiler.generator.generate(Fct.STO, lev - item.getLevel(), item.getAddress());
            } else {
                Error.print(12);						// 赋值语句格式错误
            }
        } else {
            Error.print(11);							// 变量未找到
        }

    }



    public void retStatement(SymSet fsys, int lev) {
        SymSet nxtlev;
        loadNextSymbol();
        if (symbol == Symbol.lparen) {
            loadNextSymbol();
            nxtlev = (SymSet) fsys.clone();
            nxtlev.set(Symbol.rparen);
            expression(fsys, lev);
            if (symbol != Symbol.rparen) Error.print(116);
            loadNextSymbol();
            if (symbol != Symbol.semicolon) Error.print(117);
            Compiler.generator.generate(Fct.RET, 0, 0);//TODO 考虑怎么返回值
        }else if(symbol==Symbol.semicolon){
            Compiler.generator.generate(Fct.RET, 0, 0);
        }else Error.print(115);
    }


    public void readStatement(SymSet fsys, int lev) {
        loadNextSymbol();
        if (symbol == Symbol.lparen) {
            Error.print(109);
            loadNextSymbol();
            if (symbol != Symbol.ident) Error.print(110);
            SymbolTable.Item item = Compiler.symbolTable.getByNameScope(Compiler.scanner.id,Compiler.cur_func);
            if (item.getType() != SymbolTable.Type.variable) Error.print(109);
            loadNextSymbol();
            if (symbol != Symbol.rparen) Error.print(111);
            loadNextSymbol();
            if (symbol != Symbol.semicolon) Error.print(112);
            Compiler.generator.generate(Fct.RED, 0, 0);
        }else test(fsys, facbegsys, lev);

    }


    public void writeStatement(SymSet fsys, int lev) {
        SymSet nxtlev;
        loadNextSymbol();
        if (symbol == Symbol.lparen) {
            loadNextSymbol();
            nxtlev = (SymSet) fsys.clone();
            nxtlev.set(Symbol.rparen);
            expression(fsys, lev);
            if (symbol != Symbol.rparen) Error.print(113);
            loadNextSymbol();
            if (symbol != Symbol.semicolon) Error.print(114);
            Compiler.generator.generate(Fct.WRT, 0, 0);

        }
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
            Compiler.generator.generate(Fct.LIT,0,0);
            term(nxtlev, lev);
            if (addop == Symbol.minus) {
                Compiler.generator.generate(Fct.SUB, 0, 0);
            }else Compiler.generator.generate(Fct.ADD,0,0);
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
                Compiler.generator.generate(Fct.ADD, 0, 0);
            } else {
                Compiler.generator.generate(Fct.SUB, 0, 0);
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
                Compiler.generator.generate(Fct.MUL, 0, 0);
            } else {
                Compiler.generator.generate(Fct.DIV, 0, 0);
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
                        if(item.getScope().equals(Compiler.cur_func))
                            Compiler.generator.generate(Fct.LOD, lev - item.getLevel(), item.getAddress());
                        else Error.print(123);
                        break;
                    case procedure:			// 名字为过程
                        Compiler.generator.generate(Fct.CAL, 0, item.getAddress());//TODO 改指令 搞清函数地址
                        break;
                }
                loadNextSymbol();
            }else if (symbol == Symbol.number) {	// 因子为数
                int num = Compiler.scanner.num;
                if (num > Constant.amax) {
                    Error.print(31);
                    num = 0;
                }
                Compiler.generator.generate(Fct.LIT, 0, num);
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
