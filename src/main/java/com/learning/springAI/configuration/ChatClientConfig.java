package com.learning.springAI.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
        // return ChatClient.create(chatModel);
        return ChatClient.builder(chatModel).build();
    }

    @Bean
    public ChatClient gemmaChatClient(OpenAiChatModel chatModel){
        return ChatClient.create(chatModel);
    }

    //defaults example
    @Bean
    public ChatClient chatClientWithDefaults(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a helpful assistant specialized only in Java.
                        - If the question is about Java (language, libraries, frameworks, JVM, etc.), answer clearly and concisely.
                        - If the question is about anything else, reply exactly with: "Sorry, I don't know."
                        """)
                .defaultUser("what is Java?")
                .defaultOptions(ChatOptions.builder().temperature(0.7).maxTokens(1000).build())
                .build();
    }
    /*@Bean
    public ChatMemory chatMemory() {
        return new ConversationMemory(new InMemoryConversationMemoryStore());
    }*/

    @Bean
    public ChatClient chatClientWithMemory(OllamaChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @Bean
    public ChatClient guardedChatClient(OllamaChatModel chatModel, SafeGuardAdvisor techQuestionGuard) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(techQuestionGuard))
                .build();
    }
}
