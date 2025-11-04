package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.Status;
import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.feign_clients.ChapterFeignClient;
import com.studybuddy.topic_trainer.repositories.TopicRepository;
import com.studybuddy.topic_trainer.utils.Utils;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
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

    public Topic update(Long id, Topic newTopic) {
        final var oldTopic = assertEntityExists(id);
        if (newTopic.getName() != null) {
            oldTopic.setName(newTopic.getName());
        }
        if (newTopic.getStatus() != null) {
            oldTopic.setStatus(newTopic.getStatus());
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
