package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.dto.ChatDto;
import com.nuri.nuribackend.dto.User.SignUpDto;
import com.nuri.nuribackend.dto.User.UserDto;
import com.nuri.nuribackend.exception.CustomException;
import com.nuri.nuribackend.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatService {
    private final ChatRepository chatRepository;
    @Transactional
    public ChatDto addChat(ChatDto chatDto) {
        try {
            Chat chat = chatDto.toEntity();
            Chat savedChat = chatRepository.save(chat);
            ChatDto response = ChatDto.toDto(savedChat);
            return response;
        } catch (CustomException ex){
            log.error("Error adding user: {}", ex.getMessage());
            throw new CustomException(ex.getErrorCode(), ex.getMessage());
        }
    }

    @Transactional
    public void addChatCnt(Integer chatId){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with id: " + chatId));

        chat.incrementCount();
    }

    @Transactional
    public ChatDto getChatByChatId(Integer id){
        try{
            Chat chat = chatRepository.findById(id)
                    .orElseThrow(() -> new CustomException("USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."));
            return ChatDto.toDto(chat);
        } catch (Exception ex){
            log.error("Error retrieving Chat by ID {}: {}", id, ex.getMessage());
            throw new CustomException("DATABASE_ERROR", "사용자 조회 중 오류가 발생했습니다.");
        }
    }
}
