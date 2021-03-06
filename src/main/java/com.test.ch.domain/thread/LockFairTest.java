package com.test.ch.domain.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by banmo.ch on 17/4/1.
 */
public class LockFairTest implements Runnable {
    //创建公平锁
    private static ReentrantLock lock = new ReentrantLock(true);

    public void run() {
        while (true) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        LockFairTest lft = new LockFairTest();
        Thread th1 = new Thread(lft);
        Thread th2 = new Thread(lft);
        th1.start();
        th2.start();
    }
}
