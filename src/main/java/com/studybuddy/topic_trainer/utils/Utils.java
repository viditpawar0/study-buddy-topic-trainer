package com.studybuddy.topic_trainer.utils;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

public class Utils {
    public static <T, ID> T assertEntityExists(ID id, JpaRepository<T, ID> repository) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public static Message getInitialSystemMessage(String topicName, String chapterName, String subjectName) {
        var systemPrompt = "You are a helpful assistant for students studying " +
                "topic: " + topicName + ", chapter: " + chapterName + ", and subject: " + subjectName + ". " +
                "Provide clear and concise explanations, examples, and answers to their questions based on the topic content.";
        final var systemMessage = new ChatMessage();
        systemMessage.setMessageType(MessageType.SYSTEM);
        systemMessage.setText(systemPrompt);
        return systemMessage;
    }
}
