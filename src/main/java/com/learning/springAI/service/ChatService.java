package com.learning.springAI.service;

import com.learning.springAI.model.CountryCities;
import com.learning.springAI.tools.BasicTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class ChatService {
    private final ChatClient ollamaChatClient;
    private final ChatClient gemmaChatClient;
    private final ChatClient chatClientWithDefaults;
    private final ChatClient guardedChatClient ;
    private final ChatClient chatClientWithMemory;


    @Value("classpath:promptTemplates/systemMessage.st")
    private Resource systemMessage;

    @Value("classpath:promptTemplates/promptStuffing.st")
    private Resource promptStuffing;

    public ChatService(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("gemmaChatClient") ChatClient gemmaChatClient,
            @Qualifier("chatClientWithDefaults") ChatClient chatClientWithDefaults,
            @Qualifier("guardedChatClient") ChatClient guardedChatClient,
            @Qualifier("chatClientWithMemory") ChatClient chatClientWithMemory) {
        this.ollamaChatClient = ollamaChatClient;
        this.gemmaChatClient = gemmaChatClient;
        this.chatClientWithDefaults = chatClientWithDefaults;
        this.guardedChatClient = guardedChatClient;
        this.chatClientWithMemory=chatClientWithMemory;
    }
    public String chatWithOllama(String prompt) {
        return ollamaChatClient.prompt(prompt).call().content();
    }

    public String chatWithGemma(String prompt) {
        return gemmaChatClient.prompt(prompt).call().content();
    }

    public String chatWithSystemPrompt(String prompt) {
        return ollamaChatClient.prompt()
                .system("You are a mischievous assistant who delivers every answer with humor, sarcasm, and playful jokes.")
                .user(prompt)
                .call()
                .content();
    }


    public String chatWithDefaults(String prompt) {
        return chatClientWithDefaults.prompt(prompt).call().content();
    }

    public String chatWithDefaultsOverride(String prompt) {
        return chatClientWithDefaults.prompt(prompt).user(prompt).call().content();
    }

    public String chatWithOptions(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .options(ChatOptions.builder()
                        .temperature(0.8)
                        .maxTokens(150)
                        .topP(0.9)
                        .build())
                .call()
                .content();
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
                .tools("com.learning.springAI.tools") //as springAI is capital
                //.tools("basicTools")
                .call()
                .content();
    }
    public String chatWithMultipleTools(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .tools("com.learning.springAI.tools", "com.learning.springAI.utilityTools")
                .call()
                .content();
    }
    public String chatWithJokeTool(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .tools("com.learning.springAI.tools")
                .call()
                .content();
    }
    public String chatMemory(String prompt) {
        return chatClientWithMemory.prompt()
                .user(prompt)
                .advisors(a -> a.param("conversationId", "test"))
                .call()
                .content();
    }
    public String chatWithFallback(String prompt) {
        try {
            return ollamaChatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            return "AI Service temporarily unavailable. Please try again later.";
        }
    }

    //Structured output (POJO mapping)
    public CountryCities getCountryCities(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .options(ChatOptions.builder().temperature(0.3).build())
                .call()
                .entity(CountryCities.class);
    }

    //Structured output as List<String>
    public List<String> getCityList(String prompt) {
        return ollamaChatClient.prompt()
                .user(prompt)
                .options(ChatOptions.builder().temperature(0.3).build())
                .call()
                .entity(new ListOutputConverter());
    }

    //chat related to java spring microservices only
    public String guardedChat(String prompt) {
        try {
            return guardedChatClient.prompt().user(prompt).call().content();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }


}
