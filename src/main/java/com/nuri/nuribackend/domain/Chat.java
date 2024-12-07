package com.nuri.nuribackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "subject", nullable = true, columnDefinition = "TEXT")
    private String subject;

    @Column(name = "summary", nullable = true, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "user_msg_cnt", nullable = true)
    private Integer userMsgCnt;

    public void incrementCount() {
        this.userMsgCnt++;
    }
}
