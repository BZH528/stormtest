package com.bzh;


import org.junit.Test;

import java.util.HashMap;

public class TestHashMap {

    @Test
    public void testMap () {
        HashMap<String, Long> map = new HashMap<>();
        map.put("a",1L);
        map.put("a",1L);
        System.out.println(map);
    }

}
