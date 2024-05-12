package org.kafka.producer.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.kafka.producer.dto.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final KafkaAdmin kafkaAdmin;

    @Autowired
    public TopicService(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }


    public void createTopic(Topic topic) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(topic.getName(), topic.getNumPartitions(), topic.getReplicationFactor()));
    }
}
