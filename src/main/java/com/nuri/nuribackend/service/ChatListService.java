package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.dto.Chat.ChatDto;
import com.nuri.nuribackend.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatListService {

    private final ChatRepository chatRepository;

    public List<ChatDto> getAllChatByUserId(Integer userId) {
        List<Chat> chatList = chatRepository.findAllByUserId(userId);
        return chatList.stream()
                .map(ChatDto::fromEntity)
                .collect(Collectors.toList());
    }

}
