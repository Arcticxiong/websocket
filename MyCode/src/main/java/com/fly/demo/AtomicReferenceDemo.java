package com.fly.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;


@Getter
@ToString
@AllArgsConstructor
class User{
    String userName;
    int age;
}

/**
 * 解决ABA问题， 理解原子引用+ 新增一种机制，那就是修改版本号（类似时间戳）
 */
public class AtomicReferenceDemo {

    public static void main(String[] args) {
        User z3 = new User("z3", 22);
        User l4 = new User("l4", 25);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(z3);

        System.out.println(atomicReference.compareAndSet(z3, l4) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3, l4) + "\t" + atomicReference.get().toString());


    }
}
