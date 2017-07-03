package com.test.ch.domain.retry;

/**
 * Created by banmo.ch on 17/7/3.
 */
public class RetryUtil {
    public RetryUtil() {
    }

    public static boolean doRetry(RetryUtil.Retry retry, int times) {
        int time = getTimes(times);

        do {
            try {
                if (retry.exec(time)) {
                    return true;
                }
            } catch (Exception e) {
                if (time == 1) {
                    throw new RuntimeException(e);
                }
            }

            --time;
        }
        while (time > 0);

        return false;
    }

    private static int getTimes(int times) {
        return times <= 0 ? 0 : (times > 10 ? 10 : times);
    }

    public interface Retry {
        boolean exec(int var1);
    }

    public static void main(String[] args) {
        RetryUtil.doRetry(new RetryUtil.Retry() {
            @Override
            public boolean exec(int var1) {
                System.out.println(var1);
                return false;
            }
        }, 10);
    }
}
