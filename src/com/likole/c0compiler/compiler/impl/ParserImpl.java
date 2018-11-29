package com.likole.c0compiler.compiler.impl;


import com.likole.c0compiler.compiler.Parser;
import com.likole.c0compiler.entity.Fct;
import com.likole.c0compiler.entity.SymSet;
import com.likole.c0compiler.entity.Symbol;
import com.likole.c0compiler.Compiler;

/**
 * Created by likole on 11/22/18.
 */
public class ParserImpl implements Parser {

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

    public ParserImpl() {

        // 设置声明开始符号集
        declbegsys = new SymSet(Constant.symnum);
        declbegsys.set(Symbol.constsym);
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

    @Override
    public void test(SymSet s1, SymSet s2, int errorcode) {

    }

    @Override
    public void loadNextSymbol() {

    }


    @Override
    public void prepare() {

    }

    @Override
    public void parse() {

    }

    @Override
    public void varDeclaration() {

    }

    @Override
    public void funcDeclaration() {

    }

    @Override
    public void block() {

    }

    @Override
    public void statementSeq() {

    }

    @Override
    public void singleStatement() {

    }

    @Override
    public void condStatement() {

    }

    @Override
    public void cycStatement() {

    }

    @Override
    public void callStatement() {

    }

    @Override
    public void assignmentStatement() {

    }

    @Override
    public void retStatement() {

    }

    @Override
    public void readStatement() {

    }

    @Override
    public void writeStatement() {

    }

    @Override
    public void callFunction() {

    }

    @Override
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
            if (addop == Symbol.minus)
                Compiler.generator.generate(Fct.OPR, 0, 1);//TODO 改指令
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
            if (addop == Symbol.plus)
                Compiler.generator.generate(Fct.OPR, 0, 2);//TODO 改指令
            else
                Compiler.generator.generate(Fct.OPR, 0, 3);//TODO 改指令
        }
    }

    @Override
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
            if (mulop == Symbol.times)
                Compiler.generator.generate(Fct.OPR, 0, 4);//TODO 改指令
            else
                Compiler.generator.generate(Fct.OPR, 0, 5);//TODO 该指令
        }
    }

    @Override
    public void factor(SymSet fsys,int lev) {

    }
}
