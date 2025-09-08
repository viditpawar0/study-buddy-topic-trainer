package com.studybuddy.topic_trainer;

import com.studybuddy.topic_trainer.configurations.DevContainersConfig;
import org.springframework.boot.SpringApplication;

public class TopicTrainerDevApplication {
    public static void main(String[] args) {
        SpringApplication.from(TopicTrainerApplication::main)
                .with(DevContainersConfig.class)
                .run(args);
    }
}
