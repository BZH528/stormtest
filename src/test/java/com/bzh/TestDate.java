package com.bzh;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {
        Date date = new Date();
        String insert_date = new SimpleDateFormat("YYYY-MM-dd").format(date);
        String insert_time = new SimpleDateFormat("HH:mm:ss").format(date);
        System.out.println("datetime: " + insert_date + "\t" + insert_time);

        Date date2 = new Date();
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(date2);
        System.out.println(time);

        Date date3 = new Date();
        String time3 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(date3);
        System.out.println(time3);

        String url = "123478.txt";
        int i = url.indexOf(".txt");
        System.out.println(i);
        int length = url.length();
        System.out.println("length:" + length);
    }
}
