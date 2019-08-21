package com.fly.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyData{
//    int number = 0;
    volatile int number = 0;
    public void addTO60(){
        this.number = 60;
    }

    //请注意，此时number已经加了volatile关键字修饰，volatile不保证原子性
//    public synchronized void addPlusPlus(){
    public void addPlusPlus(){
        number++;
    }

    AtomicInteger atomicInteger = new AtomicInteger();//默认为0
    public void addAtomic(){
        atomicInteger.getAndIncrement();//原子性的number++
    }
}

/**
 * 1.验证volatile的可见性
 *     1.1 假如int number = 0；number变量之前根本没有添加volatile关键字修饰,没有可见性
 *     1.2 添加了volatile关键字，可以解决可见性问题
 * 2.验证volatile不保证原子性
 *     2.1 原子性指的是什么意思？
 *            不可分割，完整性，也即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完整，要么同时成功，要么同时失败。
 *     2.2 volatile不保证原子性的案例（数值少于20000 出现了数据丢失的情况）
 *     2.3 为什么不能保证原子性？ （写覆盖）
 *     2.4 如何解决原子性？
 *          1.加synchronized
 *          2.使用java.util.concurrent下的AtomicInteger
 *
 *
 *
 */
public class VolatileDemo {
    public static void main(String[] args) {//main是一切方法的运行入口
//        seeOKByVolatile();
        MyData myData = new MyData();

        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000; j++) {
                    myData.addPlusPlus();
                    myData.addAtomic();
                }
            },String.valueOf(i)).start();
        }
        //需要等待上面20个线程全部计算完成后，再用main线程取得最终的结果值
        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+"\t int type finally number valur: " + myData.number);
        System.out.println(Thread.currentThread().getName()+"\t AtomicInteger type finally atomicInteger valur: " + myData.atomicInteger);
    }

    //volatile可以保持可见性，及时通知其他线程，主物理内存的值已经被修改
    private static void seeOKByVolatile() {
        MyData myData = new MyData();//资源类
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                //暂停一会儿线程
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTO60();
            System.out.println(Thread.currentThread().getName() + "\t update number value:" + myData.number);

        },"AAA").start();

        //第二个线程就是我们的main线程
        while (myData.number == 0){
            //main线程就一直在这里等待循环，直到number值不在等于0
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over，main get number value : " + myData.number);
    }
}
