package com.test.ch.domain.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by banmo.ch on 17/1/17.
 */
public class MainTest {
    public static void main(String[] args) {
//        Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
//        f.setAccessible(true);
//        Unsafe unsafe = (Unsafe) f.get(null);
    }
    public void execute(){
        System.out.println("execute");
    }
}
