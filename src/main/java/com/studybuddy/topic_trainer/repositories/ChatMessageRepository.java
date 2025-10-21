package com.studybuddy.topic_trainer.repositories;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Iterable<ChatMessage> findByTopic_Id(Long topicId);
}
