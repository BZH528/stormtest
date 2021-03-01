package com.bzh.algorithm.数据结构与算法分析第2版.chapter02;

public class Num02二分查找 {

    /**
     * 时间复杂服 O(logN)
     * @param nums 为顺序递增排序的列表
     * @param x 需要查找的元素
     * @return  查找元素的下标
     */
    public static int binarySerarch(int[] nums,int x) {
        int left = 0, right = nums.length - 1;
        int mid = 0;
        while (left <= right) {
            mid = (left + right)/2;

            if (nums[mid] == x) {
                return mid;
            } else if (nums[mid] > x) {
               right = mid -1;
            } else {
                left = mid + 1;
            }


        }

        return  -1;
    }

    public static void main(String[] args) {

        int[] nums = {1,2,3};

        System.out.println(binarySerarch(nums,6));
    }
}
