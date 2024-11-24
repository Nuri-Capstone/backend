package com.nuri.nuribackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessageDto {
    private Integer msgId;
    private Integer chatId;
    private String msgType;
    private String msgText;
    private String msgSound;
    private LocalDateTime timeStamp;
}