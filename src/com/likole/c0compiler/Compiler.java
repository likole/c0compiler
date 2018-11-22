package com.likole.c0compiler;

import com.likole.c0compiler.compiler.utils.SymbolTable;
import com.likole.c0compiler.interpreter.Interpreter;

import javax.swing.*;
import java.io.File;

/**
 * @author kanghao
 * @date 18-11-22 下午3:16
 */
public class Compiler extends JFrame {



    SymbolTable symbolTable;
    Interpreter interpreter;

    public Compiler(){
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
        fileChooser.showDialog(new JLabel(), "选择");
        File file=fileChooser.getSelectedFile();
    }


    public static void main(String[] args) {
        Compiler compiler=new Compiler();
        compiler.setVisible(true);
    }
}
