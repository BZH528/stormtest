package com.bzh;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

public class TestWrite2HDFS {

    /**
     * 测试写入hdfs方法
     * @throws IOException
     */
    @Test
    public void write2HDFSTest() throws IOException {
        // resources目录下加入core-site.xml,hdfs-site.xml文件
        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "root");
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("/writehdfs/a.txt");
        FSDataOutputStream outputStream = null;

        if (!fs.exists(path)) {
            outputStream = fs.create(path,false);
        } else {
            // 避免写多个数据被覆盖
            outputStream = fs.append(path);
        }
        String str1 = "aaa" + "\r\n";
        outputStream.write(str1.getBytes("utf-8"));
        String str2 = "bbb" + "\r\n";
        outputStream.write(str2.getBytes("utf-8"));
        String str3 = "ccc" + "\r\n";
        outputStream.write(str3.getBytes("utf-8"));
        String str4 = "车辆" + "\r\n";
        outputStream.write(str4.getBytes("utf-8"));
        outputStream.flush();
        outputStream.close();
    }
}
