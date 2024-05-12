package org.kafka.producer.dto;

import lombok.Data;

@Data
public class Message {

    private String topic;

    private String msg;
}
