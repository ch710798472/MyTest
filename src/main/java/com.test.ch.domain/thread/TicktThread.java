package com.test.ch.domain.thread;

/**
 * Created by banmo.ch on 17/3/15.
 *
 * @author banmo.ch
 * @date 2017/03/15
 */
public class TicktThread {

    public static void main(String[] args) {
        Seller.sell(5);
        Seller.sell(10);
        Seller.sell(20);
    }

    private static class Seller {
        public volatile static int count = 3;

        public synchronized static void sell(int money) {
            if (money == 5) {
                System.out.println("get ticket");
                count++;
            }
            if (money == 20) {
                while (count != 3) {
                    System.out.println("wait money");
                }
                System.out.println("get ticket");
                count = count -3;
            }

            if (money == 10) {
                while (count < 1) {
                    System.out.println("wait money");
                }
                System.out.println("get ticket");
                count = count -1;
            }
        }
    }
}
