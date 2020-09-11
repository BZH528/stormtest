package com.bzh.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import sun.plugin.com.Utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SentenceSpout extends BaseRichSpout{

    /**
     * 支持可靠的tuple发射方式，需要记录所有的发送的tuple，并且分配一个唯一的id
     * 使用HashMap<UUID,Values>来存储以发送待确认的tuple
     */
    private ConcurrentHashMap<UUID, Values> pending;
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
        this.pending = new ConcurrentHashMap<>();
    }

    @Override
    public void nextTuple() {


        /*this.collector.emit(values,msgId);
        index++;
        if (index >= sentences.length) {
            index = 0;
        }*/

        if (index < sentences.length) {
            Values values = new Values(sentences[index]);
            UUID msgId = UUID.randomUUID();
            this.pending.put(msgId, values);
            this.collector.emit(values,msgId);
            index++;
        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }

    /**
     * 增强SentenceSpout类，支持可靠的tuple发射方式
     * 当收到一个确认的消息，从待确定的列表中删除该tuple
     *
     * @param msgId
     */
    @Override
    public void ack(Object msgId) {
        this.pending.remove(msgId);
    }

    /**
     * 如果收到报错，重新发送tuple
     *
     * @param msgId
     */
    @Override
    public void fail(Object msgId) {
        this.collector.emit(this.pending.get(msgId),msgId);
    }
}
