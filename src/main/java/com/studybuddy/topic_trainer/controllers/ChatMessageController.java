package com.studybuddy.topic_trainer.controllers;

import com.studybuddy.topic_trainer.services.ChatMessageService;
import org.springframework.ai.chat.messages.Message;
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
    public ResponseEntity<Message> post(@RequestParam Long topicId, @RequestBody String text) {
        return ResponseEntity.ofNullable(chatMessageService.create(topicId, text));
    }

    @GetMapping
    public ResponseEntity<Iterable<Message>> getByTopicId(@RequestParam Long topicId) {
        return ResponseEntity.ofNullable(chatMessageService.retrieveByTopicId(topicId));
    }
}
