package com.test.ch.domain.calendar;

import java.util.Scanner;

/**
 * Created by banmo.ch on 17/5/3.
 */
public class CalendarTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter full year (e.g 2014): ");
        int year = input.nextInt();
        System.out.print("Enter month as number between 1 and 12 (e.g 12): ");
        int month = input.nextInt();
        //打印用户输入的年月份的日历
        printMonth(year, month);
    }

    //打印月份
    public static void printMonth(int year, int month) {
        printMonthTile(year, month);
        printMonthBody(year, month);
    }

    //打印月份的标题
    public static void printMonthTile(int year, int month) {
        System.out.println("\t" + getMonthName(month) + "  " + year);
        System.out.println("------------------------------");
        System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
    }

    //打印月份的主体
    public static void printMonthBody(int year, int month) {
        int startDay = getStartDay(year, month);
        int numberOfDaysInMonth = getNumberOfDaysInMonth(year, month);
        int i = 0;
        for (i = 0; i < startDay; i++) { System.out.print("    "); }
        for (i = 1; i <= numberOfDaysInMonth; i++) {
            System.out.printf("%4d", i);
            if ((i + startDay) % 7 == 0) { System.out.println(); }
        }
        System.out.println();
    }

    //得到月份对应的英文字符串
    public static String getMonthName(int month) {
        String monthName = "";
        switch (month) {
            case 1:
                monthName = "January";
                break;
            case 2:
                monthName = "February";
                break;
            case 3:
                monthName = "March";
                break;
            case 4:
                monthName = "April";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "June";
                break;
            case 7:
                monthName = "July";
                break;
            case 8:
                monthName = "August";
                break;
            case 9:
                monthName = "September";
                break;
            case 10:
                monthName = "October";
                break;
            case 11:
                monthName = "November";
                break;
            case 12:
                monthName = "December";
                break;
        }
        return monthName;
    }

    //得到某个月第一天的星期数
    public static int getStartDay(int year, int month) {
        final int START_DAY_FOR_JAN_1_1800 = 3;
        //得到从1800，1，1到year，month，1之间的天数
        int totalNumberOfDays = getTotalNumberOfDays(year, month);
        //返回year，month，1的开始天
        return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
    }

    //得到年份之间的总天数
    public static int getTotalNumberOfDays(int year, int month) {
        int total = 0;
        //得到从1800到1/1/year的总天数
        for (int i = 1800; i < year; i++) {
            if (isLeapYear(i)) { total += 366; } else { total += 365; }
        }
        //将一月到日历月的天数加到总天数中去
        for (int i = 1; i < month; i++) { total += getNumberOfDaysInMonth(year, i); }
        return total;
    }

    //得到某个月的天数
    public static int getNumberOfDaysInMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 ||
            month == 8 || month == 10 || month == 12) { return 31; }
        if (month == 4 || month == 6 || month == 9 || month == 11) { return 30; }
        if (month == 2) { return isLeapYear(year) ? 29 : 28; }
        return 0;//如果输入了无效月份返回0
    }

    //判断某年是否是闰年
    public static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }
}
