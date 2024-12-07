package com.nuri.nuribackend.dto;

import com.nuri.nuribackend.domain.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessageDto {
    private String id;
    private Integer chatId;
    private String msgType;
    private String msgText;
    private String msgSound;
    private LocalDateTime timeStamp;

    public static ChatMessageDto fromEntity(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.id = message.getId();
        dto.msgType = message.getMsgType();
        dto.msgText = message.getMsgText();
        dto.timeStamp = message.getTimeStamp();
        return dto;
    }
}