package com.test.ch.domain.java8;

import java.util.function.Function;

/**
 * Created by banmo.ch on 17/5/11.
 */
public class SumPref {
    public static void main(String[] args) {
        System.out.println("sequentialSum : " + measureSumPref(paralleStream::sequentialSum,10_000_000));
        System.out.println("iterativeSum : " + measureSumPref(paralleStream::iterativeSum,10_000_000));
        System.out.println("parallelSum : " + measureSumPref(paralleStream::parallelSum,10_000_000));

        System.out.println("rangedSum : " + measureSumPref(paralleStream::rangedSum,10_000_000));
        System.out.println("parallelRangedSum : " + measureSumPref(paralleStream::parallelRangedSum,10_000_000));
        System.out.println("forkJoinSum : " + measureSumPref(paralleStream::forkJoinSum,10_000_000));
    }

    public static long measureSumPref(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            //System.out.println("Result:" + sum);
            if (duration < fastest) { fastest = duration; }
        }
        return fastest;
    }
}
