package com.test.ch.domain.java8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by banmo.ch on 17/5/4.
 */
public class BufferReaderTest {
    public static void main(String[] args) throws IOException {
        String oneLine = processFile((BufferedReader br) -> br.readLine());
        String twoLine = processFile((BufferedReader br) -> br.readLine() + "\r\n" + br.readLine());
        System.out.println(oneLine);
        System.out.println(twoLine);
    }

    public static String processFile(BufferReaderProcessor p) throws IOException {
        try (
            BufferedReader br = new BufferedReader(new FileReader("hs_err_pid88478.log"))) {
            return p.process(br);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
