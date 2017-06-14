package com.test.ch.domain.NPE;

/**
 * Created by banmo.ch on 17/5/15.
 */
public class Stest {
    private static Stest ourInstance = new Stest();

    public static Stest getInstance() {
        return ourInstance;
    }

    private Stest() {
    }
}
