package com.studybuddy.topic_trainer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Collections;
import java.util.Map;

@Entity
@Data
public class ChatMessage implements Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private MessageType messageType;

    @Column(columnDefinition = "text")
    private String text;

    @ManyToOne
    private Topic topic;

    @Override
    @JsonIgnore
    public Map<String, Object> getMetadata() {
        return Collections.EMPTY_MAP; //TODO: To be implemented
    }
}
