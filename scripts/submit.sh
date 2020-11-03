# 测试集群提交拓扑任务
# 项目位置1.221:/home/software/bigdata/storm/project/highway
storm jar stormtest-1.0-SNAPSHOT.jar com.bzh.loaddata.storm.RbmqMain 4413/4413_locate_conf/dataflow.properties 4413_locate_v1_0

# 关闭拓扑任务
storm kill 4413_locate_v1_0

