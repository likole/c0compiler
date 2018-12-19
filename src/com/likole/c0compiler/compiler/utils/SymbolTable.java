package com.likole.c0compiler.compiler.utils;

import com.likole.c0compiler.Compiler;
import com.likole.c0compiler.entity.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by likole on 10/18/18.
 */
public class SymbolTable {

    /**
     * 种类
     */
    public enum Type {
        variable, procedure
    }

    /**
     * 符号表中一项
     */
    public class Item {

        /**
         * 名字
         */
        private String name;
        /**
         * 种类
         */
        private Type type;
        /**
         * 标识符的数值(constant使用)
         */
        private int value;
        /**
         * 标识符所在的层(constant不用)
         */
        private int level;
        /**
         * 标识符相对地址(constant不用)
         */
        private int address;
        /**
         * 需分配的数据区大小(procedure使用)
         */
        private int size;
        /**
         * 返回值类型
         */
        private Symbol returnType;
        /**
         * 作用域
         */
        private String scope;

        public Symbol getReturnType() {
            return returnType;
        }

        public void setReturnType(Symbol returnType) {
            this.returnType = returnType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getAddress() {
            return address;
        }

        public void setAddress(int address) {
            this.address = address;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    /**
     * 符号表
     */
    private List<Item> items = new ArrayList<>();


    public void add(Type type, int level,Symbol returnType) {
        Item item = new Item();
        item.name = Compiler.scanner.id;
        item.setType(type);
        item.setLevel(level);
        item.setReturnType(returnType);
        items.add(item);
    }

    public void add(Type type, int level, int address,String scpoe) {
        Item item = new Item();
        item.name = Compiler.scanner.id;
        item.setType(type);
        item.setLevel(level);
        item.setAddress(address);
        item.setScope(scpoe);
        items.add(item);
    }

    public void newItem(int address){
        items.add(new Item());
    }

    /**
     * 获取符号表的大小
     *
     * @return
     */
    public int getSize() {
        return items.size();
    }

    public Item getLast(){
        return items.get(getSize()-1);
    }

    public Item getByName(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                return items.get(i);
            }
        }
        return null;
    }

    public Item getByNameScope(String name,String scope){
        for (int i = 0; i < items.size(); i++) {
            Item item=items.get(i);
            if (item.getName().equals(name)&&item.getScope().equals(scope)) {
                return items.get(i);
            }
        }
        for (int i = 0; i < items.size(); i++) {
            Item item=items.get(i);
            if (item.getName().equals(name)&&item.getScope().equals("NULL")) {
                return items.get(i);
            }
        }
        return null;
    }

    public void listTable(){
        Compiler.fas.printf("%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n","Name","Type","Value","Level","Address","Size","ReturnType","Scope");
        for(Item item:items) {
            Compiler.fas.printf("%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s\n",item.getName(),item.getType(),
                    item.getValue(),item.getLevel(),item.getAddress(),item.getSize(),item.getReturnType(),item.getScope());
        }
        return ;
    }

}
