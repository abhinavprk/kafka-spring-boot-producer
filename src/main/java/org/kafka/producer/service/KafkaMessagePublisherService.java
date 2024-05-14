package org.kafka.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.kafka.producer.dto.Customer;
import org.kafka.producer.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaMessagePublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaAdmin kafkaAdmin;

    @Autowired
    public KafkaMessagePublisherService(KafkaTemplate<String, Object> kafkaTemplate, KafkaAdmin kafkaAdmin) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaAdmin = kafkaAdmin;
        //This is not correct. Do this in a separate configuration file.
      //  kafkaTemplate.setDefaultTopic("test-topic");
    }

    public void sendStringMessageToTopic(Message<String> message) {
        CompletableFuture<SendResult<String, Object>> future;
        if (topicExists(message.getTopic())) {
            future = kafkaTemplate.send(message.getTopic(), message.getMsg());
        } else {
            log.error("Kafka topic: [{}] does not exist. Pushing to default topic: [ {}]",message.getTopic(),kafkaTemplate.getDefaultTopic());
            future = kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), message.getMsg());
        }
        future.whenComplete((result, ex) -> {
            if (Objects.isNull(ex)) {
                log.info("Sent message: [{} ] with offset: [{}] to topic:[ {} ].", message, result.getRecordMetadata().offset(), result.getRecordMetadata().topic());
            } else {
               log.error("Unable to send message due to [ {} ]",ex.getMessage());
            }
        });
    }

    private boolean topicExists(String topic) {
        //There is no topics in this kafka Admin
        return true;
    }

    public void sendCustomerMessageToTopic(Message<Customer> message) {
        try {
            CompletableFuture<SendResult<String, Object>> future;
            if (topicExists(message.getTopic())) {
                future = kafkaTemplate.send(message.getTopic(), message.getMsg());
            } else {
                log.info("Kafka topic: [{}] does not exist. Pushing to default topic: [ {} ]", message.getTopic(), kafkaTemplate.getDefaultTopic());
                future = kafkaTemplate.send(kafkaTemplate.getDefaultTopic(), message.getMsg());
            }
            future.whenComplete((result, ex) -> {
                if (Objects.isNull(ex)) {
                    log.info("Sent message: [ {} ] with offset: [ {} ] to topic:[ {} ].",
                            message.getMsg(),result.getRecordMetadata().offset(), result.getRecordMetadata().topic());
                } else {
                    log.error("Unable to send message due to [ {} ]", ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("ERROR:{}", e.getMessage());
        }
    }


}
