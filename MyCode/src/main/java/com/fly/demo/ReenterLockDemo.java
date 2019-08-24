package com.fly.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Phone implements Runnable{

    public synchronized void sendSMS() throws Exception{
        System.out.println(Thread.currentThread().getId() + "\t invoked sendSMS()");
        sendEmail();
    }

    public synchronized void sendEmail() throws Exception{
        System.out.println(Thread.currentThread().getId() + "\t invoked sendEmail()");
    }

    //======================================================================================

    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        get();

    }
    public void get(){
        lock.lock();
        lock.lock(); //两两配对有几层锁都没事
        try {
            System.out.println(Thread.currentThread().getId() + "\t invoked get()");
            set();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
            lock.unlock();
        }
    }

    public void set(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getId() + "\t invoked set()");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

/**
 *  可重入锁（也叫递归锁）
 *
 *  线程可以进入任何一个它已经拥有的锁所同步的代码块
 * case 1  synchronized是一个典型的可重入锁
 *  14	 invoked sendSMS()  14线程在外层方法获取锁的时候
 * 14	 invoked sendEmail()  14线程在进入内层方法会自动获取锁
 * 15	 invoked sendSMS()
 * 15	 invoked sendEmail()
 *
 * case 2 ReentrantLock是一个典型的可重入锁
 *  16	 invoked get()
 * 16	 invoked set()
 * 17	 invoked get()
 * 17	 invoked set()
 *
 */
public class ReenterLockDemo {

    public static void main(String[] args) throws Exception{
        Phone phone = new Phone();

        new Thread(()->{
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t1").start();

        new Thread(()->{
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"t2").start();

        //============================================================
        Thread t3 = new Thread(phone);
        Thread t4 = new Thread(phone);
        t3.start();
        t4.start();
    }
}
