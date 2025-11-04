package com.studybuddy.topic_trainer.entities;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Collections;
import java.util.Map;

@Data
public class ChatMessage implements Message {
    private MessageType messageType;
    private String text;
    @Override
    public Map<String, Object> getMetadata() {
        return Collections.EMPTY_MAP; //TODO: To be implemented
    }
}
