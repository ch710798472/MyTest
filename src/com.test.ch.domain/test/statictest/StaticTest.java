package com.test.ch.domain.test.statictest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by banmo.ch on 17/3/14.
 *
 * @author banmo.ch
 * @date 2017/03/14
 */
public class StaticTest {
    static {
        str = 1;
    }

    public static Test1 t = new Test1();
    public static int a = 0;
    public static int b;
    public static int str = 2;

    public static Map<String, String> map = new HashMap<String, String>();

    public static void init() {
        putMap();
    }

    //public static void main(String[] args) {
    //    System.out.println(str);
    //    map.put("1", "first");
        //putMap();
        //System.out.println(map);
        //System.out.println(a);
        //System.out.println(b);
    //}
    //
    static class Test1 {
        public Test1() {
            StaticTest.a++;
            StaticTest.b++;
        }
    }

    private static void putMap() {
        map.put("2", "second");
        System.out.println(map.toString());
    }
}
