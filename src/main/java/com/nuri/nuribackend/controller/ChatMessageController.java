package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/msg")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByChatId(@PathVariable Integer chatId) {
        List<ChatMessageDto> messages = chatMessageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/summary/{chatId}")
    public ResponseEntity<String> getSummaryByChatId(@PathVariable Integer chatId) {
        String summary = chatMessageService.getSummaryByChatId(chatId);
        return ResponseEntity.ok(summary);
    }
}

