package com.test.ch.domain.classloader;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * Created by banmo.ch on 17/4/1.
 */
public class Main {
    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name.substring(19,name.length())
                + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;

        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

    };

    public static void main(String args[]) throws Exception {
        MyClassLoader classLoader = new MyClassLoader(System.getProperty("user.dir")+"/test/src/com.test.ch.domain");
        Class clazz = classLoader.loadClass("com.test.ch.domain.classloader.test");
        Object obj = clazz.newInstance();
        Method helloMethod = clazz.getDeclaredMethod("hello", null);
        helloMethod.invoke(obj, null);
    }
}
