/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.juc.synchronize;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @author ruitu.xr
 * @version SynchronizedDemo.java, v 0.1 2023年06月08日 13:33 ruitu.xr Exp $
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        /**
         * ReentrantLock 可重入锁
         */
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock();
        lock.unlock();

        /**
         * ReentrantReadWriteLock 可重入读写锁
         */
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.readLock().lock();
        readWriteLock.readLock().unlock();
        readWriteLock.writeLock().lock();
        readWriteLock.writeLock().unlock();

        /**
         * StampedLock 邮戳锁
         * 支持乐观读，即在读时可以写
         */
        StampedLock stampedLock = new StampedLock();
        /*
         * 传统读写锁功能支持
         */
        long stampRead = stampedLock.readLock();
        stampedLock.unlockRead(stampRead);
        long stampWrite = stampedLock.writeLock();
        stampedLock.unlockWrite(stampWrite);

        /*
            乐观读功能支持
         */
        long l = stampedLock.tryOptimisticRead();

    }

    private static void synchronizedLock() {
        Ticket ticket = new Ticket();
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.saleTicket();
            }
            System.out.println(ClassLayout.parseInstance(ticket).toPrintable());
        }, "a").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.saleTicket();
            }
        }, "b").start();

        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.saleTicket();
            }
        }, "c").start();
    }

    private static void noLock() {
        Object o = new Object();

        System.out.println(o.hashCode());
        System.out.println(Integer.toBinaryString(o.hashCode()));
        System.out.println(Integer.toHexString(o.hashCode()));
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}

class Ticket {
    private int number = 50;

    private final Object lockObject = new Object();

    public void saleTicket() {
        synchronized (lockObject) {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出第\t" + number-- + "\t票，还剩：" + number + " 张票");
            }
        }
    }
}
