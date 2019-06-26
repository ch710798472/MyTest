package com.test.ch.domain.java8;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author banmo
 * @create 2019-06-26
 **/
public class MainTest {
    /** 列表单层遍历修饰符 */
    private static final Function<Function, Consumer<List>>
        traversalListF = f -> l -> IntStream.range(0, l.size()).forEach(i -> l.set(i, f.apply(l.get(i))));
    /** 前置调用修饰符 */
    public static final Function<Function, Function<Consumer, Function>>
        beforeF = f -> c -> o -> { c.accept(o); return f.apply(o); };
    /** 函数幺元 */
    private static final Function identity = o -> o;
    /** 后置调用修饰符 */
    public static final Function<Function, Function<Consumer, Function>>
        afterF = f -> c -> o -> { Object oo = f.apply(o); c.accept(oo); return oo; };
    /** 映射单层遍历修饰符 */
    private static final Function<Function, Consumer<Map>>
        traversalMapF = f -> m -> m.forEach((k,v) -> m.put(k, f.apply(v)));
    /** 集合单层遍历修饰符 */
    private static final Function<Function, Consumer<Set>>
        traversalSetF = f -> s -> {
        List l = new ArrayList(s);
        traversalListF.apply(f).accept(l);
        s.clear();
        s.addAll(l);
    };
    /** 反射修改对象属性 */
    private static final Function<Function, Function<Object, Consumer<Field>>>
        modifyF = f -> o -> field -> {
        try {
            field.setAccessible(true);
            field.set(o, f.apply(field.get(o)));
        } catch (IllegalArgumentException | IllegalAccessException e){ /* pass.accept(o) */ }
    };
    /** 谓词: 属性在对象中是否static修饰 */
    private static final Predicate<Field> isStaticField = field -> Modifier.isStatic(field.getModifiers());
    /** 对象单层遍历器 */
    private static final Function<Function, Consumer>
        traversalObjectF = f -> o -> Stream.of(o.getClass().getDeclaredFields()).forEach(
        field -> {
            if (!isStaticField.test(field)) {
                modifyF.apply(f).apply(o).accept(field);
            }
        });
    /** 谓词匹配修饰符 */
    public static final Function<Predicate, Function<Function, Function>>
        matchingF = p -> f -> o -> p.test(o) ? f.apply(o) : o; /* identity.apply(o) */
    /** 类型匹配修饰符 */
    public static final Function<Class, Function<Function, Function>>
        typeF = clazz -> matchingF.apply(o -> o.getClass().equals(clazz));

    /** 谓词: 对象是否为null */
    private static final Predicate isNull = Objects::isNull; /* o -> o == null */
    /** 谓词: 反射判断对象的类型是否final修饰 */
    private static final Predicate isFinalClass = o -> Modifier.isFinal(o.getClass().getModifiers());
    /** 通用对象的遍历修饰符 */
    private static final Function<Function, Function>
        traversalF = f -> o ->
        isNull.test(o) ? o : /* identity.apply(o) */
            isFinalClass.test(o) ?  f.apply(o) :
                o; /* identity.apply(o) */

    /** 单层遍历器字典 */
    private static final Map<Class, Function> traversalFMap = new HashMap<>();
    static {
        traversalFMap.put(List.class, traversalListF);
        traversalFMap.put(Map.class, traversalMapF);
        traversalFMap.put(Set.class, traversalSetF);
    }
    private static final Function defaultTraversalF = traversalObjectF;
    /** 获取实现已知接口的遍历修饰符 */
    private static final Function<Object, Function<Function, Consumer>>
        getTraversalF = o -> {
        Optional<Class> op = traversalFMap.keySet().stream().filter(clazz -> clazz.isAssignableFrom(o.getClass())).findAny();
        return op.isPresent() ? traversalFMap.get(op.get()) : defaultTraversalF;
    };
    /** 浅遍历 */
    public static final Consumer simpleTraversal (Function f) {
        return o -> getTraversalF.apply(o).apply(traversalF.apply(f)).accept(o);
    }

    /** 深度遍历修饰(双递归) */
    private static final Function<Function, Function>
        depthTraversalF = f -> o ->
        isNull.test(o)       ?  o : /* identity.apply(o) */
            isFinalClass.test(o) ?  f.apply(o) :
                beforeF.apply(identity).apply(fullTraversal(f)).apply(o);

    /** 全遍历 */
    public static final Consumer fullTraversal (Function f) {
        return o -> getTraversalF.apply(o).apply(depthTraversalF.apply(f)).accept(o);
    }

    /** 列表单层遍历器(带追踪器) */
    private static final BiFunction<BiFunction, BiFunction, BiConsumer<List, Object>>
        traversalListBF = (f, trace) -> (l, queue) -> IntStream.range(0, l.size()).forEach(i -> l.set(i, f.apply(l.get(i), queue)));

    /** 集合单层遍历器(带追踪器) */
    private static final BiFunction<BiFunction, BiFunction, BiConsumer<Set, Object>>
        traversalSetBF = (f, trace) -> (s, queue) -> {
        List l = new ArrayList(s);
        traversalListBF.apply(f, trace).accept(l, queue);
        s.clear();
        s.addAll(l);
    };

