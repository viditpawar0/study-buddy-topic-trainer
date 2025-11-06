package com.studybuddy.topic_trainer.controllers;

import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.services.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("topics")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<Iterable<Topic>> post(@RequestParam Long chapterId, @RequestBody Iterable<Topic> topic) {
        return ResponseEntity.ofNullable(topicService.create(chapterId, topic));
    }

    @GetMapping("{id}")
    public ResponseEntity<Topic> get(@PathVariable Long id) {
        return ResponseEntity.of(topicService.retrieve(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<Topic>> getByChapterId(@RequestParam Long chapterId) {
        return ResponseEntity.ok(topicService.retrieveByChapterId(chapterId));
    }

    @PutMapping("{id}")
    public ResponseEntity<Topic> put(@RequestBody Topic topic, @PathVariable Long id) {
        return ResponseEntity.ofNullable(topicService.update(id, topic));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllByChapterId(@RequestParam Long chapterId) {
        topicService.deleteAllByChapterId(chapterId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{topicId}:start-training")
    public ResponseEntity<Void> startTraining(@PathVariable Long topicId) {
        topicService.startTraining(topicId);
        return ResponseEntity.ok().build();
    }
}
