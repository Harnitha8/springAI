package com.learning.springAI.controller;

import com.learning.springAI.model.CountryCities;
import com.learning.springAI.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ollama/chat/{prompt}")
    public ResponseEntity<String> ollamaChat(@PathVariable String prompt) {
        String response = chatService.chatWithOllama(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gemma/chat/{prompt}")
    public ResponseEntity<String> gemmaChat(@PathVariable String prompt) {
        String response = chatService.chatWithGemma(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/system/chat/{prompt}")
    public ResponseEntity<String> chatWithSystemPrompt(@PathVariable String prompt) {
        return ResponseEntity.ok(chatService.chatWithSystemPrompt(prompt));
    }

    @GetMapping("/defaults/chat/{prompt}")
    public ResponseEntity<String> chatWithDefaults(@PathVariable String prompt) {
        String response = chatService.chatWithDefaults(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/defaultsOverride/chat/{prompt}")
    public ResponseEntity<String> chatWithDefaultsOverride(@PathVariable String prompt) {
        String response = chatService.chatWithDefaultsOverride(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/options/chat/{prompt}")
    public ResponseEntity<String> chatWithOptions(@PathVariable String prompt) {
        return ResponseEntity.ok(chatService.chatWithOptions(prompt));
    }

    @GetMapping("/stringTemplate/chat/{programmingLanguage}/{prompt}")
    public ResponseEntity<String> chatUsingStringTemplate(@PathVariable String programmingLanguage,@PathVariable String prompt) {
        String response = chatService.chatUsingStringTemplate(programmingLanguage,prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/promptStuffing/chatAboutTechwave/{prompt}")
    public ResponseEntity<String> chatAboutTechwave(@PathVariable String prompt) {
        String response = chatService.chatAboutTechwave(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/stream/chat/{prompt}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@PathVariable String prompt) {
        return chatService.streamChat(prompt);
    }

    @GetMapping("/tools/chat/{prompt}")
    public String toolsChat(@PathVariable String prompt) {
        return chatService.toolsChat(prompt);
    }

    @GetMapping("/multiTools/chat/{prompt}")//Multiply 10 and 5 and tell me today’s date
    public ResponseEntity<String> chatWithMultipleTools(@PathVariable String prompt) {
        return ResponseEntity.ok(chatService.chatWithMultipleTools(prompt));
    }
    @GetMapping("tools/external-api/joke/{prompt}")
    public String callExternalApiUsingTools(@PathVariable String prompt) {
        return chatService.chatWithJokeTool(prompt);
    }

    @GetMapping("/memory/chat/{prompt}")
    public String chatMemory(@PathVariable String prompt) {
        return chatService.chatMemory(prompt);
    }

    @GetMapping("/fallback/chat/{prompt}")
    public ResponseEntity<String> chatWithFallback(@PathVariable String prompt) {
        return ResponseEntity.ok(chatService.chatWithFallback(prompt));
    }

    //Structured output → CountryCities record
    @GetMapping("/country-cities/chat/{prompt}") //List 3 cities in India
    public ResponseEntity<CountryCities> getCountryCities(@PathVariable String prompt) {
        CountryCities countryCities = chatService.getCountryCities(prompt);
        return ResponseEntity.ok(countryCities);
    }

    //Structured list output
    @GetMapping("/cities/chat/{prompt}")
    public ResponseEntity<List<String>> getCityList(@PathVariable String prompt) {
        List<String> cities = chatService.getCityList(prompt);
        return ResponseEntity.ok(cities);
    }

    //It filters or validates user input before sending it to the AI model.
    //For example, the TechGuardConfig only allows prompts related to Java, Spring, or Microservices.
    @GetMapping("/guarded/chat/{prompt}")
    public String guarded(@PathVariable String prompt) {
        return chatService.guardedChat(prompt);
    }


}
