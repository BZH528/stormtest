package com.bzh;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {
        Date date = new Date();
        String insert_date = new SimpleDateFormat("YYYY-MM-dd").format(date);
        String insert_time = new SimpleDateFormat("HH:mm:ss").format(date);
        System.out.println("datetime: " + insert_date + "\t" + insert_time);
    }
}
