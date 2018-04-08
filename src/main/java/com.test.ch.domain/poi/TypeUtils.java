package com.test.ch.domain.poi;

public class TypeUtils {

    public static Long toLong(Object o) {
        if (null == o) {
            return null;
        }

        return Long.valueOf(String.valueOf(o));
    }

    public static Integer toInteger(Object o) {
        if (null == o) {
            return null;
        }

        return Integer.valueOf(String.valueOf(o));
    }

    public static Double toDouble(Object o) {
        if (null == o) {
            return null;
        }

        return Double.valueOf(String.valueOf(o));
    }

    public static String toString(Object o) {
        if (null == o) {
            return "";
        }

        return String.valueOf(o);
    }
}
