package com.likole.c0compiler;

import com.likole.c0compiler.entity.Instruction;
import com.likole.c0compiler.interpreter.InterpreterListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

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
    private JButton runButton;
    private JTextArea textArea5;
    private JButton backButton;
    private JLabel nowLabel;
    private JTable table1;
    private JTable table2;

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
            int selected= fileChooser.showDialog(new JLabel(), "打开");
            sourceFile = fileChooser.getSelectedFile();
            try {
                if(sourceFile!=null&&selected==JFileChooser.APPROVE_OPTION){
                    sourceCodeTextArea.setText(readFile(sourceFile));
                }
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
                } else if(result == -2){
                    JOptionPane.showMessageDialog(mainPanel, "编译时发生严重错误，无法继续编译", "编译失败", JOptionPane.ERROR_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(mainPanel, "编译时发生了" + result + "个错误", "编译失败", JOptionPane.ERROR_MESSAGE);
                }
                textArea1.setText(readFile(new File("sourceCode")));
                if (showObjectCode) {
                    textArea2.setText(readFile(new File("objectCode")));
                }else{
                    textArea2.setText("未生成虚拟机代码");
                }
//                名字表开始
                DefaultTableModel model = new DefaultTableModel();
                Vector names=new Vector();
                String[] titles={"Name","Type","Value","Level","Address","Size","ReturnType","Scope"};
                for (int i = 0; i < 8; i++) {
                    names.add(titles[i]);
                }
                Vector data=new Vector();
                if (showSymbolTable) {
                    String symbolTable=readFile(new File("symbolTable"));
                    String[] symbolTables=symbolTable.split("\n");
                    for(String s:symbolTables){
                        String[] ss=s.split(",");
                        if(ss.length==8){
                            Vector row=new Vector();
                            for (String content:ss){
                                row.add(content);
                            }
                            data.add(row);
                        }
                    }
                }else{
                    Vector row=new Vector();
                    String notGenerated="没有生成名字表！";
                    for(int i=0;i<8;i++){
                        row.add(notGenerated.charAt(i));
                    }
                    data.add(row);
                }
                model.setDataVector(data,names);
                table1.setModel(model);
//                名字表结束
                interpreter.init();
                textArea5.setText("");
                nowLabel.setText("");
                table2.setModel(new DefaultTableModel());
                interpreter.setInterpreterListener(new InterpreterListener() {
                    @Override
                    public void print(int num) {
                        textArea5.setText(textArea5.getText()+"\n"+num);
                    }

                    @Override
                    public String read() {
                        while (true){
                            return JOptionPane.showInputDialog("请输入数据","");

                        }

                    }

                    @Override
                    public void readError() {
                        JOptionPane.showMessageDialog(mainPanel, "输入数据有误", "运行错误", JOptionPane.ERROR_MESSAGE);
                    }

                    @Override
                    public void divideByZero() {
                        JOptionPane.showMessageDialog(mainPanel, "除0错误", "运行错误", JOptionPane.ERROR_MESSAGE);
                    }

                    @Override
                    public void finished() {
                        nowLabel.setText("程序运行完毕");
                        JOptionPane.showMessageDialog(mainPanel, "程序已运行完成", "运行完成", JOptionPane.INFORMATION_MESSAGE);
                    }

                    @Override
                    public void now(int line,int base, Instruction instruction) {
                        nowLabel.setText("正在运行第 "+line+" 行："+instruction.getAction()+" "+instruction.getL()+" "+instruction.getParam()+"        "+"基址："+base);
                    }

                    @Override
                    public void stack() {
                        try {
                            String pre=readFile(new File("stackOutPre"));
                            String after=readFile(new File("stackOutAfter"));
                            String[] pres=pre.split("\n");
                            String[] afters=after.split("\n");
                            int length=Math.max(pres.length,afters.length);
                            DefaultTableModel model = new DefaultTableModel();
                            Vector names=new Vector();
                            names.add("");
                            names.add("原数据");
                            names.add("现在数据");
                            Vector data=new Vector();
                            for (int i = length-1; i >=0; i--) {
                                Vector row=new Vector();
                                row.add(i);
                                if(pres.length>i){
                                    row.add(pres[i]);
                                }else{
                                    row.add("");
                                }
                                if(afters.length>i){
                                    row.add(afters[i]);
                                }else{
                                    row.add("");
                                }
                                data.add(row);
                            }
                            model.setDataVector(data,names);
                            table2.setModel(model);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        stepButton.addActionListener(actionEvent -> {
            interpreter.interpret();
        });
        runButton.addActionListener(actionEvent -> {
            interpreter.run();
        });
        backButton.addActionListener(e -> {
            nowLabel.setText("");
            table2.setModel(new DefaultTableModel());
            interpreter.init();
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
