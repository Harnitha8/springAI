package com.learning.springAI.controller;

import com.learning.springAI.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/defaults/chat/{prompt}")
    public ResponseEntity<String> chatWithDefaults(@PathVariable String prompt) {
        String response = chatService.chatWithDefaults(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/defaultsOverride/chat/{prompt}")
    public ResponseEntity<String> chatWithDefaultsOverride(@PathVariable String prompt) {
        String response = chatService.chatWithDefaults(prompt);
        return ResponseEntity.ok(response);
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


}
