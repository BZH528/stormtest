package com.bzh;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class TestHDFS {

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        try {
            FileSystem fileSystem = FileSystem.get(conf);

            FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/bizh"));
            System.out.println(fileStatuses[0].getLen());
            System.out.println(fileStatuses[0]);
            System.out.println(fileStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
