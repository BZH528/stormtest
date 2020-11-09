package com.bzh;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestHashMap {

    @Test
    public void testMap () {
        HashMap<String, Long> map = new HashMap<>();
        map.put("a",1L);
        map.put("a",1L);
        System.out.println(map);

        String str = "\"粤L53687,2,114748885,22970015,2020-10-28 13:45:59,11,1,0,253,440000,441323,2,null,null,null\"";
        String resStr = str.substring(1, str.length() - 1);
        List<String> datas = Arrays.asList(resStr.split(","));
        ArrayList<Object> res = new ArrayList<>();
        res.addAll(datas);

        System.out.println("resSize: " + res.size());

        System.out.println("=================================");
        String s = "abcde";
        if (s.contains("a")) {
            System.out.println("s字符串包含a");
        } else if (s.contains("e")) {
            System.out.println("s字符串包含e");
        }
    }

}
