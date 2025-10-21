package com.studybuddy.topic_trainer.controllers;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.services.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("messages")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ChatMessage> post(@RequestParam Long topicId, @RequestBody String text) {
        return ResponseEntity.ofNullable(chatMessageService.create(topicId, text));
    }

    @GetMapping("{id}")
    public ResponseEntity<ChatMessage> get(@PathVariable Long id) {
        return ResponseEntity.of(chatMessageService.retrieve(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<ChatMessage>> getByTopicId(@RequestParam Long topicId) {
        return ResponseEntity.ofNullable(chatMessageService.retrieveByTopicId(topicId));
    }
}
