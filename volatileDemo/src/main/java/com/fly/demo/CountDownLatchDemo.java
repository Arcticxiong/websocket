package com.fly.demo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
//        CountDownLatch1();
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t 国，被灭");
                countDownLatch.countDown();
            },CountryEnum.forEach_CountryEnum(i).getRetMsg()).start();
        }
        countDownLatch.await();

        System.out.println(Thread.currentThread().getName()+"\t ******秦，一统华夏");

    }

    private static void CountDownLatch1() throws InterruptedException {
    /*for (int i = 0; i < 6; i++) {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t 上完自习，离开教室");
        },String.valueOf(i)).start();
    }

    System.out.println(Thread.currentThread().getName()+"\t ******班长最后关门走人");*/

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t 上完自习，离开教室");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();

        System.out.println(Thread.currentThread().getName()+"\t ******班长最后关门走人");
    }
}
