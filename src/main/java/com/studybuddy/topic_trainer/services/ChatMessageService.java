package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.entities.Status;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private final TopicService topicService;
    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;

    public ChatMessageService(TopicService topicService,
                              ChatModel chatModel,
                              JdbcChatMemoryRepository chatMemoryRepository) {
        this.topicService = topicService;
        this.chatClient = ChatClient.create(chatModel);
        this.chatMemoryRepository = chatMemoryRepository;
    }

    public Message create(Long topicId, String text) {
        final var topic = topicService.assertEntityExists(topicId);
        topic.setStatus(Status.GENERATING);
        topicService.update(topicId, topic);
        final ChatMessage assistantChatMessage;
        try {
            final var messages = chatMemoryRepository.findByConversationId(topicId.toString());
            final var userChatMessage = new ChatMessage();
            userChatMessage.setMessageType(MessageType.USER);
            userChatMessage.setText(text);
            messages.add(userChatMessage);
            chatMemoryRepository.saveAll(topicId.toString(), messages);
            assistantChatMessage = new ChatMessage();
            assistantChatMessage.setMessageType(MessageType.ASSISTANT);
            assistantChatMessage.setText(chatClient.prompt(
                    Prompt.builder()
                            .messages(chatMemoryRepository.findByConversationId(topicId.toString()))
                            .build()
            ).stream().content().collectList().block().stream().collect(Collectors.joining()));
            messages.add(assistantChatMessage);
            chatMemoryRepository.saveAll(topicId.toString(), messages);
            topic.setStatus(Status.READY);
        } catch (Exception e) {
            topic.setStatus(Status.FAILED);
            throw new RuntimeException(e);
        } finally {
            topicService.update(topicId, topic);
        }
        return assistantChatMessage;
    }

    public Iterable<Message> retrieveByTopicId(Long topicId) {
        topicService.assertEntityExists(topicId);
        final var messages = chatMemoryRepository.findByConversationId(topicId.toString());
        messages.removeFirst();
        messages.removeFirst();
        return messages;
    }

    public void deleteByTopicId(Long topicId) {
        topicService.assertEntityExists(topicId);
        chatMemoryRepository.deleteByConversationId(topicId.toString());
    }
}
