package com.nuri.nuribackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @Column(name = "subject", nullable = false, columnDefinition = "TEXT")
    private String subject;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "user_msg_cnt", nullable = false)
    private Integer userMsgCnt;

}
