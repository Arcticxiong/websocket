package com.fly.demo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *  集合类不安全的问题
 *   ArrayList
 */
public class ContainerNotSafeDemo {

    public static void main(String[] args) {
//        listNotSafe();
//        setNotSafe();
//        Map<String,String> map = new HashMap<>();
//        Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String,String> map = new ConcurrentHashMap<>();//回顾hashmap和ConcurrentHashMap底层区别
        for (int i = 1; i <= 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,8));//add方法没有加synchornized
                System.out.println(map);
            },String.valueOf(i)).start();
        }

    }

    private static void setNotSafe() {
        //        Set<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();//底层实际也是一个CopyOnWriteArrayList
        for (int i = 1; i <= 30; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));//add方法没有加synchornized
                System.out.println(set);
            },String.valueOf(i)).start();
        }

//        new HashSet<>();//底层是hashmap，可是hashset的add只有一个参数，而map是需要2个参数？ add的参数是key，value是一个叫present的object类型的常量，即value是恒定的
    }

    private static void listNotSafe() {
        //        new ArrayList<Integer>();//此时底层是一个初始值为10的空的数组

        /*List<String> list1 = Arrays.asList("a", "b", "c");
        list1.forEach(System.out::println);*/

//        List<String> list = new ArrayList<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();
        /*list.add("a");
        list.add("b");
        list.add("c");
        for (String element : list) {
            System.out.println(element);
        }*/

        for (int i = 1; i < 4; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));//add方法没有加synchornized
                System.out.println(list);
            },String.valueOf(i)).start();
        }
        //java.util.ConcurrentModificationException 并发修改异常  常见的异常列出几个

        /**
         *  1 故障现象
         *          java.util.ConcurrentModificationException
         *  2.导致原因
         *      并发争抢修改导致，参考我们的花名册签名情况，一个人正在写，另一个人抢夺，导致数据不一致异常，即并发修改异常
         *  3.解决方案
         *      3.1 new Vector()
         *      3.2 Collections.synchronizedList(new ArrayList<>());
         *      3.3  new CopyOnWriteArrayList<>() ******** 写时复制  读写分离的思想
         *
         *  4.优化建议（同样的错误不犯第二次）
         */

        /** 写时复制
         * CopyOnWrite容器即写时复制的容器，往一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将容器Object[]进行copy，
         * 复制出一个新的容器Object[] newElements，然后往新的容器Object[] newElements里添加元素，添加完元素之后，再将原容器的引用指向新的容器setArray（newElements）；
         * 这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器
         *
         public boolean add(E e) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                Object[] elements = getArray();
                int len = elements.length;
                Object[] newElements = Arrays.copyOf(elements, len + 1);
                newElements[len] = e;
                setArray(newElements);
                return true;
            } finally {
                lock.unlock();
             }
         }
         */}
}
