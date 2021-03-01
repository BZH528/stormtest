package com.bzh.algorithm.数据结构与算法分析第2版.chapter02;

public class Num03最大公约数 {

    public static void main(String[] args) {
        int m = 1989, n = 1590;
        int res;

        while (n > 0) {
            res = m % n;
            m = n;
            n = res;
        }
        System.out.println(m);
    }
}
