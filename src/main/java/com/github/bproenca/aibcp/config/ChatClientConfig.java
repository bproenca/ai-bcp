package com.github.bproenca.aibcp.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient (ChatClient.Builder chatClientBuilder) {
        ChatOptions chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini")
                .build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
    }

    @Bean
    public ChatClient geminiChatClient (ChatClient.Builder chatClientBuilder) {
        ChatOptions chatOptions = ChatOptions.builder()
                .model("gemini-2.5-flash-lite")
                .build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
    }

    @Bean
    public ChatClient deepseekChatClient (ChatClient.Builder chatClientBuilder) {
        ChatOptions chatOptions = ChatOptions.builder()
                .model("deepseek-r1:1.5b")
                .build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .build();
    }
}