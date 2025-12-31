package com.github.bproenca.aibcp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.oracle.bmc.ClientConfiguration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import com.oracle.bmc.generativeaiinference.responses.ChatResponse;
import com.oracle.bmc.retrier.RetryConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GenericChatController {

    private static final Logger log = LoggerFactory.getLogger(GenericChatController.class);

    @Value( "${aibcp.compartment}" )
    private String OCI_COMPARTMENT;
    @Value( "${aibcp.cohere.model}" )
    private String OCI_COHERE_MODEL;
    @Value( "${aibcp.llama.model}" )
    private String OCI_LLAMA_MODEL;

    private Map<String, String> models;

    private final GenerativeAiInferenceClient chatClient;

    public GenericChatController(GenerativeAiInferenceClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/generic/chat")
    public ChatResponse generate(@RequestParam(value = "model", defaultValue = "cohere") String modelInput, @RequestParam(value = "message", defaultValue = "Tell me a joke") String messageInput) {
        this.models = Map.of(
                "cohere", OCI_COHERE_MODEL,
                "llama", OCI_LLAMA_MODEL);

        log.info(">>> Calling mode {} with message {}", modelInput, messageInput);
        BaseChatRequest chatRequest = buildChatRequest(modelInput, messageInput);

        ChatDetails details = ChatDetails.builder()
                .servingMode(OnDemandServingMode.builder().modelId(models.get(modelInput))
                        .build())
                .compartmentId(OCI_COMPARTMENT)
                .chatRequest(chatRequest)
                .build();
        ChatRequest request = ChatRequest.builder()
                .chatDetails(details)
                .build();

        ChatResponse response = chatClient.chat(request);
        System.out.println("####################################################");
        System.out.println(response.toString());
        return response;
    }

    private BaseChatRequest buildChatRequest(String model, String messageInput) {
        BaseChatRequest chatRequest;
        if ("cohere".equalsIgnoreCase(model)) {
            chatRequest = CohereChatRequest.builder()
                    .message(messageInput)
                    .maxTokens(600)
                    .temperature((double)0.25)
                    .frequencyPenalty((double)1)
                    .topP((double)0.75)
                    .topK(0)
                    .isStream(false)
                    .build();
        } else {
            ChatContent content = TextContent.builder()
                    .text(messageInput)
                    .build();

            Message messageAI = UserMessage.builder()
                    .content(Arrays.asList(content))
                    .build();

            chatRequest = GenericChatRequest.builder()
                    .messages(Arrays.asList(messageAI))
                    .maxTokens(600)
                    .temperature(0.25)
                    .frequencyPenalty((double) 1)
                    .presencePenalty((double) 0)
                    .topP(0.75)
                    .isStream(false)
                    .build();
        }
        return chatRequest;

    }
}