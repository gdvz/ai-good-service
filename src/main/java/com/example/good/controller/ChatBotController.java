package com.example.good.controller;

import com.example.good.config.AiToolsConfig;
import com.example.good.service.EmailService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {

    @Autowired
    private GoogleGenAiChatModel chatModel;

    @GetMapping("/chat")
    public String chatToAction(@RequestParam String message) {

        return ChatClient.create(this.chatModel)
                .prompt(message)
                .tools(new EmailService())
                .tools(new AiToolsConfig())
                .call()
                .content();

    }

}
