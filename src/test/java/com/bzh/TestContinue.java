package com.bzh;

import org.junit.Test;

public class TestContinue {

    @Test
    public void Test() {
        String str = "helloworld";

        for(int i = 0; i< str.length(); i++) {
            if (str.charAt(i) == 'l') {
                System.out.println("找到一个l");
                continue;
            }

            System.out.println("第" + i + "次循环结束");
        }
    }
}
