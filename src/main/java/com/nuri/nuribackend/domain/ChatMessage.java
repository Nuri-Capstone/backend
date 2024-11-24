package com.nuri.nuribackend.domain;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_message")
@Data
public class ChatMessage {

    @Id
    private String id;          // feedback 저장할 때 feedback 콜렉션의 msgId 필드에 저장

    private Integer chatId;     // 채팅방 ID (Chat rdbms의 기본키임)

    private String msgType;     // 채팅 보낸 사용자(user OR gpt)

    private String msgText;     // 채팅 내용 텍스트

    private String msgSound;    // 채팅 내용 음성 S3 URL

    private LocalDateTime timeStamp;
}
