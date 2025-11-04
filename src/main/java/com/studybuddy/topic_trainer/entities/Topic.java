package com.studybuddy.topic_trainer.entities;

import com.studybuddy.topic_trainer.entity_listeners.TopicEntityListener;
import jakarta.persistence.*;
import lombok.Data;

@EntityListeners(TopicEntityListener.class)
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
}
