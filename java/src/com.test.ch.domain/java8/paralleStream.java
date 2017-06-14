package com.test.ch.domain.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by banmo.ch on 17/5/4.
 */
public class paralleStream {

    public static void main(String[] args) {
        List<Apple> appleList = new ArrayList<Apple>();
        appleList.add(new Apple(1, "black"));
        appleList.add(new Apple(2, "black"));
        appleList.add(new Apple(3, "red"));
        appleList.add(new Apple(4, "black"));
        appleList.add(new Apple(5, "red"));

        List stringList = new ArrayList<String>();
        for (int i = 0; i < 10000; i++) {
            if (i % 2 == 1) {
                stringList.add("a");
                appleList.add(new Apple(5, "red"));
            }
            if (i % 2 == 0) {
                appleList.add(new Apple(1, "black"));
                stringList.add("b");
            }
        }
        long start1 = System.currentTimeMillis();
        List<Apple> testList = appleList.stream().filter(a -> a.getWeight() > 2).collect(toList());
        //stringList.stream().filter(a -> a.equals("b")).collect(toList());
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);

        long start2 = System.currentTimeMillis();
        List<Apple> testParalleList = appleList.parallelStream().filter(a -> a.getWeight() > 2).collect(toList());
        //stringList.parallelStream().filter(a -> a.equals("b")).collect(toList());
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - start2);

        long start3 = System.currentTimeMillis();
        List<Apple> newAppleList = new ArrayList<Apple>();
        for (Apple apple : appleList) {
            if (apple.getWeight() > 2) {
                newAppleList.add(apple);
            }
        }
        long end3 = System.currentTimeMillis();
        System.out.println(end3 - start3);

        Runnable r1 = () -> System.out.println("hello world");
        r1.run();
    }

    public static long sequentialSum(long n) {
        return java.util.stream.Stream.iterate(1L, i -> i + 1).limit(n).reduce(0L, Long::sum);
    }

    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 0L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public static long parallelSum(long n) {
        return java.util.stream.Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
    }

    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1,n).reduce(0L,Long::sum);
    }

    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1,n).parallel().reduce(0L,Long::sum);
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1,n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

}
