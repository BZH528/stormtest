package com.bzh.loaddata.storm;

import com.bzh.loaddata.bean.DataSinkInfo;
import com.bzh.loaddata.util.LocateUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LocateDataFilter implements IRichBolt {

    private OutputCollector outputCollector;

    private List<DataSinkInfo> dataSinkInfos;

    private Logger logger;

    private final String[] outputFields;


    public LocateDataFilter(List<DataSinkInfo> dataSinkInfos, String[] outputFields) {
        this.dataSinkInfos = dataSinkInfos;
        this.outputFields = outputFields;
    }

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
        this.logger = LoggerFactory.getLogger(LocateDataFilter.class);
    }

    @Override
    public void execute(Tuple tuple) {
        String longtitude = tuple.getStringByField("Longtitude");
        String latitude = tuple.getStringByField("Latitude");

        List<Object> fields = tuple.getValues();
        Iterator<DataSinkInfo> iterator = dataSinkInfos.iterator();

        //提取数据，一个dataSinkInfo对象只支持一种去向，避免混乱
        while (iterator.hasNext()) {
            DataSinkInfo datainfo = iterator.next();
            logger.info("to alalyze highway：" + datainfo.getSectionName());

            if (datainfo.isExtract_all()) {
                //是否提取所有数据到某个队列
                this.outputCollector.emit(datainfo.getStreamName(), fields);
            } else if (isInHighWay(longtitude, latitude, datainfo.getLongitudes(), datainfo.getLatitudes(), datainfo.getCompute_distance())) {
                //按照某种规则提取数据
                this.outputCollector.emit(datainfo.getStreamName(), fields);
            }
        }

        this.outputCollector.emit("common", fields);
        this.outputCollector.ack(tuple);
    }

    //判断是否是在高速路上
    private boolean isInHighWay(String target_longtitude, String target_latitude, List<String> longitudes, List<String> latitudes, double max_distance) {
        return LocateUtils.isInHighWayAccordLocate(target_longtitude, target_latitude, longitudes, latitudes);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        String[] fields = this.outputFields;
        Iterator<DataSinkInfo> iterator = this.dataSinkInfos.iterator();
        while (iterator.hasNext()) {
            DataSinkInfo datainfo = iterator.next();
            outputFieldsDeclarer.declareStream(datainfo.getStreamName(), new Fields(fields));
        }
        outputFieldsDeclarer.declareStream("common", new Fields(fields));


    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
