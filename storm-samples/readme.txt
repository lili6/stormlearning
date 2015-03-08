2015-3-8
打包命令：
mvn clean install
本地模式启动
storm jar storm-samples-jar-with-dependencies.jar com.lili.ExclaimBasicTopo

本地模式也可以直接运行ExclaimBasicTopo在IDE里面进行运行，太棒了！
用来调试和测试功能。
这是个无限循环。。

集群模式：
storm jar storm-samples-jar-with-dependencies.jar com.lili.ExclaimBasicTopo exclaim


log4j-over-slf4j-1.6.6.jar:storm-samples-jar-with-dependencies.jar:/home/lili/env/apache-storm-0.9.1-incubating/conf:/home/lili/env/apache-storm-0.9.1-incubating/bin -Dstorm.jar=storm-samples-jar-with-dependencies.jar com.lili.ExclaimBasicTopo exclaim
3176 [main] INFO  backtype.storm.StormSubmitter - Jar not uploaded to master yet. Submitting jar...
3242 [main] INFO  backtype.storm.StormSubmitter - Uploading topology jar storm-samples-jar-with-dependencies.jar to assigned location: /home/lili/env/apache-storm-0.9.1-incubating/workdir/nimbus/inbox/stormjar-3d0c4179-c349-4664-ac81-1d8353274612.jar
3320 [main] INFO  backtype.storm.StormSubmitter - Successfully uploaded topology jar to assigned location: /home/lili/env/apache-storm-0.9.1-incubating/workdir/nimbus/inbox/stormjar-3d0c4179-c349-4664-ac81-1d8353274612.jar
3320 [main] INFO  backtype.storm.StormSubmitter - Submitting topology exclaim-test in distributed mode with conf {"topology.workers":3,"topology.debug":false}
4094 [main] INFO  backtype.storm.StormSubmitter - Finished submitting topology: exclaim
