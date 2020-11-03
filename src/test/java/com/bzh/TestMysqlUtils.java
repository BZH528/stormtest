package com.bzh;

import com.bzh.loaddata.util.MysqlUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestMysqlUtils {

    @Test
    public void TestMysqlUtils () {
        List<String> logintitudes = new ArrayList<String>();
        List<String> latitudes = new ArrayList<String>();
        String sqlfile_path = "conf/beh.sql";

        MysqlUtils.getHighWayInfoFromMysql(sqlfile_path, logintitudes, latitudes);
//        System.out.println(logintitudes);
        System.out.println("logintitudes size:" + logintitudes.size());
        System.out.println("===============================");
//        System.out.println(latitudes);
        System.out.println("latitudes size:" + latitudes.size());
    }
}
