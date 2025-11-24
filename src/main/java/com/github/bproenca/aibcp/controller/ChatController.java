package com.github.bproenca.aibcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @GetMapping("/ping")
    public String chat() {
        log.info("Ping at {}", LocalDateTime.now());
        return "pong: " + LocalDateTime.now();
    }
}