    /** 映射单层遍历器(带追踪器) */
    private static final BiFunction<BiFunction, BiFunction, BiConsumer<Map, Object>>
        traversalMapBF = (f, trace) -> (m, queue) -> m.forEach((k,v) -> m.put(k, f.apply(v, trace.apply(k, queue))));

    /** 反射修改对象属性(追踪属性) */
    private static final BiFunction<BiFunction, BiFunction, BiFunction<Object, Object, Consumer<Field>>>
        modifyBF = (f, trace) -> (o, queue) -> field -> {
        try {
            field.setAccessible(true);
            field.set(o, f.apply(field.get(o), trace.apply(field, queue)));
        } catch (IllegalArgumentException | IllegalAccessException e){ /* biPass.accept(o, queue) */ }
    };

    /** 对象单层遍历器(带追踪器) */
    public static final BiFunction<BiFunction, BiFunction, BiConsumer>
        traversalObjectBF = (f, trace) -> (o, queue) -> Stream.of(o.getClass().getDeclaredFields()).forEach(
        field -> {
            if (isStaticField.test(field))  ; /* pass.accept(field) */
            else                            modifyBF.apply(f, trace).apply(o, queue).accept(field);
        });

    /** 单层带追踪器遍历器字典 */
    private static final Map<Class, BiFunction> traversalBFMap = new HashMap<>();
    private static final BiFunction defaultTraversalBF = traversalObjectBF;
    static {
        traversalBFMap.put(List.class, traversalListBF);
        traversalBFMap.put(Map.class, traversalMapBF);
        traversalBFMap.put(Set.class, traversalSetBF);
        //traversalBFMap.put(Object.class, traversalObjectBF);
    }
    private static final Function<Object, BiFunction<BiFunction, BiFunction, BiConsumer>>
        getTraversalBF = o -> {
        Optional<Class> op = traversalBFMap.keySet().stream().filter(clazz -> clazz.isAssignableFrom(o.getClass())).findAny();
        return op.isPresent() ? traversalBFMap.get(op.get()) : defaultTraversalBF;
    };

    private static final BiFunction biIdentity = (o1, o2) -> o1;

    public static final Function<BiFunction, Function<BiConsumer, BiFunction>>
        beforeBF = f -> c -> (o, queue) -> { c.accept(o, queue); return f.apply(o, queue); };

    private static final BiFunction<BiFunction, BiFunction, BiFunction>
        depthTraversalBF = (f, trace) -> (o, queue) ->
        isNull.test(o)       ?  o : /* biIdentity.apply(o, queue) */
            isFinalClass.test(o) ?  f.apply(o, queue) :
                beforeBF.apply(biIdentity).apply(traceTraversal(f, trace)).apply(o, queue);

    /** 带追踪器的遍历 */
    public static final BiConsumer traceTraversal (BiFunction f, BiFunction trace) {
        return (o, queue) -> getTraversalBF.apply(o).apply(depthTraversalBF.apply(f, trace), trace).accept(o, queue);
    }

    // 正确的写法
    BiFunction<Object, List<String>, List<String>> queueTrace = (unknown, lastQueue) -> {
        List<String> lastQueue_ = new ArrayList<>();
        lastQueue_.addAll(lastQueue);
        if(unknown.getClass().equals(Field.class))  lastQueue_.add(((Field)unknown).getName());
        else                                        lastQueue_.add(unknown.toString());
        return lastQueue_;
    };

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3);
        traversalListF.apply(beforeF.apply(identity).apply(System.out::println)).accept(list);

        traversalListF.apply(afterF.apply(beforeF.apply(x -> (int)x+1 ).apply(System.out::println)).apply(System.out::println)).accept(list);

        Map<String, Integer> map = new HashMap();
        map.put("first", 1);
        map.put("second", 2);
        map.put("third", 3);
        traversalMapF.apply(beforeF.apply(identity).apply(System.out::println)).accept(map);

        Set<Integer> set = new HashSet();
        set.add(1);
        set.add(2);
        set.add(3);
        traversalSetF.apply(x -> 1).accept(set);
        set.forEach(System.out::println);

        //测试
        Person myDaughter = new Person();
        myDaughter.setName("Xinyu");
        myDaughter.setEnglishName("Alice");
        myDaughter.setAge(1L);
        myDaughter.setSex(true);

        traversalObjectF.apply(beforeF.apply(identity).apply(System.out::println)).accept(myDaughter);
        traversalObjectF.apply(afterF.apply(typeF.apply(String.class).apply(x -> "Hello, " + x)).apply(System.out::println)).accept(myDaughter);

        simpleTraversal(beforeF.apply(identity).apply(System.out::println)).accept("Hello, world!");

        BiFunction pathTrace = (unknown, lastPath) -> unknown.getClass().equals(Field.class) ? lastPath + "/" + ((Field)unknown).getName() : lastPath + "/" + unknown.toString();
        BiConsumer printPath = (o, path) -> System.out.println(path + " = " + o);
        traceTraversal(beforeBF.apply(biIdentity).apply(printPath), pathTrace).accept(myDaughter, ""); // 传入String的幺元做为初始路径
    }

}
