package org.kafka.producer.dto;

import lombok.Data;

@Data
public class Topic {

    private String name;
    private int numPartitions;
    private short replicationFactor;
}
