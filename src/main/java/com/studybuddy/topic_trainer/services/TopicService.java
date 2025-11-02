package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.entities.Status;
import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.feign_clients.ChapterFeignClient;
import com.studybuddy.topic_trainer.repositories.TopicRepository;
import com.studybuddy.topic_trainer.utils.Utils;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService implements ChatMemory {
    private final TopicRepository topicRepository;
    private final TopicInitiationService topicInitiationService;
    private final ChapterFeignClient chapterFeignClient;

    public TopicService(TopicRepository topicRepository,
                        @Lazy TopicInitiationService topicInitiationService,
                        ChapterFeignClient chapterFeignClient) {
        this.topicRepository = topicRepository;
        this.topicInitiationService = topicInitiationService;
        this.chapterFeignClient = chapterFeignClient;
    }

    public Iterable<Topic> create(Long chapterId, Iterable<Topic> topics) {
        assertChapterExists(chapterId);
        topics.forEach(topic -> {
            topic.setChapterId(chapterId);
            topic.setStatus(Status.INITIALIZING);
        });
        List<Topic> saved = topicRepository.saveAll(topics);
        saved.forEach(topicInitiationService::initializeTopicAsync);
        return saved;
    }


    public Optional<Topic> retrieve(Long id) {
        return topicRepository.findById(id);
    }

    public List<Topic> retrieveByChapterId(Long chapterId) {
        assertChapterExists(chapterId);
        return topicRepository.findByChapterId(chapterId);
    }

    public Topic update(Topic newTopic, Long id) {
        final var oldTopic = assertEntityExists(id);
        if (newTopic.getName() != null) {
            oldTopic.setName(newTopic.getName());
        }
        return topicRepository.save(oldTopic);
    }

    public void delete(Topic topic) {
        assertEntityExists(topic.getId());
        topicRepository.delete(topic);
    }

    public void delete(Long id) {
        assertEntityExists(id);
        topicRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByChapterId(Long chapterId) {
        assertChapterExists(chapterId);
        topicRepository.deleteAllByChapterId(chapterId);
    }

    @Override
    public void add(@NonNull String conversationId, @NonNull List<Message> messages) {
        final var topic = assertEntityExists(Long.parseLong(conversationId));
        messages.forEach(message -> {
            var chatMessage = new ChatMessage();
            chatMessage.setMessageType(message.getMessageType());
            chatMessage.setText(message.getText());
            topic.addMessages(chatMessage);
        });
        topicRepository.save(topic);
    }

    @NonNull
    @Override
    public List<Message> get(@NonNull String conversationId) {
        ArrayList<Message> messages = new ArrayList<>(assertEntityExists(Long.parseLong(conversationId)).getMessages());
        messages.addFirst(new UserMessage("Hello"));
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            if (message.getMessageType() == MessageType.ASSISTANT) {
                messages.remove(i);
                messages.add(i, new AssistantMessage(message.getText(), message.getMetadata()));
            }
        }
        return messages;
    }

    @Override
    public void clear(@NonNull String conversationId) {
        final var topic = assertEntityExists(Long.parseLong(conversationId));
        topic.clearMessages();
        topicRepository.save(topic);
    }

    public Topic assertEntityExists(Long topicId) {
        return Utils.assertEntityExists(topicId, topicRepository);
    }

    private void assertChapterExists(Long chapterId) {
        try {
            chapterFeignClient.get(chapterId);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new EntityNotFoundException(e);
            } else  {
                throw e;
            }
        }
    }
}
