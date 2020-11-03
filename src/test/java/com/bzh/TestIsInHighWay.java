package com.bzh;

import com.bzh.loaddata.util.LocateUtils;
import com.bzh.loaddata.util.MysqlUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestIsInHighWay {

    @Test
    public void testIsInHighWayAccordLocate() {
        String target_longtitude = "113518352";
        String target_latitude = "22751484";

        List<String> logintitudes = new ArrayList<String>();
        List<String> latitudes = new ArrayList<String>();
        String sqlfile_path = "conf/beh.sql";

        // 获取mysql表中业务数据
        MysqlUtils.getHighWayInfoFromMysql(sqlfile_path, logintitudes, latitudes);

        // 判断是否在高速路上
        boolean inHighWayAccordLocate = LocateUtils.isInHighWayAccordLocate(target_longtitude, target_latitude, logintitudes, latitudes);
        System.out.println(inHighWayAccordLocate);

    }
}
