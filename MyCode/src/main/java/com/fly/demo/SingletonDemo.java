package com.fly.demo;

public class SingletonDemo {

    private static volatile SingletonDemo instance = null;

    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletonDemo()");
    }

//    public static SingletonDemo getInstance(){
//        if (instance == null) {
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }

    //DCL （Double Check Lock双端检索机制 潜在隐患，指令重排（先分配物理空间，后初始化），可能物理地址不为空，初始化为未完成就取，可能内容为空,需要在instance前加上volatile禁止指令重排）
    public static SingletonDemo getInstance(){
        if (instance == null) {
            synchronized (SingletonDemo.class){
                if(instance == null){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        //单线程（main线程的操作动作）
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
//        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        //并发多线程后，情况发生了变化

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                SingletonDemo.getInstance();
            },String.valueOf(i)).start();
        }
    }
}
