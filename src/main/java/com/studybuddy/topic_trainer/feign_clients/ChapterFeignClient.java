package com.studybuddy.topic_trainer.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "subject-trainer", path = "chapters")
public interface ChapterFeignClient {
    @GetMapping("{id}")
    ChapterDTO get(@PathVariable Long id);
}
