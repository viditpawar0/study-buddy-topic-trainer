package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.entities.Status;
import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.feign_clients.ChapterFeignClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.studybuddy.topic_trainer.utils.Utils.getInitialSystemMessage;

@Service
public class TopicInitiationService {
    private final TopicService topicService;
    private final ChapterFeignClient chapterFeignClient;
    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;

    public TopicInitiationService(TopicService topicService,
                                  ChapterFeignClient chapterFeignClient,
                                  ChatClient chatClient,
                                  JdbcChatMemoryRepository chatMemoryRepository) {
        this.topicService = topicService;
        this.chapterFeignClient = chapterFeignClient;
        this.chatClient = chatClient;
        this.chatMemoryRepository = chatMemoryRepository;
    }

    @Async
    public void initializeTopicAsync(Topic topic) {
        topic.setStatus(Status.INITIALIZING);
        topicService.update(topic.getId(), topic);
        try {
            final var chapter = chapterFeignClient.get(topic.getChapterId());
            final var subject = chapter.subject();
            final var topicName = topic.getName();
            final var chapterName = chapter.name();
            final var subjectName = subject.name();
            final var systemMessage = getInitialSystemMessage(topicName, chapterName, subjectName);
            final var initialUserMessage = new ChatMessage();
            initialUserMessage.setMessageType(MessageType.USER);
            initialUserMessage.setText("Hello");
            final var assistantMessage = new ChatMessage();
            assistantMessage.setMessageType(MessageType.ASSISTANT);
            assistantMessage.setText(chatClient
                    .prompt()
                    .messages(systemMessage, initialUserMessage)
                    .stream().content().collectList().block().stream().collect(Collectors.joining()));
            chatMemoryRepository.saveAll(topic.getId().toString(), List.of(systemMessage, initialUserMessage, assistantMessage));
            topic.setStatus(Status.READY);
        } catch (Exception e) {
            topic.setStatus(Status.FAILED);
            throw new RuntimeException(e);
        } finally {
            topicService.update(topic.getId(), topic);
        }
    }
}
