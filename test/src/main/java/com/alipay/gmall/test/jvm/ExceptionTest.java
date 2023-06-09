/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.jvm;

/**
 * @author ruitu.xr
 * @version ExceptionTest.java, v 0.1 2023年06月05日 11:11 ruitu.xr Exp $
 */
public class ExceptionTest {
    public static void main(String[] args) {
        Runnable runnable = test02();
        runnable.run();
    }

    public static int test() {
        try {
            int i = 1/ 0;
            return i;
        } finally {
            int i = 20;
            return i;
        }
    }

    public static int test1() {
        int i = 10;
        try {
            return i;
        } finally {
            i = 20;
        }
    }

    public static Runnable test02() {
        int a = 10;
        return new Runnable() {
            @Override
            public void run() {
                System.out.println(a);
            }
        };
    }
}
