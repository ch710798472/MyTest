package com.test.ch.domain.classloader;

/**
 * Created by banmo.ch on 17/4/1.
 */
public class test {
    public void hello() {
        System.out.println("恩，是的，我是由 " + getClass().getClassLoader().getClass()
            + " 加载进来的");
    }
}
