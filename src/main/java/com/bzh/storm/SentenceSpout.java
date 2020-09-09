package com.bzh.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import sun.plugin.com.Utils;

import java.util.Map;

public class SentenceSpout extends BaseRichSpout{

    private SpoutOutputCollector collector;

    private String [] sentences = {
            "my dog has fleas",
            "i like cold bevarages",
            "the dog ate my homework",
            "don't have a cow man",
            "i don't think i i like fleas"
    };

    private int index = 0;

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;

    }

    @Override
    public void nextTuple() {
        /*this.collector.emit(new Values(sentences[index]));
        index++;
        if (index >= sentences.length) {
            index = 0;
        }*/

        if (index < sentences.length) {
            this.collector.emit(new Values(sentences[index]));
            index++;
        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}
