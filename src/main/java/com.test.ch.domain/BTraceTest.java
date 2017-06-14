package com.test.ch.domain;
import static com.sun.btrace.BTraceUtils.*;

import com.sun.btrace.annotations.*;
/**
 * Created by banmo.ch on 17/1/17.
 */
@BTrace
public class BTraceTest {
    @TLS
    private static long startTime = 0;

    @OnMethod(clazz = "com.test.ch.domain.test.MainTest", method = "execute")
    public static void startMethod(){
        startTime = timeMillis();
    }

    @OnMethod(clazz = "com.test.ch.domain.test.MainTest", method = "execute", location = @Location(Kind.RETURN))
    public static void endMethod(){
        println(strcat("the class method execute time=>", str(timeMillis()-startTime)));
        println("-------------------------------------------");
    }

    @OnMethod(clazz = "com.test.ch.domain.test.MainTest", method = "execute", location = @Location(Kind.RETURN))
    public static void traceExecute(@ProbeClassName String name,@ProbeMethodName String method,int sleepTime){
        println(strcat("the class name=>", name));
        println(strcat("the class method=>", method));
        println(strcat("the class method params=>", str(sleepTime)));

    }
}