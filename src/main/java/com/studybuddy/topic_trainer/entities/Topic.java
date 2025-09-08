package com.studybuddy.topic_trainer.entities;

import jakarta.persistence.*;
import lombok.Data;

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
}
