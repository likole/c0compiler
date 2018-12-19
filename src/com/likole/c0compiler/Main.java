package com.likole.c0compiler;

import com.likole.c0compiler.entity.Instruction;
import com.likole.c0compiler.interpreter.Interpreter;
import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.likole.c0compiler.Compiler.interpreter;

/**
 * @author likole
 * @date 11/22/18
 */
public class Main {
    private JTextArea sourceCodeTextArea;
    private JCheckBox objectCodeCheckBox;
    private JCheckBox symbolTableCheckBox;
    private JButton compileButton;
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private JPanel inputPanel;
    private JButton openButton;
    private JButton stepButton;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;

    private File sourceFile;

    public Main() {
        initView();

    }

    private String readFile(File file) throws IOException {
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(fileContent);
        fileInputStream.close();
        return new String(fileContent);
    }

    private void initView() {
        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showDialog(new JLabel(), "打开");
            sourceFile = fileChooser.getSelectedFile();
            try {
                sourceCodeTextArea.setText(readFile(sourceFile));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        compileButton.addActionListener(e -> {
            String code = sourceCodeTextArea.getText();
            boolean showObjectCode = objectCodeCheckBox.isSelected();
            boolean showSymbolTable = symbolTableCheckBox.isSelected();
            Compiler compiler = new Compiler(code, showObjectCode, showSymbolTable);
            try {
                int result = compiler.compile();
                if (result == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "编译成功", "编译成功", JOptionPane.INFORMATION_MESSAGE);
                } else if (result == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "发生了未知的错误，请检查日志", "编译失败", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "编译时发生了" + result + "个错误", "编译失败", JOptionPane.ERROR_MESSAGE);
                }
                textArea1.setText(readFile(new File("sourceCode")));
                if (showObjectCode) textArea2.setText(readFile(new File("objectCode")));
                if (showSymbolTable) textArea3.setText(readFile(new File("symbolTable")));
                interpreter.init();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stepButton.addActionListener(actionEvent -> {
            interpreter.test();
//            interpreter.interpret();
//            interpreter.print();
            try {
                textArea4.setText(readFile(new File("stackOut")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
