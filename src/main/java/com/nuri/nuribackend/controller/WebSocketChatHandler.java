package com.nuri.nuribackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuri.nuribackend.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.util.HashSet;
import java.util.Set;

import com.nuri.nuribackend.repository.ChatMessageRepository;
import com.nuri.nuribackend.domain.ChatMessage;
import com.nuri.nuribackend.service.ChatMessageService;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ChatMessageRepository chatMessageRepository;
    private ChatMessageService chatMessageService;

    @Override //클라이언트와의 웹소켓 연결이 성립되면 호출
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session); //세션을 sessions에 추가
        log.info("Connected: {}", session.getId()); //연결된 클라이언트 id 기록
    }

    @Override //클라이언트가 보낸 메세지 처리
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageDto chatMessageDto = mapper.readValue(message.getPayload(), ChatMessageDto.class);

        if (chatMessageDto.getChatId() == null) {
            int newChatId = generateNewChatId();
            chatMessageDto.setChatId(newChatId);
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatMessageDto.getChatId());
        chatMessage.setMsgType(chatMessageDto.getMsgType());
        chatMessage.setMsgText(chatMessageDto.getMsgText());
        chatMessage.setMsgSound(chatMessageDto.getMsgSound());

        // MongoDB에 메시지 저장
        chatMessageRepository.save(chatMessage);

        // 응답 메시지를 클라이언트에게 전송
        String responseJson = mapper.writeValueAsString(chatMessage);
        session.sendMessage(new TextMessage(responseJson));

    }
    private Integer generateNewChatId() {
        return UUID.randomUUID().hashCode();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Disconnected: {}", session.getId());
    }

    private void broadcastMessage(ChatMessageDto message) {
        sessions.parallelStream().forEach(sess -> {
            try {
                sess.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
            } catch (Exception e) {
                log.error("Error sending message", e);
            }
        });
    }
}
