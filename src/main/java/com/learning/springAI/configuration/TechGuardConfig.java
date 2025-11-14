package com.learning.springAI.configuration;

import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TechGuardConfig {
    @Bean
    public SafeGuardAdvisor techQuestionGuard() {
        List<String> forbidden = List.of("politics", "cinema");
        return SafeGuardAdvisor.builder()
                .sensitiveWords(forbidden)
                .failureResponse("Sorry, I can't.")
                .order(0)
                .build();
    }
}
