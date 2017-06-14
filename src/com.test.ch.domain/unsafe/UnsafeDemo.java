package com.test.ch.domain.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static sun.misc.Unsafe.getUnsafe;

/**
 * Created by banmo.ch on 17/1/17.
 */
public class UnsafeDemo {
    public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe"); // Internal reference
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        // This creates an instance of player class without any initialization
        Player p = (Player) unsafe.allocateInstance(Player.class);
        System.out.println(p.getAge()); // Print 0

        p.setAge(45); // Let's now set age 45 to un-initialized object
        System.out.println(p.getAge()); // Print 45

        MainTest mainTest = (MainTest) unsafe.allocateInstance(MainTest.class);
        mainTest.execute();

    }
}

class Player {
    private int age = 12;

    private Player() {
        this.age = 50;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}