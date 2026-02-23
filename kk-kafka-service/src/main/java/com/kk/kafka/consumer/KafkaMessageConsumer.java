package com.kk.kafka.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageConsumer {

    @Value("${app.kafka.topic-name:sample-topic}")
    private String topicName;

    @KafkaListener(topics = "${app.kafka.topic-name:sample-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
        System.out.println("From topic: " + topicName);
    }


}

