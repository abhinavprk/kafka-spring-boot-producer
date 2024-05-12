package org.kafka.producer.controller;

import org.kafka.producer.dto.Topic;
import org.kafka.producer.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic/")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("new-topic")
    public ResponseEntity<String> createTopic(@RequestBody Topic topic){
        try {
            topicService.createTopic(topic);
            return new ResponseEntity<>("Topic Created", HttpStatus.OK);
        } catch (Exception _){
            return new ResponseEntity<>("Topic could not be created.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
