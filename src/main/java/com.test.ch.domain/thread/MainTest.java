package com.test.ch.domain.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by banmo.ch on 17/3/15.
 *
 * @author banmo.ch
 * @date 2017/03/15
 */
public class MainTest {
    private static ThreadPoolExecutor threadpool;

    /**
     * Param:
     * corePoolSize - 池中所保存的线程数，包括空闲线程。
     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。
     * keepAliveTime -当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间。
     * unit - keepAliveTime参数的时间单位，线程池维护线程所允许的空闲时间的单位:秒 。
     * workQueue - 执行前用于保持任务的队列（缓冲队列）。此队列仅保持由execute 方法提交的 Runnable 任务。
     * RejectedExecutionHandler -线程池对拒绝任务的处理策略(重试添加当前的任务，自动重复调用execute()方法)
     */
    public MainTest(){
        threadpool=new ThreadPoolExecutor(2, 10, 0, TimeUnit.SECONDS, new LinkedBlockingDeque(10),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    //add task into thread pool
    public void submit(final int flag){
        threadpool.execute(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(flag + "   Hello" + " activeCount=" + threadpool.getActiveCount() + " coreSize = "+threadpool.getCorePoolSize());
            }
        });
    }

    /**
     * close thread pool
     */
    public void shutdown() {
        threadpool.shutdown();
    }

    public static void main(String[] args) {
        MainTest t = new MainTest();
        for (int i = 0; i < 20; i++) {
            System.out.println("time:" + i);
            t.submit(i);
        }
        threadpool.shutdown();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
