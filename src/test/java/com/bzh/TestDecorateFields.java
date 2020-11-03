package com.bzh;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestDecorateFields {

    @Test
    public void testDecorate() {
        /*String[] dataMsgs = {
                "\"E85CED9C96A2260F281C6F74FB35F98A,0,114202632,22997002,2020-10-28 13:52:10,30,0,269,440000,441302,2,null,null,null\"",
                "\"D3442D8711CE2EBD76D31A4EB8422C5C,0,114576610,23043960,2020-10-28 13:52:09,11,64,91,440000,441303,3,null,null,null\"",
                "\"E794B34DF53F6386E87224F47866DA0E,0,114744640,22644900,2020-10-28 13:52:00,12,0,100,440000,441323,786626,null,null,null\"",
                "\"4816F4E635BF85A323129A43EA5874E9,0,114287451,23694991,2020-10-28 13:52:15,11,0,58,440000,441324,2,null,null,null\"",
                "\"F016D7519322FE489A0FE5AB25002688,0,114195888,23160250,2020-10-28 13:54:15,11,86,250,440000,441322,786627,null,null,null\""
        };*/

        DateTimeFormatter fmTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();


        String str = "\"E85CED9C96A2260F281C6F74FB35F98A,0,114202632,22997002,2020-10-28 13:52:10,30,0,269,440000,441302,2,null,null,null\"";
        String[] split = str.split(",");
        List<String> list = Arrays.asList(split);
        list.set(4,localDateTime.format(fmTime));
        String join = String.join(",", list);
        System.out.println(join);


    }
}
