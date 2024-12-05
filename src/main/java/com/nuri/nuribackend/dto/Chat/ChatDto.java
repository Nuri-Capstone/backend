package com.nuri.nuribackend.dto.Chat;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private Integer chatId;
    private User user;
    private String subject;
    private String summary;
    private Date date;
    private Integer userMsgCnt;

    public static ChatDto fromEntity(Chat chat) {
        return new ChatDto(
                chat.getChatId(),
                chat.getUser(),
                chat.getSubject(),
                chat.getSummary(),
                chat.getDate(),
                chat.getUserMsgCnt()
        );
    }
}
