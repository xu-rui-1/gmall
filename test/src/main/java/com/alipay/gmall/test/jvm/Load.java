/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.jvm;

/**
 * @author ruitu.xr
 * @version Load.java, v 0.1 2023年06月05日 14:52 ruitu.xr Exp $
 */
public class Load {
    public static void main(String[] args) throws ClassNotFoundException {
        //Singleton instance = Singleton.getInstance();
        //Singleton instance1 = Singleton.getInstance();
        ClassLoader classLoader = Load.class.getClassLoader();
        classLoader.loadClass("com.alipay.gmall.test.jvm.Load");
    }
}

class Singleton {
    private Singleton() {};

    private static class LazyHolder{
        private static final Singleton SINGLETON = new Singleton();

        static {
            System.out.println("init lazy holder");
        }

        private static Singleton getSingleton() {
            return SINGLETON;
        }
    }

    public static Singleton getInstance() {
        return LazyHolder.getSingleton();
    }
}
