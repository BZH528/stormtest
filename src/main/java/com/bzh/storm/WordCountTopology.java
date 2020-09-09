package com.bzh.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class WordCountTopology {

    private static final String SENTENCE_SPOUT_ID = "sentence-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String COUNT_BOLT_ID = "count-bolt";
    private static final String REPORT_BOLT_ID = "report-bolt";
    private static final String TOPOLOGY_NAME = "word-count-topology";

    public static void main(String[] args) throws Exception {

        long statr_time = System.currentTimeMillis();

        SentenceSpout spout = new SentenceSpout();
        SplitSentenceBolt splitBolt = new SplitSentenceBolt();
        WordCountBolt countBolt = new WordCountBolt();
        ReportBolt reportBolt = new ReportBolt();

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(SENTENCE_SPOUT_ID, spout,2);
        // SentenceSpout -> SplitSentenceBolt
        builder.setBolt(SPLIT_BOLT_ID, splitBolt,2)
                .setNumTasks(4)
                .shuffleGrouping(SENTENCE_SPOUT_ID);
        // SplitSentenceBolt -> WordCountBolt
//         builder.setBolt(COUNT_BOLT_ID, countBolt,4).fieldsGrouping(SPLIT_BOLT_ID, new Fields("word"));

        //在并发状况下，将countBolt从按字段分组修改为随机分组，结果不准确,前提countbotl引入的并发实例大于一个
        builder.setBolt(COUNT_BOLT_ID, countBolt,4).shuffleGrouping(SPLIT_BOLT_ID);

        // WordCountBolt -> ReportBolt
        builder.setBolt(REPORT_BOLT_ID, reportBolt).globalGrouping(COUNT_BOLT_ID);

        Config config = new Config();

        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology(TOPOLOGY_NAME, config, builder.createTopology());
        Thread.sleep(5 * 1000);
        localCluster.killTopology(TOPOLOGY_NAME);
        localCluster.shutdown();

        System.out.println("SPEND_TIME: " + (System.currentTimeMillis() - statr_time)/100 );

    }
}
