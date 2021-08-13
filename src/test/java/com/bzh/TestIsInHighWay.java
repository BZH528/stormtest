package com.bzh;

import com.bzh.loaddata.util.LocateUtils;
import com.bzh.loaddata.util.MysqlUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestIsInHighWay {


    /**
     * 在高速上的经纬度坐标
     * 113479016,23212444
     */
    @Test
    public void testIsInHighWayAccordLocate() {
       /* //在南韶高速上
        String target_longtitude = "114167239";
        String target_latitude = "25030616";*/

       // 测试北二环beh 11348108023203660 这条在北二环上

        String target_longtitude = "111501984";
        String target_latitude = "23283350";

        List<String> logintitudes = new ArrayList<String>();
        List<String> latitudes = new ArrayList<String>();
        String sqlfile_path = "conf/huaiyang.sql";

        // 获取mysql表中业务数据
        MysqlUtils.getHighWayInfoFromMysql(sqlfile_path, logintitudes, latitudes);

        // 判断是否在高速路上
        boolean inHighWayAccordLocate = LocateUtils.isInHighWayAccordLocate(target_longtitude, target_latitude, logintitudes, latitudes);
        System.out.println(inHighWayAccordLocate);

    }
}
