/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.juc.threadlocal;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author ruitu.xr
 * @version ThreadLocalDemo.java, v 0.1 2023年06月07日 17:23 ruitu.xr Exp $
 */
public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        //synchronizedMethod();
        MyData myData = new MyData();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        try {
            for (int i = 0; i < 10; i++) {
                executorService.execute(()-> {
                    try {
                        Integer beforeInit = myData.threadLocalField.get();
                        myData.add();
                        Integer afterInit = myData.threadLocalField.get();
                        System.out.println(beforeInit + "\t" + afterInit);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                        myData.threadLocalField.remove();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        countDownLatch.await();
        System.out.println("main end");
    }

    private static void synchronizedMethod() {
        House house = new House();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                int size = new Random().nextInt(5) + 1;
                try {
                    //System.out.println(size);
                    for (int j = 0; j < size; j++) {
                        house.saleHouse();
                        house.saleVolumeByThreadLocal();
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 号销售卖出: " + house.saleVolume.get());
                } finally {
                    house.saleVolume.remove();
                }
            }, String.valueOf(i)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t 共计卖出：" + house.saleCount);
    }
}

class House {
    int saleCount = 0;

    public synchronized void saleHouse() {
        ++saleCount;
    }

    /*ThreadLocal<Integer> saleVolume = new ThreadLocal<Integer>(){
        protected Integer initialValue() {
            return 0;
        }
    };*/
    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(() -> 0);

    public void saleVolumeByThreadLocal() {
        saleVolume.set(1 + saleVolume.get());
    }

}

class MyData {
    ThreadLocal<Integer> threadLocalField = ThreadLocal.withInitial(() -> 0);

    public void add() {
        threadLocalField.set(threadLocalField.get() + 1);
    }
}
