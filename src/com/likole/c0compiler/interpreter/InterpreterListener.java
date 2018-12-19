package com.likole.c0compiler.interpreter;

/**
 * Created by likole on 12/19/18.
 */
public interface InterpreterListener {

    void print(int num);

    String read();

    void readError();

    void divideByZero();

    void finished();
}
