/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author ruitu.xr
 * @version PropertiesDemo.java, v 0.1 2023年06月09日 10:12 ruitu.xr Exp $
 */
public class PropertiesDemo {
    @Value("${test.title}")
    private String title;

    public static void main(String[] args) {
        PropertiesDemo demo = new PropertiesDemo();
        System.out.println(demo.title);
    }
}
