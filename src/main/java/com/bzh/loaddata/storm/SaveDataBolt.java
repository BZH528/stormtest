package com.bzh.loaddata.storm;

import com.bzh.loaddata.hive.FileToHDFS;
import com.bzh.loaddata.util.TableType;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SaveDataBolt extends BaseRichBolt{

    private OutputCollector outputCollector = null;
    private Logger logger = null;
    private FileToHDFS fileToHDFS = null;
    private String filePath = null;
    private FSDataOutputStream outputStream = null;
    private String baseUrl = null;
    private int maxCount = 2000;
    private String tableType = null;

    private int consumeCount = 0;
    private long lastSaveTime = 0l;

    private Properties dataflow;

    public SaveDataBolt(Properties dataflow) {
        this.dataflow = dataflow;
    }

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector outputCollector) {
        logger = LoggerFactory.getLogger(SaveDataBolt.class);
        this.outputCollector = outputCollector;
        this.fileToHDFS = new FileToHDFS(dataflow);
        if (dataflow != null) {
            this.baseUrl = dataflow.getProperty("data.storeUrl");
            this.maxCount = Integer.parseInt(dataflow.getProperty("data.split.maxCount", "2000"));
            this.tableType = dataflow.getProperty("data.type", "locate");
        } else {
            logger.error("fail to load dataflow.properties,please check it");
        }
    }

    @Override
    public void execute(Tuple tuple) {

        logger.info("the source queuename is:" + this.dataflow.get("mq.spout.queueName"));
        List<Object> values = tuple.getValues();
        List<String> fields = new ArrayList<>();

        this.decoradeFields(values, fields);

        this.consumeCount++;

        logger.info("the maxcount is :" + maxCount + ",the current count is : " + this.consumeCount);

        if (this.isNeedSaveNow()) {
            this.saveDataToHive();
        }

        FSDataOutputStream outputStream = this.getOutputStream();
        String join = String.join("\u0001", fields) + "\r\n";
        logger.info("join: " + join );

        try {
            outputStream.write(join.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            this.closeStream();
        }

        //刷写进文件
        this.flushData();

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public void cleanup() {
        super.cleanup();
        this.saveDataToHive();
        this.closeStream();
    }

    /**
     * 有些字段的顺序需要调整，这里统一处理一下
     * @param values 待处理的字段
     * @param fields 需要返回的字段列表
     */
    private void decoradeFields(List<Object> values, List<String> fields) {
        if (TableType.LOCATE.equalsIgnoreCase(this.tableType)) {
            // locate 数据
            for (int i = 0; i < values.size(); i++) {
                fields.add(values.get(i) + "");
                if (i == 5) {
                    this.addTimeFileds(fields);
                }
            }
        } else if (TableType.ALARM.equalsIgnoreCase(this.tableType)) {
            // alarm数据
            for (int i = 0; i < values.size(); i++) {
                fields.add(values.get(i) + "");
                if (i == 6) {
                    fields.add("");
                }
                if (i == 7) {
                    this.addTimeFileds(fields);
                }
            }
        } else {
            // follow数据
            for (int i = 0; i < values.size(); i++) {
                fields.add(values.get(i) + "");
                if (i == 5) {
                    fields.add("");
                }
                if (i == 6) {
                    this.addTimeFileds(fields);
                }
            }
        }

        if (filePath == null) {
            this.filePath = this.getFilePath();
        }
    }

    /**
     *  添加时间字段信息。
     * @param fileds 待处理的字段列表。
     */
    private void addTimeFileds(List<String> fileds) {
        Date date = new Date();
        String insert_date = new SimpleDateFormat("YYYY-MM-dd").format(date);
        String insert_time = new SimpleDateFormat("HH:mm:ss").format(date);
        fileds.add(insert_date);
        fileds.add(insert_time);
    }

    /**
     *  动态获取文件路径名，这个是为了方便生成的不同文件名，从而达到将文件写入不同的文件中
     *  要注意在多线程环境下文件名的处理
     * @return 文件名
     */
    private String getFilePath() {
        Date date = new Date();
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String partition = this.getPartitionAccordingTime();
        // 避免多个线程文件名相同，否则会导致多线程情况下只有一个线程能打开文件，其他线程会打开时抛出异常，另外还会有大量的小文件生成
        long id = Thread.currentThread().getId();

        return baseUrl + "/in_date=" + partition + "/" + time + id + ".txt";
    }

    /**
     *  获取表分区名
     * @return
     */
    private String getPartitionAccordingTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 判断是否立即保存
     * @return 返回是否需要保存
     */
    private boolean isNeedSaveNow() {
        if (this.lastSaveTime == 0 && this.consumeCount < this.maxCount) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        long minutes = (currentTime - lastSaveTime) / (1000 * 60);
        if (this.consumeCount >= maxCount) {
            return true;
        }
        double minCount = maxCount * 0.1;
        if (minutes >= 30 && this.consumeCount >= minCount) {
            return true;
        }
        return false;
    }

    /**
     * 保存数据到hdfs文件系统
     */
    private void saveDataToHive() {
        // 刷写进文件
        this.flushData();

        logger.info("the path to save file is :" + this.filePath);

        //及时将流关掉
        this.closeStream();

        //重新给表路径
        this.filePath = this.getFilePath();
        logger.info(filePath);

        //重新获取流
        this.outputStream = fileToHDFS.getOutputSteamFromHDFS(filePath);

        //判断条件复位
        this.consumeCount = 0;
        this.lastSaveTime = System.currentTimeMillis();
    }

    private void flushData() {
        if (outputStream != null) {
            try {
                this.outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeStream() {
        if (fileToHDFS != null) {
            fileToHDFS.close();
        }

        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private FSDataOutputStream getOutputStream() {
        if (outputStream == null) {
            //在获取新的输入流之前，把之前的流关掉，避免在多线程环境下生成大量的空文件
            fileToHDFS.close();

            this.outputStream = fileToHDFS.getOutputSteamFromHDFS(filePath);
        }

        return outputStream;
    }


}
