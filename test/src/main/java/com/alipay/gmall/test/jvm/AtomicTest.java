/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.test.jvm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author ruitu.xr
 * @version AtomicTest.java, v 0.1 2023年06月05日 17:05 ruitu.xr Exp $
 */
public class AtomicTest {
    private static AtomicInteger count = new AtomicInteger(0);

    private static final ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 10,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("INFOSECMODEL", "SYNTHETIZE_COMBAT_MODEL",
                "XHUNTER_MODEL", "PRETESTMODEL"));
        System.out.println(list);
        list.add("dada");
        System.out.println(list);
        list.clear();
        System.out.println(list);

    }

    private static void deadLock() {
        final Object a = new Object();
        final Object b = new Object();

        new Thread(()-> {
            synchronized (a) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有A锁，需要获取B锁" );
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b) {
                    System.out.println(Thread.currentThread().getName() + "\t 成功获取B锁");
                }
            }
        }, "线程A").start();

        new Thread(()-> {
            synchronized (b) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持B锁，需要获取A锁" );
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a) {
                    System.out.println(Thread.currentThread().getName() + "\t 成功获取A锁");
                }
            }
        }, "线程B").start();
    }

    private static void future3() {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("111");
                return 1;
            }, EXECUTOR_SERVICE).thenApply(new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer s) {
                    System.out.println("222");
                    return s + 2;
                }
            }).thenApply(f -> {
                System.out.println("333");
                return f + 3;
            }).whenComplete(new BiConsumer<Integer, Throwable>() {
                @Override
                public void accept(Integer integer, Throwable throwable) {
                    if (throwable == null) {
                        System.out.println("计算结果: " + integer);
                    }
                }
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                System.out.println(throwable.getCause() + throwable.getMessage());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EXECUTOR_SERVICE.shutdown();
        }
    }

    private static void future2() {
        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "----come in");
                int result = ThreadLocalRandom.current().nextInt(10);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-------1秒钟后出结果：" + result);
                return result;
            }, EXECUTOR_SERVICE).whenComplete(new BiConsumer<Integer, Throwable>() {
                @Override
                public void accept(Integer integer, Throwable throwable) {
                    if (throwable == null) {
                        System.out.println("-----计算完成，更新系统updateValue: " + integer);
                    }
                }
            }).exceptionally(new Function<Throwable, Integer>() {
                @Override
                public Integer apply(Throwable throwable) {
                    throwable.printStackTrace();
                    System.out.println("异常情况：" + throwable.getCause() + "\t" + throwable.getMessage());
                    return null;
                }
            });

            System.out.println(Thread.currentThread().getName() + "线程去忙其他任务");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EXECUTOR_SERVICE.shutdown();
        }
    }

    private static void future1() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "----come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("-------1秒钟后出结果：" + result);
            return result;
        });

        System.out.println(completableFuture.get());
    }
}

class SingletonNew {
    private volatile static SingletonNew SINGLETON_NEW = null;

    private SingletonNew() {

    }

    public SingletonNew getInstance() {
        if (SINGLETON_NEW == null) {
            synchronized (this) {
                if (SINGLETON_NEW == null) {
                    SINGLETON_NEW = new SingletonNew();
                }
            }
        }
        return SINGLETON_NEW;
    }
}
