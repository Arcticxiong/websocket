package com.fly.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData{//资源类
    private int number = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void increment()throws Exception{

        lock.lock();
        try {
            //判断  多线程判断不能用if
            while (number !=0){//用while 不用if 防止虚假唤醒
                //等待 不能生产
                condition.await();
            }
            //干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void decrement()throws Exception{

        lock.lock();
        try {
            //判断
            while (number == 0){
                //等待 不能生产
                condition.await();
            }
            //干活
            number --;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
            //通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

}

/**
 * 传统版生产者消费者模式
 *
 * 初始值为0的一个变量，两个线程对其交替操作，一个加1一个减1，来5轮
 *
 * 1. 线程 操作（方法） 资源类
 * 2. 判断 干活 通知
 * 3. 防止虚假唤醒机制
 */
public class ProdCustomer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"AAA").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"BBB").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"CCC").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"DDD").start();
    }
}
