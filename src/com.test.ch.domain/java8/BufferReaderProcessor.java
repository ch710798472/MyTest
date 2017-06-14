package com.test.ch.domain.java8;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by banmo.ch on 17/5/4.
 */
@FunctionalInterface
public interface BufferReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
