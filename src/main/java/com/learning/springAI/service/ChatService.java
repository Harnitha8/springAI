package com.learning.springAI.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class ChatService {

    private final ChatClient ollamaChatClient;
    private final ChatClient gemmaChatClient;
    private final ChatClient chatClientWithDefaults;

    @Value("classpath:promptTemplates/systemMessage.st")
    private Resource systemMessage;

    @Value("classpath:promptTemplates/promptStuffing.st")
    private Resource promptStuffing;


    public ChatService(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("gemmaChatClient") ChatClient gemmaChatClient,
            @Qualifier("chatClientWithDefaults") ChatClient chatClientWithDefaults) {
        this.ollamaChatClient = ollamaChatClient;
        this.gemmaChatClient = gemmaChatClient;
        this.chatClientWithDefaults= chatClientWithDefaults;
    }

    public String chatWithOllama(String prompt) {
        return ollamaChatClient.prompt(prompt).call().content();
    }

    public String chatWithGemma(String prompt) {
        return gemmaChatClient.prompt(prompt).call().content();
    }

    public String chatWithDefaults(String prompt) {
        return chatClientWithDefaults.prompt(prompt).call().content();
    }

    public String chatWithDefaultsOverride(String prompt) {
        return chatClientWithDefaults.prompt(prompt).user(prompt).call().content();
    }

    public String chatUsingStringTemplate(String programmingLanguage, String prompt) {
        return ollamaChatClient.prompt()
                .system(systemSpec -> systemSpec
                        .text(systemMessage)
                        .param("programmingLanguage", programmingLanguage))
                .user(prompt)
                .call()
                .content();
    }
    public String chatAboutTechwave(String prompt) {
        return ollamaChatClient.prompt()
                .system(promptStuffing)
                .user(prompt)
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .build())
                .call()
                .content();
    }

    public Flux<String> streamChat(String prompt) {
        return ollamaChatClient.prompt().user(prompt).stream().content().bufferTimeout(20, Duration.ofMillis(200)) // group tokens
                .map(tokens -> String.join("", tokens)); 
    }

    public String toolsChat(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .functions("com.example.springai.tools")
                .call()
                .content();
    }
}
