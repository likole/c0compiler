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
    }

    /**
     * 符号表
     */
    private List<Item> items = new ArrayList<>();


    public void add(Type type, int level, int address) {
        Item item = new Item();
        item.name = Compiler.scanner.id;
        item.setType(type);
        switch (type) {
            case variable:
                item.setLevel(level);
                item.setAddress(address);
                break;
            case procedure:
                item.setLevel(level);
                break;
            default:
                break;
        }
        items.add(item);
    }

    /**
     * 获取符号表的大小
     *
     * @return
     */
    public int getSize() {
        return items.size();
    }

    public Item getByName(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                return items.get(i);
            }
        }
        return null;
    }

}
