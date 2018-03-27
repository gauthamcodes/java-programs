package com.gautham.twitterstream;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;

public class TwitterConsumer {
	public void consume()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:8080");
        props.put("group.id", "group1");
        // to read messages from the beginning
        // first time for every group
        props.put("auto.offset.reset", "smallest");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);

        try {
            Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
            topicCountMap.put("topic1", 1);
            Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
            List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(this.topic);
            if (streams.size() > 0)
            {
                readMessages(streams.get(0));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
