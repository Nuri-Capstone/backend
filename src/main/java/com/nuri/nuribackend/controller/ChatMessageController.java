package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.service.ChatMessageService;
import com.nuri.nuribackend.service.ChatSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/msg")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final ChatSummaryService chatSummaryService;

    public ChatMessageController(ChatMessageService chatMessageService, ChatSummaryService chatSummaryService) {
        this.chatMessageService = chatMessageService;
        this.chatSummaryService = chatSummaryService;
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByChatId(@PathVariable Integer chatId) {
        List<ChatMessageDto> messages = chatMessageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/summary/{chatId}")
    public ResponseEntity<Map<String, Object>> getSummaryByChatId(@PathVariable Integer chatId) {
        chatSummaryService.getChatSummary(chatId);
        String summary = chatMessageService.getSummaryByChatId(chatId);
        Map<String, Object> response = new HashMap<>();
        response.put("summary", summary);
        return ResponseEntity.ok(response);
    }
}

