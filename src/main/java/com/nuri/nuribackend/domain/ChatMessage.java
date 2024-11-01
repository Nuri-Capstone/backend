package com.nuri.nuribackend.domain;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_message")
@Data
public class ChatMessage {

    @Id
    private String msgId;       // 말풍선 ID
    private Integer chatId;     // 채팅방 ID

    private String msgType;     // 채팅 보낸 사용자(user OR gpt)

    private String msgText;     // 채팅 내용 텍스트

    private String msgSound;    // 채팅 내용 음성 S3 URL
}
