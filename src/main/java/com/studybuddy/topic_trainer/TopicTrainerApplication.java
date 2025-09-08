package com.studybuddy.topic_trainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TopicTrainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TopicTrainerApplication.class, args);
    }

}
