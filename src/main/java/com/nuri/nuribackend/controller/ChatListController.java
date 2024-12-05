package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.dto.Chat.ChatDto;
import com.nuri.nuribackend.service.ChatListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chatList")
@RequiredArgsConstructor
public class ChatListController {

    private final ChatListService chatListService;

    @GetMapping
    public ResponseEntity<List<ChatDto>> getAllChat() {
        Integer userId = 1; // userId 임시로 1
        List<ChatDto> chatList;
        chatList = chatListService.getAllChatByUserId(userId);
        System.out.println("채팅 리스트: " + chatList);
        return ResponseEntity.ok(chatList);
    }
}
