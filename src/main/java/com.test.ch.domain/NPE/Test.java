package com.test.ch.domain.NPE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banmo.ch on 17/5/15.
 */
public class Test {
    public static void main(String[] args) {
        List<Long> whiteList = new ArrayList<Long>();
        System.out.println(whiteList.contains(1));
        System.out.println(whiteList.size());
    }
}
