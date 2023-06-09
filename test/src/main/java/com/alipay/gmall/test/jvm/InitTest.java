/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.jvm;

/**
 * @author ruitu.xr
 * @version InitTest.java, v 0.1 2023年06月05日 10:35 ruitu.xr Exp $
 */
public class InitTest {
    private String a = "s1";

    {
        b = 20;
    }

    private int b = 10;
    {
        a = "s2";
    }

    public InitTest(String a, int b) {
        this.a = a;
        this.b = b;
    }

    public static void main(String[] args) {
        InitTest s3 = new InitTest("s3", 30);
        System.out.println(s3.a);
        System.out.println(s3.b);
    }
}
