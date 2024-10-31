package com.nuri.nuribackend.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "chatlist")
public class ChatList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "subject", nullable = false, columnDefinition = "TEXT")
    private String subject;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "user_msg_cnt", nullable = false)
    private Integer userMsgCnt;
}
