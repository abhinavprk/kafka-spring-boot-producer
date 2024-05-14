package org.kafka.producer.dto;

import lombok.Data;

@Data
public class Message<T> {

    private String topic;

    private T msg;
}
