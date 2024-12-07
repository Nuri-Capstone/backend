package com.nuri.nuribackend.dto;

import com.nuri.nuribackend.domain.Chat;
import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.dto.User.UserDto;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChatDto {
    private Integer chatId;
    private String subject;
    private String summary;
    private User user;
    private Integer user_msg_cnt;
    private Date date;

    static public ChatDto toDto(Chat chat){
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .user(chat.getUser())
                .date(chat.getDate())
                .user_msg_cnt(chat.getUserMsgCnt())
                .summary(chat.getSummary())
                .subject(chat.getSubject())
                .build();
    }

    public Chat toEntity(){
        return Chat.builder()
                .chatId(chatId)
                .user(user)
                .date(date)
                .userMsgCnt(user_msg_cnt)
                .summary(summary)
                .subject(subject)
                .build();
    }
}