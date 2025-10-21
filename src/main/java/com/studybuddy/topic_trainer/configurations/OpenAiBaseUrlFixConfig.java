package com.studybuddy.topic_trainer.configurations;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("dev")
public class OpenAiBaseUrlFixConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();

        // Take the configured base URL and strip `/v1` if present
        String currentUrl = environment.getProperty("spring.ai.openai.base-url");
        String fixedUrl = currentUrl.replaceAll("/v\\d+/?$", ""); // remove trailing /v1

        Map<String, Object> map = new HashMap<>();
        map.put("spring.ai.openai.base-url", fixedUrl);

        environment.getPropertySources().addFirst(new MapPropertySource("openai-url-fix", map));
    }
}
