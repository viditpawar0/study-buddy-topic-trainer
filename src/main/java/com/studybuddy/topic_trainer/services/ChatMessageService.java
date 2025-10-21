package com.studybuddy.topic_trainer.services;

import com.studybuddy.topic_trainer.entities.ChatMessage;
import com.studybuddy.topic_trainer.repositories.ChatMessageRepository;
import com.studybuddy.topic_trainer.utils.Utils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final TopicService topicService;
    private final ChatClient chatClient;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              TopicService topicService,
                              ChatModel chatModel) {
        this.chatMessageRepository = chatMessageRepository;
        this.topicService = topicService;
        this.chatClient = ChatClient.create(chatModel);
    }

    public ChatMessage create(Long id, String text) {
        var userChatMessage = new ChatMessage();
        userChatMessage.setMessageType(MessageType.USER);
        userChatMessage.setText(text);
        topicService.add(id.toString(), userChatMessage);
        var assistantChatMessage = new ChatMessage();
        assistantChatMessage.setMessageType(MessageType.ASSISTANT);
        assistantChatMessage.setText(chatClient.prompt(
                Prompt.builder()
                        .messages(topicService.get(id.toString()))
                        .build()
        ).call().content());
        topicService.add(id.toString(), assistantChatMessage);
        return assistantChatMessage;
    }

    public Optional<ChatMessage> retrieve(Long id) {
        return chatMessageRepository.findById(id);
    }

    public Iterable<ChatMessage> retrieveByTopicId(Long topicId) {
        topicService.assertEntityExists(topicId);
        return chatMessageRepository.findByTopic_Id((Long.parseLong(topicId.toString())));
    }

    public ChatMessage update(ChatMessage chatMessage, Long id) {
        assertEntityExists(id);
        return chatMessageRepository.save(chatMessage);
    }

    public void delete(ChatMessage chatMessage) {
        assertEntityExists(chatMessage.getId());
        chatMessageRepository.delete(chatMessage);
    }

    public void delete(Long id) {
        assertEntityExists(id);
        chatMessageRepository.deleteById(id);
    }

    public void assertEntityExists(Long chatMessageId) {
        Utils.assertEntityExists(chatMessageId, chatMessageRepository);
    }
}
