package org.kafka.producer.service;

import org.kafka.producer.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaAdmin kafkaAdmin;

    @Autowired
    public KafkaMessagePublisherService(KafkaTemplate<String, String> kafkaTemplate, KafkaAdmin kafkaAdmin) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaAdmin = kafkaAdmin;
        //This is not correct. Do this in a separate configuration file.
      //  kafkaTemplate.setDefaultTopic("test-topic");
    }

    public void sendMessageToTopic(Message message) {
        CompletableFuture<SendResult<String, String>> future;
        if (topicExists(message.getTopic())) {
            future = kafkaTemplate.send(message.getTopic(), message.getMsg());
        } else {
            System.out.println("Kafka topic: [" + message.getTopic() + " ] does not exist. Pushing to default topic: [ "
                    + kafkaTemplate.getDefaultTopic()
                    + " ]");
            future = kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), message.getMsg());
        }
        future.whenComplete((result, ex) -> {
            if (Objects.isNull(ex)) {
                System.out.println("Sent message: [ " + message + " ] with offset: [ " + result.getRecordMetadata().offset() + " ] to topic:[ " +
                        result.getRecordMetadata().topic() + " ].");
            } else {
                System.out.println("Unable to send message due to [ " + ex.getMessage() + " ]");
            }
        });
    }

    private boolean topicExists(String topic) {
        //There is no topics in this kafka Admin
        return true;
    }

}
