package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.dto.ChatMessageDto;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
