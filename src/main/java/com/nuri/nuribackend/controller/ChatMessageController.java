package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.service.ChatMessageService;
import com.nuri.nuribackend.service.ChatSummaryService;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.dto.ChatDto;
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
    private final ChatRepository chatRepository;

    public ChatMessageController(ChatMessageService chatMessageService, ChatSummaryService chatSummaryService, ChatRepository chatRepository) {
        this.chatMessageService = chatMessageService;
        this.chatSummaryService = chatSummaryService;
        this.chatRepository = chatRepository;
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

        Chat chat = chatRepository.findByChatId(chatId);
        chat.setSummary(summary);
        chat.setSubject(summary);
        chatRepository.save(chat);

        Map<String, Object> response = new HashMap<>();
        response.put("summary", summary);
        return ResponseEntity.ok(response);
    }
}

