package com.studybuddy.topic_trainer.entity_listeners;

import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.services.ChatMessageService;
import jakarta.persistence.PreRemove;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TopicEntityListener {
    private final ChatMessageService chatMessageService;

    public TopicEntityListener(@Lazy ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PreRemove
    public void cascadeMessages(Topic topic) {
        chatMessageService.deleteByTopicId(topic.getId());
    }
}
