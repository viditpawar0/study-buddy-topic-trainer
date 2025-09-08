package com.studybuddy.topic_trainer.repositories;

import com.studybuddy.topic_trainer.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByChapterId(Long chapterId);
}
