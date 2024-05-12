package org.kafka.producer.controller;

import org.kafka.producer.dto.Message;
import org.kafka.producer.service.KafkaMessagePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer/")
public class EventController {

    private final KafkaMessagePublisherService kafkaMessagePublisherService;

    @Autowired
    public EventController(KafkaMessagePublisherService kafkaMessagePublisherService) {
        this.kafkaMessagePublisherService = kafkaMessagePublisherService;
    }

    @PostMapping("message")
    public ResponseEntity<String> publishMessage(@RequestBody Message message){
        try {
            kafkaMessagePublisherService.sendMessageToTopic(message);
            return new ResponseEntity<>("Message Successfully Published", HttpStatus.OK);
        } catch (Exception _){
            return new ResponseEntity<>("Not able to send message", HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
