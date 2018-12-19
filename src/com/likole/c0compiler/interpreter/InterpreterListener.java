package com.likole.c0compiler.interpreter;

import com.likole.c0compiler.entity.Instruction;

/**
 * Created by likole on 12/19/18.
 */
public interface InterpreterListener {

    void print(int num);

    String read();

    void readError();

    void divideByZero();

    void finished();

    void now(int line,int base, Instruction instruction);

    void stack();
}
