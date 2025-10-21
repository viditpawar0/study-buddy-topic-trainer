package com.studybuddy.topic_trainer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    @Column
    private Long chapterId;

    @Column
    private Status status;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "topic", fetch = FetchType.EAGER) //TODO: Fix fetch type to lazy (default).
    private List<ChatMessage> messages;

    public void addMessages(ChatMessage... messages) {
        for (final var message : messages) {
            message.setTopic(this);
            this.messages.add(message);
        }
    }

    public void removeMessages(ChatMessage... messages) {
        this.messages.removeAll(Arrays.asList(messages));
    }

    public void clearMessages() {
        this.messages.clear();
    }
}
