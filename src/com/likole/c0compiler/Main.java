package com.likole.c0compiler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.Buffer;

/**
 *
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
    private JButton outputButton;
    private JTextArea textArea1;

    private File sourceFile;

    public Main() {
        initView();
    }

    private String readFile(File file) throws IOException {
        Long fileLength=file.length();
        byte[] fileContent=new byte[fileLength.intValue()-1];
        FileInputStream fileInputStream=new FileInputStream(file);
        fileInputStream.read(fileContent);
        fileInputStream.close();
        return new String(fileContent);
    }

    private void initView(){
        openButton.addActionListener(e -> {
            JFileChooser fileChooser=new JFileChooser();
            fileChooser.showDialog(new JLabel(),"打开");
            sourceFile=fileChooser.getSelectedFile();
            try {
                sourceCodeTextArea.setText(readFile(sourceFile));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        compileButton.addActionListener(e->{
            String code=sourceCodeTextArea.getText();
            boolean showObjectCode= objectCodeCheckBox.isSelected();
            boolean showSymbolTable=symbolTableCheckBox.isSelected();
            Compiler compiler=new Compiler(code,showObjectCode,showSymbolTable);
            try {
                if(compiler.compile()){
                    JOptionPane.showMessageDialog(mainPanel, "编译成功", "编译成功",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(mainPanel, "无法编译", "编译错误",JOptionPane.ERROR_MESSAGE);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
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
