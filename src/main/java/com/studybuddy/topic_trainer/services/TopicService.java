package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.Topic;
import com.studybuddy.topic_trainer.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic create(Topic topic) {
        return topicRepository.save(topic);
    }

    public Optional<Topic> retrieve(Long id) {
        return topicRepository.findById(id);
    }

    public List<Topic> retrieveByChapterId(Long chapterId) {
        return topicRepository.findByChapterId(chapterId);
    }

    public Topic update(Topic topic) {
        return topicRepository.save(topic);
    }

    public void delete(Topic topic) {
        topicRepository.delete(topic);
    }

    public void delete(Long id) {
        topicRepository.deleteById(id);
    }
}
