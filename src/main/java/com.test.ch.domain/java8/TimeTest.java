package com.test.ch.domain.java8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

/**
 * Created by banmo.ch on 17/5/18.
 */
public class TimeTest {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.of(2017,5,18);
        System.out.println(LocalDate.now());
        LocalTime localTime = LocalTime.parse("14:15:30");
        System.out.println(localTime.get(ChronoField.SECOND_OF_MINUTE));
    }
}
