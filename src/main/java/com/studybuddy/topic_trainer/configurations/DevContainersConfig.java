package com.studybuddy.topic_trainer.configurations;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@TestConfiguration
public class DevContainersConfig {
    @Bean
    @ServiceConnection
    LocalStackContainer localStackContainer() throws IOException, InterruptedException {
        LocalStackContainer localStackContainer = new LocalStackContainer(
                DockerImageName.parse("localstack/localstack:4.7.0")
        ).withReuse(true);
        localStackContainer.start();
        localStackContainer.execInContainer("awslocal", "s3", "mb", "s3://study-buddy");
        return localStackContainer;
    }
}
