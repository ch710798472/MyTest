package com.test.ch.domain.java8;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import static java.util.stream.Collectors.*;

import java.util.stream.IntStream;

/**
 * Created by banmo.ch on 17/5/9.
 */
public class Stream {
    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, "meat"),
            new Dish("beef", false, 700, "meat"),
            new Dish("chicken", false, 400, "meat"),
            new Dish("french fries", true, 530, "other"),
            new Dish("rice", true, 350, "other"),
            new Dish("season", true, 120, "other"),
            new Dish("pizza", true, 550, "other"),
            new Dish("prawns", false, 300, "fish"),
            new Dish("salmon", false, 450, "fish")
        );
        List<String> treeHighCaloricDishNames = menu.stream()
            .filter(d -> {
                System.out.println("filtering " + d.getName());
                return d.getCalories() > 300;
            })
            .map(d -> {
                System.out.println("maping " + d.getName());
                return d.getName();
            })
            .limit(3)
            .collect(toList());
        System.out.println(treeHighCaloricDishNames);
        menu.stream().forEach(System.out::println);
        System.out.println(menu.stream().map(Dish::getName).map(String::length).collect(toList()));
        System.out.println(menu.stream().mapToInt(Dish::getCalories).sum());
        System.out.println(menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum)));
        System.out.println(menu.stream().mapToInt(Dish::getCalories).max().orElse(1));
        System.out.println(IntStream.range(1, 10).filter(p -> p % 2 == 0).count());
        java.util.stream.Stream.iterate(new int[] {0, 1}, t -> new int[] {t[1], t[0] + t[1]}).limit(20).forEach(
            t -> System
                .out
                .print("(" + t[0] + "," + t[1] + ")"));

        System.out.println("\r\n<<++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>>\r\n");
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
