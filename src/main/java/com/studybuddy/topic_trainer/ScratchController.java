package com.studybuddy.topic_trainer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scratch2")
public class ScratchController {
    @GetMapping
    public String scratch() {
        return "Hello from Topic Trainer!";
    }
}
