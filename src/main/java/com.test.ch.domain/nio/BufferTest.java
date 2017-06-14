package com.test.ch.domain.nio;

import java.nio.IntBuffer;

/**
 * Created by banmo.ch on 17/6/14.
 */
public class BufferTest {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(2);
        intBuffer.put(12345678);
        intBuffer.put(2);
        intBuffer.flip();
        System.err.println(intBuffer.get());
        System.err.println(intBuffer.get());
        understand3Attribute();
        resetPostion();
    }

    public static void understand3Attribute() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        intBuffer.put(10);
        intBuffer.put(101);
        System.err.println("Write mode: ");
        System.err.println("\tCapacity: " + intBuffer.capacity());
        System.err.println("\tPosition: " + intBuffer.position());
        System.err.println("\tLimit: " + intBuffer.limit());

        intBuffer.flip();
        System.err.println("Read mode: ");
        System.err.println("\tCapacity: " + intBuffer.capacity());
        System.err.println("\tPosition: " + intBuffer.position());
        System.err.println("\tLimit: " + intBuffer.limit());
    }

    public static void resetPostion() {
        IntBuffer intBuffer = IntBuffer.allocate(2);
        intBuffer.put(1);
        intBuffer.put(2);
        intBuffer.flip();
        System.err.println(intBuffer.get());
        System.err.println("position: " + intBuffer.position());
        intBuffer.mark();
        System.err.println(intBuffer.get());

        System.err.println("position: " + intBuffer.position());
        intBuffer.reset();
        System.err.println("position: " + intBuffer.position());
        System.err.println(intBuffer.get());
    }
}
