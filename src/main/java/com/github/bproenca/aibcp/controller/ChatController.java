package com.github.bproenca.aibcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        log.info("Chat for message {}", message);
        return chatClient.prompt(message).call().content();
    }

    @GetMapping("/ping")
    public String chat() {
        log.info("Ping at {}", LocalDateTime.now());
        return "pong: " + LocalDateTime.now();
    }
}