package com.nuri.nuribackend.service;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.repository.ChatRepository;
import com.nuri.nuribackend.service.home.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatSummaryService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final OpenAiService openAiService;

    @Autowired
    public ChatSummaryService(
            ChatRepository chatRepository,
            ChatMessageRepository chatMessageRepository,
            OpenAiService openAiService
            ) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.openAiService = openAiService;
    }

    public void getChatSummary(Integer chatId) {
        /*
         * 1. 채팅방 조회(chatId)
         * 2. 채팅방에서 모든 메세지(user, gpt) 조회
         * 3. 메세지를 gpt에게 "한 문장으로 요약해줘"라는 요청을 보냄
         * 4. 응답으로 받은 채팅방 대화 요약을 chatId 객체의 summary 필드에 저장함
        */

        Chat chat = chatRepository.findByChatId(chatId);

        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);

        // gpt 호출
        String summary = openAiService.getChatSummary(messages);
        log.info("채팅방 요약: " + summary);

        // return summary;

        chat.setSummary(summary);
        chatRepository.save(chat);
    }
}
