package com.github.bproenca.aibcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatMultiController {

    private final Map<String, ChatClient> chatClientDB;
    private Logger log = LoggerFactory.getLogger(ChatMultiController.class);

    public ChatMultiController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                               @Qualifier("deepseekChatClient") ChatClient deepseekChatClient,
                                    @Qualifier("geminiChatClient") ChatClient geminiChatClient) {
        chatClientDB = Map.of(
                "openai", openAiChatClient,
                "deepseek", deepseekChatClient,
                "gemini", geminiChatClient);
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam("message") String message,
            @RequestParam(name = "model", defaultValue = "openai") String model)
    {
        ChatClient chatClient =  chatClientDB.get(model);
        log.info(">> Chat message = {} model = {} obj {}", message, model, chatClient);
        return chatClient.prompt().user(message).call().content();
    }
}