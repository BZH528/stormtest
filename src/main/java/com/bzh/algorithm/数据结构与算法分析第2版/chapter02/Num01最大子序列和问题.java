package com.bzh.algorithm.数据结构与算法分析第2版.chapter02;


public class Num01最大子序列和问题 {


    /**
     * 算法1
     * 时间复杂度 O(n^2)
     * @param nums
     * @return
     */
    public static int f(int[] nums) {
        int n = nums.length;
        int max = 0;
        for (int i = 0; i < n; i++) {
            int currentMax = 0;
            for (int j = i ; j < n; j++) {
                currentMax += nums[j];

                if (currentMax >= max) {
                    max = currentMax;
                }
            }
        }

        return max;

    }

    /**
     * 算法2
     * 时间复杂度 O(n)
     * @param nums
     * @return
     */
    public static int f2(int[] nums ) {
        int n = nums.length;
        int currentMax=0,max = 0;
        for (int i = 0; i < n; i++) {
            currentMax += nums[i];
            if (currentMax > max) {
                max = currentMax;
            } else if (currentMax < 0) {
                currentMax = 0;
            }
        }

        return max;
    }

    public static void main(String[] args) {

        // 如果所有整数都是负数，则结果为0
        int[] nums = {-2, -11, -10, -13, -5, -2};

        System.out.println(f(nums));

        System.out.println(f2(nums));
    }
}
