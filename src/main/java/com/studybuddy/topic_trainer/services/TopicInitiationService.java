package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.entities.Status;
import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.feign_clients.ChapterFeignClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.studybuddy.topic_trainer.utils.Utils.getInitialSystemMessage;

@Service
public class TopicInitiationService {
    private final TopicService topicService;
    private final ChapterFeignClient chapterFeignClient;
    private final ChatClient chatClient;

    public TopicInitiationService(TopicService topicService,
                                  ChapterFeignClient chapterFeignClient,
                                  ChatClient chatClient) {
        this.topicService = topicService;
        this.chapterFeignClient = chapterFeignClient;
        this.chatClient = chatClient;
    }

    @Async
    public void initializeTopicAsync(Topic saved) {
        var status = Status.INITIALIZING;
        try {
            final var chapter = chapterFeignClient.get(saved.getChapterId());
            final var subject = chapter.subject();
            final var topicName = saved.getName();
            final var chapterName = chapter.name();
            final var subjectName = subject.name();
            final var systemMessage = getInitialSystemMessage(topicName, chapterName, subjectName);
            topicService.add(saved.getId().toString(), systemMessage);
            final var assistantMessage = new ChatMessage();
            assistantMessage.setMessageType(MessageType.ASSISTANT);
            assistantMessage.setText(chatClient.prompt(
                    Prompt.builder()
                            .messages(topicService.get(saved.getId().toString()))
                            .build()
            ).stream().content().collectList().block().stream().collect(Collectors.joining()));
            topicService.add(saved.getId().toString(), assistantMessage);
            status = Status.INITIALIZED;
        } catch (Exception e) {
            status = Status.FAILED;
            throw new RuntimeException(e);
        } finally {
            saved = topicService.retrieve(saved.getId()).get();
            saved.setStatus(status);
            topicService.update(saved, saved.getId());
        }
    }
}
