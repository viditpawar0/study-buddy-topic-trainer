package com.studybuddy.topic_trainer.configurations;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenAiConfig {
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    @Bean
    public ChatModel chatModel() {
        return OpenAiChatModel.builder()
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .build())
                .openAiApi(OpenAiApi.builder()
                        .baseUrl(baseUrl.replaceAll("/v\\d+/?$", ""))
                        .apiKey(apiKey)
                        .build())
                .build();
    }
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
