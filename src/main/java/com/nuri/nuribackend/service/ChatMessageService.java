package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.dto.ChatMessageDto;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessageDto> getMessagesByChatId(Integer chatId) {
        List<ChatMessage> messages = chatMessageRepository.findAllByChatId(chatId);
        return messages.stream()
                .map(ChatMessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
