package com.test.ch.domain.testand;

/**
 * Created by banmo.ch on 17/5/24.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(getFalseAndTrue());
    }

    private static boolean getFalseAndTrue() {
        return false && printWorld();
    }

    private static boolean printWorld() {
        System.out.println("a~");
        return true;
    }
}
