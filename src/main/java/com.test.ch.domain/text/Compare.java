package com.test.ch.domain.text;

import com.sun.tools.javac.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Compare {
    public static void main(String[] args) {
        String str1 = "513,587";
        String str2
            = "203,331,385";

        List<String> str1Array = new ArrayList<String>();
        str1Array.addAll(Arrays.asList(str1.split(",")));
        List<String> str2Array = new ArrayList<String>();
        str2Array.addAll(Arrays.asList(str2.split(",")));
        List<String> sameResult = new ArrayList<String>();
        Iterator iterator2 = str2Array.iterator();

        while(iterator2.hasNext()){
            String tmp2 = (String)iterator2.next();
            Iterator iterator1 = str1Array.iterator();
            while(iterator1.hasNext()){
                String tmp1 = (String)iterator1.next();
                if (tmp2.equals(tmp1)) {
                    sameResult.add(tmp1);
                    iterator1.remove();
                    iterator2.remove();
                }
            }
        }

        System.out.println("str1Array: " + Arrays.toString(str1Array.toArray()));
        System.out.println("str2Array: " + Arrays.toString(str2Array.toArray()));
        System.out.println("sameResult: " + Arrays.toString(sameResult.toArray()));
    }
}
