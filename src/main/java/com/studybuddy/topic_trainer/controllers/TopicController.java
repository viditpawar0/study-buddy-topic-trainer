package com.studybuddy.topic_trainer.controllers;

import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.services.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<Topic> post(@RequestBody Topic topic) {
        System.out.println(topic.toString());
        return ResponseEntity.ofNullable(topicService.create(topic));
    }

    @GetMapping("{id}")
    public ResponseEntity<Topic> get(@PathVariable Long id) {
        return ResponseEntity.of(topicService.retrieve(id));
    }

    @GetMapping
    public ResponseEntity<List<Topic>> getByChapterId(@RequestParam Long chapterId) {
        return ResponseEntity.ok(topicService.retrieveByChapterId(chapterId));
    }

    @PutMapping
    public ResponseEntity<Topic> put(@RequestBody Topic topic) {
        return ResponseEntity.ofNullable(topicService.update(topic));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody Topic topic) {
        topicService.delete(topic);
        return ResponseEntity.ok().build();
    }
}
